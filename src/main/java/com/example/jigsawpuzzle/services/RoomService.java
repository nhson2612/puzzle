package com.example.jigsawpuzzle.services;

import com.example.jigsawpuzzle.core.ImageResizer;
import com.example.jigsawpuzzle.domain.*;
import com.example.jigsawpuzzle.dto.RoomRequest;
import com.example.jigsawpuzzle.repositories.PuzzleRepository;
import com.example.jigsawpuzzle.repositories.RoomRepository;
import com.example.jigsawpuzzle.security.JwtAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final ImageService imageService;
    private final ImageResizer imageResizer;
    private final PuzzleService puzzleService;
    private final UserService userService;
    private final RoomRepository roomRepository;
    private final PuzzleRepository puzzleRepository;

    public RoomService(ImageService imageService, ImageResizer imageResizer, PuzzleService puzzleService, UserService userService, RoomRepository roomRepository, PuzzleRepository puzzleRepository) {
        this.imageService = imageService;
        this.imageResizer = imageResizer;
        this.puzzleService = puzzleService;
        this.userService = userService;
        this.roomRepository = roomRepository;
        this.puzzleRepository = puzzleRepository;
    }

    public Room createRoom(MultipartFile image, RoomRequest roomRequest, Authentication authentication) throws IOException {
        String imageId = roomRequest.imageId();
        byte[] resizedImage = null;
        List<String> puzzlePieceUrls = null;

        if (imageId != null) {
            byte[] imageBytes = imageService.getImageById(imageId);
            resizedImage = imageResizer.resizeImage(imageBytes, roomRequest.rows(), roomRequest.cols());
            puzzlePieceUrls = puzzleService.generatePuzzlePieces(resizedImage, null, roomRequest.rows(), roomRequest.cols());
        } else if (image != null) {
            resizedImage = imageResizer.resizeImage(image.getBytes(), roomRequest.rows(), roomRequest.cols());
            puzzlePieceUrls = puzzleService.generatePuzzlePieces(resizedImage, null, roomRequest.rows(), roomRequest.cols());
        }

        User host = userService.getUserById(Long.valueOf(authentication.getName()));
        assert puzzlePieceUrls != null;
        Puzzle puzzle = createPuzzle(puzzlePieceUrls.getFirst(), host, roomRequest);
        puzzle = puzzleRepository.save(puzzle);

        Match match = createInitialMatch(puzzle, roomRequest);
        Room room = Room.builder()
                .name(roomRequest.roomName())
                .host(host)
                .roomPlayers(new ArrayList<>())
                .matches(new ArrayList<>(List.of(match)))
                .currentMatch(match)
                .createdAt(LocalDateTime.now())
                .status(RoomStatus.WAITING)
                .maxPlayers(roomRequest.maxPlayers())
                .password(roomRequest.pass())
                .build();

        match.setRoom(room);
        return roomRepository.save(room);
    }

    private Match createInitialMatch(Puzzle puzzle, RoomRequest roomRequest) {
        return Match.builder()
                .startedAt(null)
                .endedAt(null)
                .players(new ArrayList<>())
                .puzzle(puzzle)
                .result(null)
                .mode(roomRequest.mode())
                .build();
    }

    private Puzzle createPuzzle(String puzzlePieceUrls, User host, RoomRequest roomRequest){
        return Puzzle.builder()
                .title(null)
                .originalImageUrl(puzzlePieceUrls.replaceAll("piece_\\d+\\.png", "original.png"))
                .createdBy(host)
                .createdAt(LocalDateTime.now())
                .numberOfPieces(roomRequest.cols() * roomRequest.rows())
                .rowCount(roomRequest.rows())
                .colCount(roomRequest.cols())
                .build();
    }

}
