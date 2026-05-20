package com.stylelens.www.ai.controller;

import com.stylelens.www.ai.dto.ChatRequest;
import com.stylelens.www.ai.service.AiService;
import com.stylelens.www.common.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    public Result<String> chat(@Valid @RequestBody ChatRequest request) {
        String response;
        if (request.getSystemPrompt() != null) {
            response = aiService.chatWithSystem(request.getMessage(), request.getSystemPrompt());
        } else {
            response = aiService.chat(request.getMessage());
        }
        return Result.success(response);
    }

    @PostMapping("/chat/simple")
    public Result<String> simpleChat(@RequestParam String message) {
        return Result.success(aiService.chat(message));
    }

    @PostMapping("/image/generate")
    public Result<String> generateImage(@RequestParam String prompt) {
        return Result.success(aiService.generateImage(prompt));
    }
}
