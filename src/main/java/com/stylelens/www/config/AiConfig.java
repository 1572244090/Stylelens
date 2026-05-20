package com.stylelens.www.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.ai.dashscope")
public class AiConfig {

    private String apiKey;

    private String baseUrl;

    private String systemPrompt = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    private ChatConfig chat = new ChatConfig();

    private ImageConfig image = new ImageConfig();

    private RetryConfig retry = new RetryConfig();

    @Data
    public static class ChatConfig {
        private ChatOptions options = new ChatOptions();
        private Long timeout = 30000L;
    }

    @Data
    public static class ChatOptions {
        private String model = "qwen-turbo";
        private Double temperature = 0.7;
        private Double topP = 0.8;
        private Integer maxTokens = 2000;
    }

    @Data
    public static class ImageConfig {
        private ImageOptions options = new ImageOptions();
        private Long timeout = 60000L;
    }

    @Data
    public static class ImageOptions {
        private String model = "wanx-v1";
        private String size = "1024*1024";
    }

    @Data
    public static class RetryConfig {
        private Integer maxAttempts = 3;
        private BackoffConfig backoff = new BackoffConfig();
    }

    @Data
    public static class BackoffConfig {
        private Long initialInterval = 1000L;
        private Double multiplier = 2.0;
        private Long maxInterval = 5000L;
    }

    public DashScopeChatOptions toChatOptions() {
        return DashScopeChatOptions.builder()
                .withModel(chat.getOptions().getModel())
                .withTemperature(chat.getOptions().getTemperature())
                .withTopP(chat.getOptions().getTopP())
                .build();
    }
}
