package com.sayit.api.infrastructure.config;

import com.sayit.api.application.ports.in.AskAiUseCase;
import com.sayit.api.application.ports.out.AiProviderPort;
import com.sayit.api.application.usecase.AskAiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class BeanConfiguration {

    @Bean
    public AskAiUseCase askAiUseCase(AiProviderPort aiProviderPort) {
        return new AskAiService(aiProviderPort);
    }
}
