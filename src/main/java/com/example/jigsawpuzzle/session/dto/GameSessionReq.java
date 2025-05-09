package com.example.jigsawpuzzle.session.dto;

public record GameSessionReq(Long userId,Long roomId,Long matchId,Long puzzleId) { }