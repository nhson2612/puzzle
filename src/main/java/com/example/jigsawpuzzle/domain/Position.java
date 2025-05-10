package com.example.jigsawpuzzle.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Position {
    private Integer x;
    private Integer y;

    public Position(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

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

    Double distanceTo(Position droppedPosition){
        double dx = droppedPosition.x - x;
        double dy = droppedPosition.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
