package com.example.web_lab4.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthCodeRequestDto {
    @NotBlank(message = "code is required")
    private String code;

    @NotBlank(message = "redirect_uri is required")
    @JsonProperty("redirect_uri")
    private String redirectUri;

}
