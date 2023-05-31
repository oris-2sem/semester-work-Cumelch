package ru.itis.zooshop.service;

import ru.itis.zooshop.model.dto.comment.CommentCreationDTO;
import ru.itis.zooshop.model.dto.comment.CommentMessageDTO;
import ru.itis.zooshop.model.dto.comment.CommentUpdateDTO;
import ru.itis.zooshop.model.entity.CommentEntity;
import ru.itis.zooshop.model.entity.OfferEntity;
import ru.itis.zooshop.model.view.CommentDisplayView;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<List<CommentEntity>> findAllByOffer(OfferEntity offer);
    List<CommentDisplayView> findAllByUsername(String username);
    List<CommentDisplayView> getAllCommentsForOffer(Long offerId);
    CommentDisplayView map(CommentEntity comment);
    CommentDisplayView createComment(CommentCreationDTO commentCreationDto);
    CommentDisplayView updateComment(CommentUpdateDTO commentUpdateDTO);
    void deleteComment(Long id);
}
