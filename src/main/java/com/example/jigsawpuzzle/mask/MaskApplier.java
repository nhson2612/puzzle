package com.example.jigsawpuzzle.mask;

import com.example.jigsawpuzzle.core.PuzzleConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import puzzle.MaskServiceGrpc;
import com.google.protobuf.ByteString;

import java.util.List;

public class MaskApplier {
    private final MaskServiceGrpc.MaskServiceBlockingStub blockingStub;

    public MaskApplier(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = MaskServiceGrpc.newBlockingStub(channel);
    }

    public byte[] applyMask(byte[] imageData, List<Integer> edges, int x, int y, PuzzleConfig puzzleConfig) {
        // Tạo request
        puzzle.MaskServiceOuterClass.MaskRequest request = puzzle.MaskServiceOuterClass.MaskRequest.newBuilder()
                .setImageData(ByteString.copyFrom(imageData))
                .addAllEdges(edges)
                .setX(x)
                .setY(y)
                .setPieceSize(puzzleConfig.PIECE_SIZE)
                .setTabSize(puzzleConfig.TAB_SIZE)
                .build();

        puzzle.MaskServiceOuterClass.MaskResponse response = blockingStub.applyMask(request);

        return response.getResultImageData().toByteArray();
    }
}