package ru.itis.zooshop.model.dto.image;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ImageDTO {
    private String publicId;
    private String imageUrl;

    public ImageDTO setPublicId(String publicId) {
        this.publicId = publicId;
        return this;
    }

    public ImageDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
