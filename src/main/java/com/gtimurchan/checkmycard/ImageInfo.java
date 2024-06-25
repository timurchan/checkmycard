package com.gtimurchan.checkmycard;

import java.util.Objects;

public class ImageInfo {
    private String imageUrl;

    public ImageInfo(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageInfo)) return false;
        ImageInfo imageInfo = (ImageInfo) o;
        return Objects.equals(imageUrl, imageInfo.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}
