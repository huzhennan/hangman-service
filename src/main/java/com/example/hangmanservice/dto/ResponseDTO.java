package com.example.hangmanservice.dto;

import com.example.hangmanservice.model.GameSession;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
        data.put("word", gameSession.getDisplayedWord());
        data.put("totalWordCount", gameSession.getCurrentWordId());
        data.put("wrongGuessCountOfCurrentWord", gameSession.getWrongGuessCountOfCurrentWord());

        return new ResponseDTO(gameSession.getSessionId(), data);
    }

    public static Object result(GameSession gameSession) {
        Map<String, Object> data = new HashMap<>();
        data.put("word", gameSession.getDisplayedWord());
        data.put("totalWordCount", gameSession.getCurrentWordId());
        data.put("wrongGuessCountOfCurrentWord", gameSession.getWrongGuessCountOfCurrentWord());
        data.put("score", gameSession.getScore());

        return new ResponseDTO(gameSession.getSessionId(), data);
    }

    public static Object done(GameSession gameSession) {
        Map<String, Object> data = new HashMap<>();
        data.put("playerId", gameSession.getPlayerId());
        data.put("sessionId", gameSession.getSessionId());
        data.put("totalWordCount", gameSession.getCurrentWordId());
        data.put("correctWordCount", gameSession.getCorrectWordCount());
        data.put("totalWrongGuessCount", gameSession.getWrongGuessCountOfCurrentWord());
        data.put("score", gameSession.getScore());
        data.put("datetime", gameSession.getSubmittedAt());

        return new ResponseDTO(gameSession.getSessionId(), data);
    }
}
