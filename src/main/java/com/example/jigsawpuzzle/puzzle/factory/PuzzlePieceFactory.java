package com.example.jigsawpuzzle.puzzle.factory;

import com.example.jigsawpuzzle.core.ImageResizer;
import com.example.jigsawpuzzle.domain.Position;
import com.example.jigsawpuzzle.domain.Puzzle;
import com.example.jigsawpuzzle.domain.PuzzlePiece;
import com.example.jigsawpuzzle.puzzle.repository.RedisPuzzlePieceRepository;
import com.example.jigsawpuzzle.puzzle.service.PuzzlePieceService;
import com.example.jigsawpuzzle.puzzle.service.PuzzleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class PuzzlePieceFactory {

    @Value("${puzzle.storage.path:./uploads/puzzles}")
    private String storagePath;
    private final RedisPuzzlePieceRepository puzzlePieceRepository;
    private final PuzzleService puzzleService;

    public PuzzlePieceFactory(RedisPuzzlePieceRepository puzzlePieceRepository, PuzzleService puzzleService) {
        this.puzzlePieceRepository = puzzlePieceRepository;
        this.puzzleService = puzzleService;
    }

    public List<PuzzlePiece> createInitialPieces(Long matchId) {
        List<PuzzlePiece> pieces = new ArrayList<>();
        Puzzle puzzle = puzzleService.getPuzzleByMatchId(matchId);
        String puzzleId = extractPuzzleId(puzzle.getOriginalImageUrl());

        if (puzzleId == null) {
            throw new IllegalArgumentException("No puzzle found for matchId: " + matchId);
        }
        File puzzleDir = new File(storagePath + "/" + puzzleId);
        File[] files = puzzleDir.listFiles((dir, name) -> name.startsWith("piece_") && name.endsWith(".png"));
        if (files == null || files.length == 0) {
            throw new RuntimeException("No pieces found for puzzleId: " + puzzleId);
        }
        int rows = puzzle.getRowCount();
        int cols = puzzle.getColCount();
        Integer edgeSize = ImageResizer.EDGE_SIZES.get(rows);
        if (edgeSize == null) {
            throw new IllegalArgumentException("Invalid row count for puzzleId: " + puzzleId);
        }
        int index = 0;
        for (File file : files) {
            String pieceUrl = "/puzzles/" + puzzleId + "/" + file.getName();
            int row = index / cols;
            int col = index % cols;
            Integer correctX = col * edgeSize;
            Integer correctY = row * edgeSize;

            PuzzlePiece piece = new PuzzlePiece();
            piece.setImageUrl(pieceUrl);
            piece.setCorrectPosition(new Position(correctX, correctY));
            piece.setCurrentPosition(new Position(-1, -1));
            piece.setIsPlacedCorrectly(false);
            piece.setHeldBy(null);
            pieces.add(piece);
            index++;
        }
        puzzlePieceRepository.saveAll(matchId,pieces);
        return pieces;
    }

    public List<PuzzlePiece> getCurrentPieces(Long matchId) {
        List<PuzzlePiece> pieces = new ArrayList<>();
        Puzzle puzzle = puzzleService.getPuzzleByMatchId(matchId);
        String puzzleId = extractPuzzleId(puzzle.getOriginalImageUrl());
        if (puzzleId == null) {
            throw new IllegalArgumentException("No puzzle found for matchId: " + matchId);
        }
        return puzzlePieceRepository.findAllByMatchId(matchId);
    }
    public static String extractPuzzleId(String url) {
        int startIdx = url.indexOf("/images/") + "/images/".length();
        int endIdx = url.indexOf(".png");
        if (startIdx != -1 && endIdx != -1) {
            return url.substring(startIdx, endIdx);
        } else {
            throw new IllegalArgumentException("URL không hợp lệ hoặc không chứa phần UUID.");
        }
    }
}

