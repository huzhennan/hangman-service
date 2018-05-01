package com.example.hangmanservice.dto;

import lombok.Data;

@Data
public class ErrorDTO {
    private String code;
    private String message;

    public ErrorDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
