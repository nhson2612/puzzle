package com.example.jigsawpuzzle.websocket.dto;

import com.example.jigsawpuzzle.domain.Position;

public record ReleasePieceRequest(Long pieceId, Long userId, Position position) { }
