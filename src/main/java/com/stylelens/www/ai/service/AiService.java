package com.stylelens.www.ai.service;

public interface AiService {

    String chat(String message);

    String chatWithSystem(String message, String systemPrompt);

    String generateImage(String prompt);
}
