package com.example.jigsawpuzzle.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class Position {
    private Integer x;
    private Integer y;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return Objects.equals(x, position.x) && Objects.equals(y, position.y);
    }
}
