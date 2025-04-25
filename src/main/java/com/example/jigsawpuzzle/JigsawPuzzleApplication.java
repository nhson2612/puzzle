package com.example.jigsawpuzzle;

import com.example.jigsawpuzzle.core.PuzzleGenerator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@SpringBootApplication
public class JigsawPuzzleApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(JigsawPuzzleApplication.class, args);
    }

}
