package com.stylelens.www.ai.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.stylelens.www.ai.service.AiService;
import com.stylelens.www.config.AiConfig;
import com.stylelens.www.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.springframework.ai.model.Media;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    private final ChatModel chatModel;

    private final ImageModel imageModel;

    private final AiConfig aiConfig;

    private final RetryTemplate retryTemplate;

    @Override
    public String chat(String message) {
        log.info("开始文本生成请求, 消息: {}", message);
        try {
            return retryTemplate.execute(context -> 
                chatClient.prompt()
                    .user(message)
                    .call()
                    .content()
            );
        } catch (Exception e) {
            log.error("文本生成失败", e);
            throw new BusinessException("AI服务调用失败: " + e.getMessage());
        }
    }

    @Override
    public String chatWithSystem(String message, String systemPrompt) {
        log.info("开始带系统提示的对话, 消息: {}, 系统提示: {}", message, systemPrompt);
        try {
            String prompt = systemPrompt != null ? systemPrompt : aiConfig.getSystemPrompt();
            return retryTemplate.execute(context -> 
                chatClient.prompt()
                    .system(prompt)
                    .user(message)
                    .call()
                    .content()
            );
        } catch (Exception e) {
            log.error("带系统提示的对话失败", e);
            throw new BusinessException("AI服务调用失败: " + e.getMessage());
        }
    }

    @Override
    public String generateImage(String prompt) {
        log.info("开始图片生成请求, 提示词: {}", prompt);
        try {
            return retryTemplate.execute(context -> {
                String[] sizeParts = aiConfig.getImage().getOptions().getSize().split("\\*");
                ImageOptions options = DashScopeImageOptions.builder()
                    .withModel(aiConfig.getImage().getOptions().getModel())
                    .withHeight(Integer.parseInt(sizeParts[1]))
                    .withWidth(Integer.parseInt(sizeParts[0]))
                    .build();

                ImagePrompt imagePrompt = new ImagePrompt(prompt, options);
                var response = imageModel.call(imagePrompt);
                
                if (response != null && response.getResult() != null) {
                    String imageUrl = response.getResult().getOutput().getUrl();
                    log.info("图片生成成功, URL: {}", imageUrl);
                    return imageUrl;
                }
                throw new BusinessException("图片生成失败");
            });
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("图片生成失败", e);
            throw new BusinessException("AI图片服务调用失败: " + e.getMessage());
        }
    }

    @Override
    public String analyzeUserStyle(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "上传的照片不能为空");
        }

        log.info("接收到用户照片分析请求, 文件名: {}, 大小: {} bytes", file.getOriginalFilename(), file.getSize());

        try {
            byte[] imageBytes = file.getBytes();
            String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";

            String systemDiagnosticPrompt = """
                你是一个资深的个人形象顾问与色彩分析专家。请仔细审视用户上传的正脸半身照片。
                分析其面部特征并计算其色彩属性，最终返回标准的 JSON 格式报告。
                
                请严格按照以下 JSON 格式结构返回，不要包含任何 Markdown 标记（如 ```json 等），确保代码可以直接转换为实体类：
                {
                  "gender": "男/女",
                  "skinTone": "冷色调/暖色调/中性调",
                  "brightness": "高明度/中等偏高/中等/低明度",
                  "contrast": "高对比度/中等对比/低对比度",
                  "colorSeason": "冷夏/暖秋/暖春/冷冬",
                  "faceShape": "如：椭圆形/方形/心形",
                  "styleKeywords": ["清爽", "知性", "简洁", "优雅"]
                }
                """;

            Media media = new Media(
                MimeTypeUtils.parseMimeType(contentType),
                new ByteArrayResource(imageBytes)
            );

            UserMessage userMessage = new UserMessage(
                systemDiagnosticPrompt,
                List.of(media)
            );

            DashScopeChatOptions visionOptions = aiConfig.toVisionOptions();

            return retryTemplate.execute(context -> {
                var response = chatModel.call(
                    new org.springframework.ai.chat.prompt.Prompt(userMessage, visionOptions)
                );
                if (response != null && response.getResult() != null) {
                    String jsonResult = response.getResult().getOutput().getContent();
                    log.info("多模态图像分析成功。返回数据: {}", jsonResult);
                    return jsonResult;
                }
                throw new BusinessException("视觉模型分析未返回有效数据");
            });

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用视觉模型进行多模态分析时发生异常", e);
            throw new BusinessException("个人色彩分析服务异常: " + e.getMessage());
        }
    }
}
