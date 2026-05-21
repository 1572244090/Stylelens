package com.stylelens.www.ai.service;

import org.springframework.web.multipart.MultipartFile;

public interface AiService {

    String chat(String message);

    String chatWithSystem(String message, String systemPrompt);

    String generateImage(String prompt);

    /**
     * 新增：上传用户初始照片并调用百炼视觉模型进行多模态风格与色彩分析
     * @param file 用户上传的半身正脸照片
     * @return 包含肤色、色彩季型等信息的结构化 JSON 字符串
     */
    String analyzeUserStyle(MultipartFile file);
}
