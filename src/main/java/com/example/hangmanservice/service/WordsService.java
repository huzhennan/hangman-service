package com.example.hangmanservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhennan.hu on 18-5-2
 */
@Service
//@Scope("singleton")
public class WordsService {
    private static final Logger log = LoggerFactory.getLogger(WordsService.class);

    private List<String> words;
    private List<String> lengthLessThan5Words = new LinkedList<>();
    private List<String> lengthLessThan8Words = new LinkedList<>();
    private List<String> lengthLessThan12Words = new LinkedList<>();
    private List<String> lengthGreaterThan12Words = new LinkedList<>();

    public WordsService() {
        log.debug("---------------------------------");
        log.debug("load english words from words.txt");
        log.debug("---------------------------------");
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource("words.txt").toURI());
            words = Files.readAllLines(path);

            words.stream()
                    .filter(s -> s.matches("^[a-zA-Z]+$"))
                    .map(String::toUpperCase).forEach(word -> {
                if (word.length() <= 5) {
                    lengthLessThan5Words.add(word);
                }

                if (word.length() <= 8) {
                    lengthLessThan8Words.add(word);
                }

                if (word.length() <= 12) {
                    lengthLessThan12Words.add(word);
                }

                if (word.length() > 12) {
                    lengthGreaterThan12Words.add(word);
                }
            });
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("import words fails");
        }
    }

    public String getRandomString() {
        Random random = new Random();
        int id = random.nextInt(words.size());
        return words.get(id);
    }

    public String getRandomStringWithLevel(WordDifficultyLevel level) {
        List<String> selectedWords = null;
        switch (level) {
            case ONE:
                selectedWords = lengthLessThan5Words;
                break;
            case TWO:
                selectedWords = lengthLessThan8Words;
                break;
            case THREE:
                selectedWords = lengthLessThan12Words;
                break;
            case FOUR:
                selectedWords = lengthGreaterThan12Words;
                break;
        }

        Random random = new Random();
        int id = random.nextInt(selectedWords.size());
        return selectedWords.get(id);
    }
}
