package com.sayit.api.infrastructure.adapters.out.ai.gemini;

import com.sayit.api.application.ports.out.AiProviderPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class GeminiAdapter implements AiProviderPort {

    private final RestClient restClient = RestClient.create();

    @Override
    public String generateText(String userMessage, String apiKey) {

        String messageWithInstructions = userMessage + " [System note: Answer strictly in the exact same language the user used in the message above. Be extremely concise, direct, and informal. Use a maximum of two sentences.]";

        String url = "https://openrouter.ai/api/v1/chat/completions";

        // 🟢 Usando o roteador de modelos gratuitos oficial do OpenRouter
        Map<String, Object> requestBody = Map.of(
                "model", "openrouter/free",
                "messages", List.of(
                        Map.of("role", "user", "content", messageWithInstructions)
                )
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("HTTP-Referer", "https://sayit.app")
                    .header("X-Title", "SayIt Desktop")
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            return extractTextFromOpenRouter(response);

        } catch (RestClientResponseException e) {
            return "Aviso da IA: Problema na comunicação com o OpenRouter (Verifique sua chave).";
        } catch (Exception e) {
            return "Erro inesperado ao contactar a IA.";
        }
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromOpenRouter(Map<?, ?> response) {
        try {
            if (response == null || !response.containsKey("choices")) {
                return "Resposta vazia da IA.";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                return "Nenhuma resposta retornada pela IA.";
            }

            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            if (message == null || !message.containsKey("content")) {
                return "Conteúdo da resposta inválido.";
            }

            return (String) message.get("content");
        } catch (Exception e) {
            return "Erro ao processar a resposta da IA.";
        }
    }
}