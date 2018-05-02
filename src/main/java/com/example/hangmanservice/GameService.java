package com.example.hangmanservice;

import com.example.hangmanservice.dto.RequestDTO;
import com.example.hangmanservice.dto.ResponseDTO;
import com.example.hangmanservice.exception.GameSessionException;
import com.example.hangmanservice.model.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {
    @Autowired
    private GameSessionRepository sessionRepository;

    public GameSession guess(GameSession gameSession, String guessLetter) throws GameSessionException {
        gameSession.guess(guessLetter);
        sessionRepository.save(gameSession);
        return gameSession;
    }

    public GameSession nextWord(GameSession gameSession) throws GameSessionException {
        String word = "happy";
        gameSession.nextWord(word);
        sessionRepository.save(gameSession);
        return gameSession;
    }

    public GameSession createOrGetGameSession(RequestDTO requestDTO) {
        if (requestDTO.getAction().equals("startGame")) {
            Assert.notNull(requestDTO.getPlayerId(), "player id is null");
            GameSession gameSession = new GameSession(requestDTO.getPlayerId());
            sessionRepository.save(gameSession);
            return gameSession;
        } else {
            Assert.notNull(requestDTO.getSessionId(), "session id is null");
            return sessionRepository.findBySessionId(requestDTO.getSessionId());
        }
    }
}
