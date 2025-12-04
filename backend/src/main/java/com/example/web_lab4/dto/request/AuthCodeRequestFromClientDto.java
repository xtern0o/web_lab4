package com.example.web_lab4.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthCodeRequestFromClientDto {
    @NotBlank(message = "Auth Code is required")
    private String code;

    @NotBlank(message = "redirect URI is required")
    private String redirectUri;

}
