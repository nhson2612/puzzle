package com.example.jigsawpuzzle.config;

import com.example.jigsawpuzzle.scoring.ScoreCalculator;
import com.example.jigsawpuzzle.scoring.SimpleScoreCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfiguration {
    @Bean
    public ScoreCalculator scoreCalculator(){
        return new SimpleScoreCalculator();
    }

}