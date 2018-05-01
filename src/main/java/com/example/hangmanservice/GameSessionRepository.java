package com.example.hangmanservice;

import com.example.hangmanservice.model.GameSession;
import org.springframework.data.repository.CrudRepository;

public interface GameSessionRepository extends CrudRepository<GameSession, Long> {
    GameSession findBySessionId(String sessionId);
}
