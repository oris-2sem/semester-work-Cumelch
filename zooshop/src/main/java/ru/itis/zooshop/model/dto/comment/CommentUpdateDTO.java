package ru.itis.zooshop.model.dto.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentUpdateDTO {
    private Long id;
    private String message;
}
