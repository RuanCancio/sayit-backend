package com.sayit.api.application.domain;

public class Prompt {
    private final String userMessage;
    private String aiResponse;

    public Prompt(String userMessage) {
        if(userMessage == null || userMessage.isBlank()) {
            throw new IllegalArgumentException("The message cnnnot be blank");
        }
        this.userMessage = userMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }
}
