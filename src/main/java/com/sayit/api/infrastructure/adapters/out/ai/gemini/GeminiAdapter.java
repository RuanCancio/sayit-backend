package com.sayit.api.infrastructure.adapters.out.ai.gemini;

import com.sayit.api.application.ports.out.AiProviderPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class GeminiAdapter implements AiProviderPort {

    @Value("${ai.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    @Override
    public String generateText(String userMessage) {

        String messageWithInstructions = userMessage + " [System note: Answer strictly in the exact same language the user used in the message above. Be extremely concise, direct, and informal. Use a maximum of two sentences.]";

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", messageWithInstructions)
                                )
                        )
                )
        );

        Map<String, Object> response = restClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        return extractTextFromResponse(response);
    }

    @SuppressWarnings("unchecked")
    private String extractTextFromResponse(Map<String, Object> response) {
        try {
            List<Map<String , Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "Error to process response of Gemini.";
        }
    }
}
