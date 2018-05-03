package com.example.hangmanservice.dto;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RequestDTO {
    @NotBlank(groups = {ValidationPlayId.class})
    @Email(groups = {ValidationPlayId.class})
    private String playerId;
    @NotBlank(groups = {ValidationSessionId.class, ValidationGuess.class})
    private String sessionId;
    @NotBlank
    @Pattern(regexp = "startGame|nextWord|guess|getResult|submitResult")
    private String action;
    @NotBlank(groups = {ValidationGuess.class})
    @Size(min = 1, max = 1)
    private String guess;

    public RequestDTO() {
    }

    public RequestDTO(String playerId, String sessionId, String action) {
        this.playerId = playerId;
        this.sessionId = sessionId;
        this.action = action;
    }

    public interface ValidationPlayId {
    }

    public interface ValidationSessionId {

    }

    public interface ValidationGuess {

    }
}
