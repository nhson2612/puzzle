package com.example.jigsawpuzzle.dto;

import com.google.protobuf.ByteString;
import java.util.List;

public class MaskRequest {
    private ByteString imageData;
    private List<Integer> mask;
    private int x;
    private int y;
    private int pieceSize;
    private int tabSize;

    public MaskRequest(ByteString imageData, List<Integer> mask, int x, int y, int pieceSize, int tabSize) {
        this.imageData = imageData;
        this.mask = mask;
        this.x = x;
        this.y = y;
        this.pieceSize = pieceSize;
        this.tabSize = tabSize;
    }

    // Getter và Setter
    public ByteString getImageData() {
        return imageData;
    }

    public void setImageData(ByteString imageData) {
        this.imageData = imageData;
    }

    public List<Integer> getMask() {
        return mask;
    }

    public void setMask(List<Integer> mask) {
        this.mask = mask;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPieceSize() {
        return pieceSize;
    }

    public void setPieceSize(int pieceSize) {
        this.pieceSize = pieceSize;
    }

    public int getTabSize() {
        return tabSize;
    }

    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }

    // Builder Pattern (nếu cần)
    public static class Builder {
        private ByteString imageData;
        private List<Integer> mask;
        private int x;
        private int y;
        private int pieceSize;
        private int tabSize;
        public Builder setImageData(ByteString imageData) {
            this.imageData = imageData;
            return this;
        }

        public Builder setMask(List<Integer> mask) {
            this.mask = mask;
            return this;
        }

        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setY(int y) {
            this.y = y;
            return this;
        }

        public Builder setPieceSize(int pieceSize) {
            this.pieceSize = pieceSize;
            return this;
        }

        public Builder setTabSize(int tabSize) {
            this.tabSize = tabSize;
            return this;
        }

        public MaskRequest build() {
            return new MaskRequest(imageData, mask, x, y,pieceSize,tabSize);
        }
    }
}
