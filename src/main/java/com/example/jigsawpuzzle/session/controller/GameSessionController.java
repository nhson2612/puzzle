package com.example.jigsawpuzzle.session.controller;

import com.example.jigsawpuzzle.session.service.GameSessionService;
import com.example.jigsawpuzzle.session.dto.GameSessionReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-session")
public class GameSessionController {
    private final GameSessionService gameSessionService;

    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveSession(@RequestBody GameSessionReq sessionReq) {
        gameSessionService.saveSession(sessionReq);
        return ResponseEntity.ok("Game session saved successfully.");
    }

}