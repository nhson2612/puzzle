package com.example.jigsawpuzzle.puzzle.controller;

import com.example.jigsawpuzzle.core.ImageResizer;
import com.example.jigsawpuzzle.core.ImageService;
import com.example.jigsawpuzzle.puzzle.service.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puzzles")
public class PuzzleController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private PuzzleService puzzleService;
    private final ImageResizer imageResizer;

    public PuzzleController(ImageResizer imageResizer) {
        this.imageResizer = imageResizer;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("rows") int rows,
            @RequestParam("cols") int cols  ) {
        try {
            String fileName = file.getOriginalFilename();
            String imageUrl = imageService.saveImage(file.getBytes(),fileName);
            byte[] resizeImage = imageResizer.resizeImage(file.getBytes(), rows, cols);
            List<String> puzzlePieceUrls = puzzleService.generatePuzzlePieces(resizeImage,imageUrl.substring(imageUrl.lastIndexOf('/') + 1),rows,cols);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã tạo puzzle thành công");
            response.put("originalImage", imageUrl);
            response.put("puzzlePieces", puzzlePieceUrls);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Không thể tạo puzzle: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/pieces/{imageId}")
    public ResponseEntity<List<String>> getPuzzlePieces(@PathVariable String imageId) {
        try {
            List<String> pieceUrls = puzzleService.getPuzzlePiecesByImageId(imageId);
            return ResponseEntity.ok(pieceUrls);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/preview/{imageId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getPreviewImage(@PathVariable String imageId) {
        try {
            byte[] imageBytes = puzzleService.getPreviewImage(imageId);
            return ResponseEntity.ok(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}