package com.example.hangmanservice.dto;

import com.example.hangmanservice.model.GameSession;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseDTO {
    private String message;
    private String sessionId;

    private Map<String, Object> data;

    public ResponseDTO(String message, String sessionId, Map<String, Object> data) {
        this.message = message;
        this.sessionId = sessionId;
        this.data = data;
    }

    public ResponseDTO(String sessionId, Map<String, Object> data) {
        this(null, sessionId, data);
    }

    public static ResponseDTO started(String sessionId, long numberOfWordsToGuess, long numberOfGuessAllowedForEachWord) {
        Map<String, Object> data = new HashMap<>();
        data.put("numberOfWordsToGuess", numberOfWordsToGuess);
        data.put("numberOfGuessAllowedForEachWord", numberOfGuessAllowedForEachWord);

        return new ResponseDTO("THE GAME IS ON", sessionId, data);
    }

    public static ResponseDTO word(GameSession gameSession) {
        Map<String, Object> data = new HashMap<>();
        data.put("word", gameSession.getCurrentWord());
        data.put("totalWordCount", gameSession.getCurrentWordId());
        data.put("wrongGuessCountOfCurrentWord", gameSession.getWrongGuessCountOfCurrentWord());

        return new ResponseDTO(gameSession.getSessionId(), data);
    }
}
