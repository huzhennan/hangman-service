package com.example.hangmanservice;

import com.example.hangmanservice.dto.ErrorDTO;
import com.example.hangmanservice.dto.RequestDTO;
import com.example.hangmanservice.dto.ResponseDTO;
import com.example.hangmanservice.exception.GameSessionException;
import com.example.hangmanservice.model.GameSession;
import com.example.hangmanservice.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

@RestController
public class GameController {
    @Autowired
    private GameService gameService;

    @RequestMapping(path = "/game/on", method = RequestMethod.POST)
    public ResponseEntity game(@RequestBody RequestDTO requestDTO) {
        ConstraintViolation<RequestDTO> item = validateRequest(requestDTO);
        if (item != null) {
            String message = String.format("%s %s", item.getPropertyPath().toString(), item.getMessage());
            ErrorDTO errorDTO = new ErrorDTO("request-validate-fail", message);
            return ResponseEntity.status(400).body(errorDTO);
        }

        GameSession gameSession = gameService.createOrGetGameSession(requestDTO);
        if (gameSession == null) {
            ErrorDTO errorDTO = new ErrorDTO("game-session-not-found", "Game session not found");
            return ResponseEntity.status(404).body(errorDTO);
        }

        if (gameSession.isHasSubmitted()) {
            ErrorDTO errorDTO = new ErrorDTO("game-session-has-submitted", "this session has submitted.");
            return ResponseEntity.status(400).body(errorDTO);
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
                    nextSession = gameService.nextWord(gameSession);
                } catch (GameSessionException e) {
                    ErrorDTO errorDTO = new ErrorDTO("game-session-operation-failed", e.getMessage());
                    return ResponseEntity.status(400).body(errorDTO);
                }
                return ResponseEntity.ok(ResponseDTO.word(nextSession));
            }
            case "guessWord": {
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
            case "submitResult": {
                gameService.submit(gameSession);
                return ResponseEntity.ok(ResponseDTO.done(gameSession));
            }
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO("action-not-found", "action not found."));
    }

    private ConstraintViolation<RequestDTO> validateRequest(RequestDTO requestDTO) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<RequestDTO>> defaultConstraintViolations =
                validator.validate(requestDTO, Default.class);
        if (!defaultConstraintViolations.isEmpty()) {
            return defaultConstraintViolations.iterator().next();
        }

        String action = requestDTO.getAction();
        switch (action) {
            case "startGame": {
                Set<ConstraintViolation<RequestDTO>> constraintViolations1 = validator.validate(requestDTO, RequestDTO.ValidationPlayId.class);
                if (!constraintViolations1.isEmpty()) {
                    return constraintViolations1.iterator().next();
                }
                break;
            }
            case "nextWord":
            case "getResult":
            case "submitResult": {
                Set<ConstraintViolation<RequestDTO>> constraintViolations2 = validator.validate(requestDTO, RequestDTO.ValidationSessionId.class);
                if (!constraintViolations2.isEmpty()) {
                    return constraintViolations2.iterator().next();
                }
                break;
            }
            case "guess": {
                Set<ConstraintViolation<RequestDTO>> constraintViolations3 = validator.validate(requestDTO, RequestDTO.ValidationGuess.class);
                if (!constraintViolations3.isEmpty()) {
                    return constraintViolations3.iterator().next();
                }
                break;
            }
        }
        return null;
    }
}
