package com.example.jigsawpuzzle.room.service;

import com.example.jigsawpuzzle.auth.service.UserService;
import com.example.jigsawpuzzle.core.ImageService;
import com.example.jigsawpuzzle.domain.Match;
import com.example.jigsawpuzzle.domain.Puzzle;
import com.example.jigsawpuzzle.domain.Room;
import com.example.jigsawpuzzle.domain.User;
import com.example.jigsawpuzzle.match.service.MatchService;
import com.example.jigsawpuzzle.puzzle.service.PuzzleService;
import com.example.jigsawpuzzle.room.dto.RoomRequest;
import com.example.jigsawpuzzle.room.dto.RoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RoomCreationFacade {

    private final ImageService imageService;
    private final PuzzleService puzzleService;
    private final MatchService matchService;
    private final RoomService roomService;
    private final UserService userService;

    public RoomResponse createRoom(MultipartFile image, RoomRequest roomRequest, Authentication authentication) throws IOException, IOException {
        Long userId = Long.valueOf(authentication.getName());
        User host = userService.getUserById(userId);

        String imageUrl = imageService.resolvePuzzleImageUrl(image, roomRequest.imageId());
        Puzzle puzzle = puzzleService.createAndSaveInitialPuzzle(imageUrl, host, roomRequest);
        Match match = matchService.createInitialMatch(puzzle, roomRequest);
        Room room = roomService.createRoom(host, match, roomRequest);

        return new RoomResponse(room);
    }
}

