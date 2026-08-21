package com.sayit.api.application.ports.in;

import com.sayit.api.application.domain.Prompt;

public interface AskAiUseCase{
    Prompt execute(Prompt prompt);
}
