package com.example.jigsawpuzzle.room.service;

import com.example.jigsawpuzzle.domain.*;
import com.example.jigsawpuzzle.match.MatchRepository;
import com.example.jigsawpuzzle.match.factory.MatchStarterStrategyFactory;
import com.example.jigsawpuzzle.match.strategy.MatchStarterStrategy;
import com.example.jigsawpuzzle.room.dto.RoomRequest;
import com.example.jigsawpuzzle.match.MatchPreparationService;
import com.example.jigsawpuzzle.room.dto.RoomResponse;
import com.example.jigsawpuzzle.room.repository.projection.RoomWithoutPassword;
import com.example.jigsawpuzzle.auth.service.UserService;
import com.example.jigsawpuzzle.room.repository.RoomPlayerRepository;
import com.example.jigsawpuzzle.room.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final UserService userService;
    private final RoomRepository roomRepository;
    private final RoomPlayerRepository roomPlayerRepository;
    private final MatchPreparationService matchPreparationService;
    private final MatchRepository matchRepository;
    private final MatchStarterStrategyFactory strategyFactory;


    public RoomService(UserService userService, RoomRepository roomRepository, RoomPlayerRepository roomPlayerRepository, MatchPreparationService matchPreparationService, MatchRepository matchRepository, MatchStarterStrategyFactory strategyFactory) {
        this.userService = userService;
        this.roomRepository = roomRepository;
        this.roomPlayerRepository = roomPlayerRepository;
        this.matchPreparationService = matchPreparationService;
        this.matchRepository = matchRepository;
        this.strategyFactory = strategyFactory;
    }

    @Transactional(readOnly = true)
    public List<RoomWithoutPassword> getAllRooms() {
        List<RoomWithoutPassword> allRooms = roomRepository.findAllRoomsProjectedBy();
        return allRooms;
    }
    public Room createRoom(User host, Match match, RoomRequest request) {
        Room room = Room.builder()
                .name(request.roomName())
                .host(host)
                .matches(List.of(match))
                .currentMatch(match)
                .createdAt(LocalDateTime.now())
                .status(RoomStatus.WAITING)
                .maxPlayers(request.maxPlayers())
                .password(request.pass())
                .joinable(true)
                .build();

        RoomPlayer roomPlayer = RoomPlayer.builder()
                .room(room)
                .user(host)
                .isReady(false)
                .build();

        room.addPlayer(roomPlayer);
        match.setRoom(room);
        return roomRepository.save(room);
    }

    public RoomResponse enterRoom(Long roomId, String inputPassword, Authentication authentication) {
        Long userId = extractUserId(authentication);
        ensureUserNotInAnyRoom(userId);
        Room room = getFullRoomByIdOrThrow(roomId);
        if (room.getPassword() != null && !room.getPassword().isBlank()) {
            if (inputPassword == null || !room.getPassword().equals(inputPassword)) {
                throw new IllegalArgumentException("Incorrect room password");
            }
        }
        User user = userService.getUserById(userId);
        RoomPlayer roomPlayer = createRoomPlayer(user, room);
        room.addPlayer(roomPlayer);
        roomRepository.save(room);
        return new RoomResponse(room);
    }
    public void startRoom(Long roomId, RoomRequest roomRequest) throws IOException {
        Room room = roomRepository.findByIdWithCurrentMatchAndPlayers(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + roomId));
        Match currentMatch = room.getCurrentMatch();

        if (currentMatch == null) {
            throw new IllegalStateException("No current match is assigned to this room.");
        }
        List<User> users = room.getRoomPlayers().stream()
                .map(RoomPlayer::getUser)
                .collect(Collectors.toList());
        currentMatch.setPlayers(users);
        if (currentMatch.getStatus() == MatchStatus.PLAYING || currentMatch.getStatus() == MatchStatus.READY) {
            matchRepository.save(currentMatch);
            return;
        }
        MatchMode mode = currentMatch.getMode();
        MatchStarterStrategy strategy = strategyFactory.getStrategy(mode);
        strategy.validate(room, roomRequest);

        if (roomRequest.duration() == null || roomRequest.duration() <= 0) {
            throw new IllegalArgumentException("Invalid match duration.");
        }
        currentMatch.setStartedAt(LocalDateTime.now());
        currentMatch.setEndedAt(LocalDateTime.now().plus(Duration.ofSeconds(roomRequest.duration())));
        room.setStatus(RoomStatus.PLAYING);
        matchPreparationService.prepareMatchAssets(currentMatch, roomRequest);
    }
    public void exitRoom(Long roomId, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        ensureUserNotInActiveMatch(userId);
        Room room = roomRepository.findByIdWithPlayers(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + roomId));
        RoomPlayer player = room.getRoomPlayers().stream()
                .filter(p -> p.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User is not in the room"));
        room.removePlayer(player);
        roomPlayerRepository.delete(player);
        if (room.getHost().getId().equals(userId)) {
            if (room.getRoomPlayers().isEmpty()) {
                roomRepository.delete(room);
                return;
            } else {
                room.setHost(room.getRoomPlayers().get(0).getUser());
            }
        }

        roomRepository.save(room);
    }
    public void ready(Long roomId, Authentication authentication) {
        Room room = roomRepository.findByIdWithPlayers(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with ID: " + roomId));
        Long userId = Long.valueOf(authentication.getName());
        RoomPlayer targetPlayer = room.getRoomPlayers().stream()
                .filter(p -> p.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User is not a player in this room."));
        targetPlayer.setIsReady(true);
        roomPlayerRepository.save(targetPlayer);
    }

    private Long extractUserId(Authentication auth) {
        return Long.valueOf(auth.getName());
    }

    private void ensureUserNotInAnyRoom(Long userId) {
        roomPlayerRepository.findByUser_Id(userId).ifPresent(player -> {
            throw new IllegalStateException("User is already in another room");
        });
    }
    private void ensureUserNotInActiveMatch(Long userId){
        List<Long> activeMatches = matchRepository.findActiveMatchesByUserId(userId);
        if (!activeMatches.isEmpty()) {
            throw new IllegalStateException("User is already in an active match. Active matches: " + activeMatches);
        }
    }

    private Room getFullRoomByIdOrThrow(Long roomId) {
        return roomRepository.findFullRoomById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
    }
    private RoomPlayer createRoomPlayer(User user, Room room) {
        return RoomPlayer.builder()
                .user(user)
                .room(room)
                .isReady(false)
                .build();
    }
}