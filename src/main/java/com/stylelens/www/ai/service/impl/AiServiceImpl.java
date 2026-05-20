package com.stylelens.www.ai.service.impl;

import com.stylelens.www.ai.service.AiService;
import com.stylelens.www.config.AiConfig;
import com.stylelens.www.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageOptions;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

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
}
