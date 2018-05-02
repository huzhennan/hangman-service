package com.example.hangmanservice;

import com.example.hangmanservice.service.WordDifficultyLevel;
import com.example.hangmanservice.service.WordsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhennan.hu on 18-5-2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WordsServiceTest {
    @Autowired
    private WordsService wordsService;

    @Test
    public void testGetRandomString() {
        String word = wordsService.getRandomString();
        System.out.println("-----------------");
        System.out.println("word: " + word);
        System.out.println("-----------------");
    }

    @Test
    public void testGetRandomStringWithLevel() {
        String word = wordsService.getRandomStringWithLevel(WordDifficultyLevel.FOUR);
        System.out.println("-----------------");
        System.out.println("word: " + word);
        System.out.println("-----------------");
    }

}
