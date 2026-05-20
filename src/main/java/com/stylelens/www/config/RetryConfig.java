package com.stylelens.www.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@RequiredArgsConstructor
public class RetryConfig {

    private final AiConfig aiConfig;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate template = new RetryTemplate();

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(aiConfig.getRetry().getBackoff().getInitialInterval());
        backOffPolicy.setMultiplier(aiConfig.getRetry().getBackoff().getMultiplier());
        backOffPolicy.setMaxInterval(aiConfig.getRetry().getBackoff().getMaxInterval());
        template.setBackOffPolicy(backOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(aiConfig.getRetry().getMaxAttempts());
        template.setRetryPolicy(retryPolicy);

        return template;
    }
}
