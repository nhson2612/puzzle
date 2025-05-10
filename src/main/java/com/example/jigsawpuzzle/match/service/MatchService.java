package com.example.jigsawpuzzle.match.service;

import com.example.jigsawpuzzle.domain.Match;
import com.example.jigsawpuzzle.domain.MatchStatus;
import com.example.jigsawpuzzle.domain.Puzzle;
import com.example.jigsawpuzzle.room.dto.RoomRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class MatchService {
    public Match createInitialMatch(Puzzle puzzle, RoomRequest request) {
        return Match.builder()
                .startedAt(null)
                .endedAt(null)
                .players(new ArrayList<>())
                .puzzle(puzzle)
                .result(null)
                .mode(request.mode())
                .status(MatchStatus.NA)
                .build();
    }
}
