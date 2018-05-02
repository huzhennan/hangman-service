package com.example.hangmanservice;

import com.example.hangmanservice.exception.GameSessionException;
import com.example.hangmanservice.model.GameSession;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhennan.hu on 18-5-2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameSessionTest {

    @Test
    public void testGetDisplayedWord() throws GameSessionException {
        GameSession gameSession = new GameSession("example@domain.com");
        gameSession.nextWord("hello");

        gameSession.guess("l");

        Assert.assertEquals(gameSession.getDisplayedWord(), "**ll*");
    }

    @Test(expected = GameSessionException.class)
    public void testGuessWithoutNextWord() throws GameSessionException {
        GameSession gameSession = new GameSession("example@domain.com");
        gameSession.guess("l");
    }

    @Test
    public void testGetScore() {
        GameSession gameSession = new GameSession("example@domain.com");
        gameSession.nextWord("hello");

        try {
            gameSession.guess("h");
            gameSession.guess("e");
            gameSession.guess("l");
            gameSession.guess("o");

        } catch (GameSessionException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(gameSession.getScore(), 20);
    }

    @Test
    public void testGetScoreWithWrongGuess() {
        GameSession gameSession = new GameSession("example@domain.com");
        gameSession.nextWord("hello");

        try {
            gameSession.guess("h");
            gameSession.guess("e");
            gameSession.guess("a");
            gameSession.guess("l");
            gameSession.guess("o");
        } catch (GameSessionException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(gameSession.getScore(), 19);
    }
}
