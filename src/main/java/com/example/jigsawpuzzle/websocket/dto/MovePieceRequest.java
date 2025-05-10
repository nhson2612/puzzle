package com.example.jigsawpuzzle.websocket.dto;

import com.example.jigsawpuzzle.domain.Position;

public record MovePieceRequest(Long pieceId,Position newPosition,Long userId){}