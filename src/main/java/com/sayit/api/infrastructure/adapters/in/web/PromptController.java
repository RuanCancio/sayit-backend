package com.sayit.api.infrastructure.adapters.in.web;

import com.sayit.api.application.domain.Prompt;
import com.sayit.api.application.ports.in.AskAiUseCase;
import com.sayit.api.infrastructure.adapters.in.dto.PromptRequestDto;
import com.sayit.api.infrastructure.adapters.in.dto.PromptResponseDto;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class PromptController {

    private final AskAiUseCase askAiUseCase;

    public PromptController( AskAiUseCase askAiUseCase) {
        this.askAiUseCase = askAiUseCase;
    }

    @PostMapping("/prompt")
    public PromptResponseDto ask(@RequestBody PromptRequestDto dto) {
        Prompt prompt = new Prompt(dto.prompt());
        Prompt result = askAiUseCase.execute(prompt);
        return new PromptResponseDto(result.getAiResponse());
    }

}
