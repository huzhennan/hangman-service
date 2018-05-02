package com.example.hangmanservice.model;

import com.example.hangmanservice.exception.GameSessionException;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
public class GameSession {
    public static final long NUMBER_OF_WORDS_TO_GUESS = 80;
    public static final long NUMBER_OF_GUESS_ALLOWED_FOR_EACH_WORD = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String playerId;
    private String sessionId;


    // current word state
    private int currentWordId = 0;
    private String currentWord;
    private int guessCountOfCurrentWord = 0;
    private int wrongGuessCountOfCurrentWord = 0;
    private CurrentWordState state = CurrentWordState.INITIALIZED;
    private String guessedLetters = "";

    private int score = 0;

    public GameSession() {
    }

    public GameSession(String playerId) {
        this.playerId = playerId;
        this.sessionId = UUID.randomUUID().toString();
    }

    public GameSession nextWord(String word) throws GameSessionException {
        if (currentWordId >= NUMBER_OF_WORDS_TO_GUESS - 1) {
            throw new GameSessionException("GAME ERROR: haved guessed 80 words");
        }

        currentWordId++;
        currentWord = word;
        guessCountOfCurrentWord = 0;
        wrongGuessCountOfCurrentWord = 0;
        state = CurrentWordState.STARTED;
        guessedLetters = "";

        return this;
    }

    public GameSession guess(String letter) throws GameSessionException {
        if (state == CurrentWordState.INITIALIZED || state == CurrentWordState.END) {
            throw new GameSessionException("GAME ERROR: need a word to start");
        }

        if (guessCountOfCurrentWord > NUMBER_OF_GUESS_ALLOWED_FOR_EACH_WORD) {
            throw new GameSessionException("GAME ERROR: had guess 10 times, can't guess again");
        }

        guessCountOfCurrentWord++;
        guessedLetters += letter;

        if (!currentWord.contains(letter)) {
            wrongGuessCountOfCurrentWord++;
        }

        updateState();
        return this;
    }

    private void updateState() {
        if (state == CurrentWordState.STARTED) {
            Set<Character> guessedLettersSet = guessedLetters.codePoints()
                    .mapToObj(i -> (char) i).collect(Collectors.toSet());
            Set<Character> currentWordLettersSet = currentWord.codePoints()
                    .mapToObj(i -> (char) i).collect(Collectors.toSet());
            if (guessedLettersSet.containsAll(currentWordLettersSet)) {
                this.state = CurrentWordState.END;

                score += 20 - wrongGuessCountOfCurrentWord;
            }
        }
    }

    public String getDisplayedWord() {
        Set<Character> guessedLettersSet = guessedLetters.codePoints()
                .mapToObj(i -> (char)i).collect(Collectors.toSet());

        final StringBuilder result = new StringBuilder();
        currentWord.codePoints().mapToObj(i -> (char)i).map(character -> {
            if (guessedLettersSet.contains(character)) {
                return character;
            } else {
                return '*';
            }
        }).forEach(result::append);

        return result.toString();
    }
}
