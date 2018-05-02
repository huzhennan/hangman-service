package com.example.hangmanservice.service;

import com.example.hangmanservice.GameSessionRepository;
import com.example.hangmanservice.dto.RequestDTO;
import com.example.hangmanservice.exception.GameSessionException;
import com.example.hangmanservice.model.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class GameService {
    @Autowired
    private GameSessionRepository sessionRepository;
    @Autowired
    private WordsService wordsService;

    public GameSession guess(GameSession gameSession, String guessLetter) throws GameSessionException {
        gameSession.guess(guessLetter);
        sessionRepository.save(gameSession);
        return gameSession;
    }

    public GameSession nextWord(GameSession gameSession) throws GameSessionException {
        int currentWordId = gameSession.getCurrentWordId();
        WordDifficultyLevel level = null;
        if (currentWordId <= 20) {
            level = WordDifficultyLevel.ONE;
        } else if (currentWordId <= 40) {
            level = WordDifficultyLevel.TWO;
        } else if (currentWordId <= 60) {
            level = WordDifficultyLevel.THREE;
        } else {
            level = WordDifficultyLevel.FOUR;
        }
        String word = wordsService.getRandomStringWithLevel(level);

        gameSession.nextWord(word);
        sessionRepository.save(gameSession);
        return gameSession;
    }

    public GameSession submit(GameSession gameSession) {
        gameSession.submit();
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
