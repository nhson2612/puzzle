package com.example.jigsawpuzzle.domain;

public enum MatchStatus {
    NA, // chưa bắt đầu
    PROCESSING, // đang tải assets
    READY, // hoàn thành tải assets
    PLAYING, // đang chơi
    FINISHED // kết thúc
}