package com.example.jigsawpuzzle.scoring;


import com.example.jigsawpuzzle.domain.PlayerState;
import com.example.jigsawpuzzle.domain.PuzzlePiece;

import java.util.List;

public interface ScoreCalculator {
    PlayerState calculateScore(PuzzlePiece puzzlePiece, List<PuzzlePiece> pieces,Long userId);
}