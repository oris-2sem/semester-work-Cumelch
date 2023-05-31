package ru.itis.zooshop.model.dto.comment;

import lombok.*;
import ru.itis.zooshop.model.entity.CommentEntity;

@Builder
@AllArgsConstructor
@Data
public class CommentCreationDTO {
    private String username;
    private Long offerId;
    private String message;

    public static CommentCreationDTO from(CommentEntity comment) {
        return CommentCreationDTO.builder()
                .username(comment.getAuthor().getUsername())
                .offerId(comment.getOffer().getId())
                .message(comment.getText())
                .build();
    }
}