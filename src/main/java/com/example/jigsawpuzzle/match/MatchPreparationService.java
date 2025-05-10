package com.example.jigsawpuzzle.match;

import com.example.jigsawpuzzle.core.ImageResizer;
import com.example.jigsawpuzzle.domain.Match;
import com.example.jigsawpuzzle.domain.MatchStatus;
import com.example.jigsawpuzzle.room.dto.RoomRequest;
import com.example.jigsawpuzzle.puzzle.service.PuzzleService;
import com.example.jigsawpuzzle.core.ImageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchPreparationService {
    private final ImageResizer imageResizer;
    private final PuzzleService puzzleService;
    private final ImageService imageService;
    private final MatchRepository matchRepository;

    public MatchPreparationService(ImageResizer imageResizer, PuzzleService puzzleService, ImageService imageService, MatchRepository matchRepository) {
        this.imageResizer = imageResizer;
        this.puzzleService = puzzleService;
        this.imageService = imageService;
        this.matchRepository = matchRepository;
    }

    @Async
    public void prepareMatchAssets(Match match, RoomRequest request) {
        try {
            if (match.getStatus() == MatchStatus.NA) {
                match.setStatus(MatchStatus.PROCESSING);
                matchRepository.save(match);
                byte[] imageBytes = imageService.getImageById(request.imageId());
                byte[] resized = imageResizer.resizeImage(imageBytes, request.rows(), request.cols());
                List<String> pieces = puzzleService.generatePuzzlePieces(resized, null, request.rows(), request.cols());
                match.setStatus(MatchStatus.READY);
                matchRepository.save(match);
            }
            else if (match.getStatus() == MatchStatus.READY || match.getStatus() == MatchStatus.PLAYING) {
                // Nếu trạng thái đã READY hoặc đang PLAYING, không làm gì cả
                return;
            }
            else if (match.getStatus() == MatchStatus.PROCESSING) {
                // Trạng thái là PROCESSING: đang xử lý tài nguyên, bạn có thể giữ trạng thái này hoặc gửi thông báo cho client
                // Một cách đơn giản có thể là kiểm tra xem tài nguyên có đang được xử lý không, rồi tiếp tục thông báo cho client.
                // Ví dụ, bạn có thể gửi thông báo qua WebSocket về tiến trình.
                return;
            }

        } catch (Exception e) {
            match.setStatus(MatchStatus.FINISHED);  // Nếu có lỗi, đánh dấu trạng thái là FINISHED
            matchRepository.save(match);
            throw new RuntimeException("Failed to prepare match", e);
        }
    }

}

