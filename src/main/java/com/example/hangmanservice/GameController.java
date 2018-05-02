package com.example.hangmanservice;

import com.example.hangmanservice.dto.ErrorDTO;
import com.example.hangmanservice.dto.RequestDTO;
import com.example.hangmanservice.dto.ResponseDTO;
import com.example.hangmanservice.exception.GameSessionException;
import com.example.hangmanservice.model.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    @Autowired
    private GameService gameService;

    @RequestMapping(path = "/game/on", method = RequestMethod.POST)
    public ResponseEntity game(@RequestBody RequestDTO requestDTO) {

        GameSession gameSession = gameService.createOrGetGameSession(requestDTO);
        if (gameSession == null) {
            ErrorDTO errorDTO = new ErrorDTO("game-session-not-found", "Game session not found");
            return ResponseEntity.status(404).body(errorDTO);
        }

        String action = requestDTO.getAction();
        switch (action) {
            case "startGame": {
                return ResponseEntity.ok(ResponseDTO.started(gameSession.getSessionId(),
                        GameSession.NUMBER_OF_WORDS_TO_GUESS,
                        GameSession.NUMBER_OF_GUESS_ALLOWED_FOR_EACH_WORD));
            }
            case "nextWord": {
                GameSession nextSession = null;
                try {
                      nextSession =gameService.nextWord(gameSession);
                } catch (GameSessionException e) {
                    ErrorDTO errorDTO = new ErrorDTO("game-session-operation-failed", e.getMessage());
                    return ResponseEntity.status(400).body(errorDTO);
                }
                return ResponseEntity.ok(ResponseDTO.word(nextSession));
            }
            case "guess": {
                String guessedLetter = requestDTO.getGuess();
                GameSession nextSession = null;
                try {
                     nextSession = gameService.guess(gameSession, guessedLetter);
                } catch (GameSessionException e) {
                    ErrorDTO error = new ErrorDTO("game-error-guess", e.getMessage());
                    return ResponseEntity.status(400).body(error);
                }
                return ResponseEntity.ok(ResponseDTO.word(nextSession));
            }
            case "getResult": {
                return ResponseEntity.ok(ResponseDTO.result(gameSession));
            }
        }


        return ResponseEntity.notFound().build();
    }
}
