package com.example.jigsawpuzzle.dto;

import com.example.jigsawpuzzle.domain.MatchMode;

public record RoomRequest(String roomName, Integer maxPlayers, String pass, String imageId,Integer duration, MatchMode mode,Integer rows,Integer cols) { }