package com.example.hangmanservice;

import com.example.hangmanservice.dto.RequestDTO;
import com.example.hangmanservice.model.GameSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {
    @Autowired
    private GameService gameService;


    @Test
    public void testStartGame() {
        RequestDTO requestDTO = new RequestDTO("example@test.com", null, "startGame");
        GameSession gameSession = gameService.createOrGetGameSession(requestDTO);

        System.out.println("response: " + gameSession);
    }

    @Test
    public void testNextWord() {
        RequestDTO requestDTO = new RequestDTO("example@test.com", null, "startGame");
        GameSession gameSession = gameService.createOrGetGameSession(requestDTO);

        GameSession nextSession = gameService.nextWord(gameSession);
        System.out.println("----------------");
        System.out.println(nextSession);
        System.out.println("----------------");
    }
}
