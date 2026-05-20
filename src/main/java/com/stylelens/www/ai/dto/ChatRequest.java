package com.stylelens.www.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatRequest implements Serializable {

    @NotBlank(message = "消息内容不能为空")
    private String message;

    private String systemPrompt;

    private String conversationId;
}
