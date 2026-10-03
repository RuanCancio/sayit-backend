package com.sayit.api.infrastructure.adapters.in.web;

import com.sayit.api.application.domain.Prompt;
import com.sayit.api.application.ports.in.AskAiUseCase;
import com.sayit.api.infrastructure.adapters.in.dto.PromptRequestDto;
import com.sayit.api.infrastructure.adapters.in.dto.PromptResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*") // 🟢 Libera o header X-User-API-Key no CORS
public class PromptController {

    private final AskAiUseCase askAiUseCase;

    public PromptController(AskAiUseCase askAiUseCase) {
        this.askAiUseCase = askAiUseCase;
    }

    @PostMapping("/prompt")
    public ResponseEntity<PromptResponseDto> ask(
            @RequestHeader(value = "X-User-API-Key", required = false) String apiKey,
            @RequestBody(required = false) PromptRequestDto dto) {

        // 1. Validação do Header sem estourar Erro 500
        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new PromptResponseDto("API Key do OpenRouter não fornecida no cabeçalho X-User-API-Key."));
        }

        // 2. Validação do DTO e Mensagem
        if (dto == null || dto.prompt() == null || dto.prompt().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new PromptResponseDto("A mensagem não pode ser vazia."));
        }

        try {
            Prompt prompt = new Prompt(dto.prompt());
            Prompt result = askAiUseCase.execute(prompt, apiKey);

            return ResponseEntity.ok(new PromptResponseDto(result.getAiResponse()));
        } catch (Exception e) {
            // 3. Captura qualquer falha inesperada e devolve resposta tratada
            return ResponseEntity.internalServerError()
                    .body(new PromptResponseDto("Erro ao processar requisição: " + e.getMessage()));
        }
    }
}