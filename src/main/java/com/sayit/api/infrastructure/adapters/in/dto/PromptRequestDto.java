package com.sayit.api.infrastructure.adapters.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PromptRequestDto(
        @JsonProperty("prompt") String prompt) {
}
