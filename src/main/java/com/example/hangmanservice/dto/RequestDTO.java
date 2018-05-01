package com.example.hangmanservice.dto;


import lombok.Data;

@Data
public class RequestDTO {
    private String playerId;
    private String sessionId;
    private String action;
    private String guess;

    public RequestDTO() {
    }

    public RequestDTO(String playerId, String sessionId, String action) {
        this.playerId = playerId;
        this.sessionId = sessionId;
        this.action = action;
    }
}
