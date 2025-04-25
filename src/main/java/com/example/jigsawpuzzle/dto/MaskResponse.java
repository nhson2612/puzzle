package com.example.jigsawpuzzle.dto;

import com.google.protobuf.ByteString;

public class MaskResponse {
    private ByteString imageData;
    private String status;

    public MaskResponse(ByteString imageData, String status) {
        this.imageData = imageData;
        this.status = status;
    }
    public ByteString getImageData() {
        return imageData;
    }

    public void setImageData(ByteString imageData) {
        this.imageData = imageData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public static class Builder {
        private ByteString imageData;
        private String status;

        public Builder setImageData(ByteString imageData) {
            this.imageData = imageData;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public MaskResponse build() {
            return new MaskResponse(imageData, status);
        }
    }
}

