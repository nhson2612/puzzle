package com.example.jigsawpuzzle.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
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