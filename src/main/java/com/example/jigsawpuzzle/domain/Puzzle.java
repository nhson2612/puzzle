package com.example.jigsawpuzzle.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "puzzles")
@Builder
@AllArgsConstructor
public class Puzzle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String originalImageUrl;
    private String title;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false, unique = true)
    private User createdBy;
    private LocalDateTime createdAt;
    @Min(1)
    @NotNull
    private Integer numberOfPieces;
    private Integer rowCount;  // Đổi tên từ "rows" thành "rowCount"
    private Integer colCount;  // Đổi tên từ "cols" thành "colCount"
    public Puzzle() {

    }
}