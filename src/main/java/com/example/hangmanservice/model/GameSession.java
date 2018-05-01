package com.example.hangmanservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String playerId;
    private String sessionId;
    private int currentWordId = 0;
    private String currentWord;
    private int wrongGuessCountOfCurrentWord = 0;
    private int guessCountOfCurrentWord = 0;

//    private Set<String> guessedLetter = new HashSet<>();
    private String guessedLetters = "";
    private GameSessionState state = GameSessionState.STARTED;

    public GameSession() {
    }

    public GameSession(String playerId) {
        this.playerId = playerId;
        this.sessionId = UUID.randomUUID().toString();
    }

    public GameSession nextWord(String word) {
        this.currentWord = word;
        this.currentWordId++;
        return this;
    }

    public GameSession guess(String letter) {
        guessCountOfCurrentWord++;
        guessedLetters += letter;

        if (currentWord.contains(letter)) {
            wrongGuessCountOfCurrentWord++;
        }

        return this;
    }
}
