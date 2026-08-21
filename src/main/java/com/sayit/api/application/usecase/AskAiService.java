package com.sayit.api.application.usecase;

import com.sayit.api.application.domain.Prompt;
import com.sayit.api.application.ports.in.AskAiUseCase;
import com.sayit.api.application.ports.out.AiProviderPort;

public class AskAiService implements AskAiUseCase {

    private final AiProviderPort aiProviderPort;

    public AskAiService(AiProviderPort aiProviderPort) {
        this.aiProviderPort = aiProviderPort;
    }

    @Override
    public Prompt execute(Prompt prompt) {
        String responseText = aiProviderPort.generateText(prompt.getUserMessage());
        prompt.setAiResponse(responseText);
        return prompt;
    }
}
