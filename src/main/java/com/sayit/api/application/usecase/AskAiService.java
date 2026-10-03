package com.sayit.api.application.usecase;

import com.sayit.api.application.domain.Prompt;
import com.sayit.api.application.ports.in.AskAiUseCase;
import com.sayit.api.application.ports.out.AiProviderPort;
import org.springframework.stereotype.Service;

@Service
public class AskAiService implements AskAiUseCase {

    private final AiProviderPort aiProviderPort;

    public AskAiService(AiProviderPort aiProviderPort) {
        this.aiProviderPort = aiProviderPort;
    }

    @Override
    public Prompt execute(Prompt prompt, String apiKey) {

        String aiResponse = aiProviderPort.generateText(prompt.getUserMessage(), apiKey);

        prompt.setAiResponse(aiResponse);

        return prompt;
    }
}