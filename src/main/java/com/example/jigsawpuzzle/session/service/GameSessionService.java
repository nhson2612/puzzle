package com.example.jigsawpuzzle.session.service;

import com.example.jigsawpuzzle.auth.repository.UserRepository;
import com.example.jigsawpuzzle.domain.*;
import com.example.jigsawpuzzle.match.MatchRepository;
import com.example.jigsawpuzzle.room.repository.RoomRepository;
import com.example.jigsawpuzzle.session.repository.GameSessionRepository;
import com.example.jigsawpuzzle.session.dto.GameSessionReq;
import org.springframework.stereotype.Service;

@Service
public class GameSessionService {
    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MatchRepository matchRepository;

    public GameSessionService(GameSessionRepository gameSessionRepository,
                              UserRepository userRepository,
                              RoomRepository roomRepository,
                              MatchRepository matchRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.matchRepository = matchRepository;
    }

    public void saveSession(GameSessionReq sessionReq) {
        User user = userRepository.findById(sessionReq.userId()).orElseThrow(() -> new RuntimeException("User not found"));
        Room room = roomRepository.findById(sessionReq.roomId()).orElseThrow(() -> new RuntimeException("Room not found"));
        Match match = matchRepository.findById(sessionReq.matchId()).orElseThrow(() -> new RuntimeException("Match not found"));
        Puzzle puzzle = match.getPuzzle();

        GameSession gameSession = new GameSession();
        gameSession.setUser(user);
        gameSession.setRoom(room);
        gameSession.setMatch(match);
        gameSession.setPuzzle(puzzle);
        gameSessionRepository.save(gameSession);
    }
}
