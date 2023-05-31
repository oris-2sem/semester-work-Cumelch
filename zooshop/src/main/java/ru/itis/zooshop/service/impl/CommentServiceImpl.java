package ru.itis.zooshop.service.impl;

import org.springframework.stereotype.Service;
import ru.itis.zooshop.exceptions.OfferNotFoundException;
import ru.itis.zooshop.model.dto.comment.CommentCreationDTO;
import ru.itis.zooshop.model.dto.comment.CommentUpdateDTO;
import ru.itis.zooshop.model.entity.CommentEntity;
import ru.itis.zooshop.model.entity.OfferEntity;
import ru.itis.zooshop.model.entity.UserEntity;
import ru.itis.zooshop.model.view.CommentDisplayView;
import ru.itis.zooshop.repository.CommentRepository;
import ru.itis.zooshop.exceptions.UserNotFoundException;
import ru.itis.zooshop.exceptions.CommentNotFoundException;
import ru.itis.zooshop.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final OfferServiceImpl offerServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public CommentServiceImpl(CommentRepository commentRepository, OfferServiceImpl offerServiceImpl, UserServiceImpl userServiceImpl) {
        this.commentRepository = commentRepository;
        this.offerServiceImpl = offerServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }


    public Optional<List<CommentEntity>> findAllByOffer(OfferEntity offer) {
        return this.commentRepository.findAllByOffer(offer);
    }

    public List<CommentDisplayView> findAllByUsername(String username) {
        List<CommentEntity> comments =
                this.commentRepository.findAllByUsername(username)
                .orElseThrow(CommentNotFoundException::new);

        return comments.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public List<CommentDisplayView> getAllCommentsForOffer(Long offerId) {
        OfferEntity offer = offerServiceImpl
                .findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        List<CommentEntity> comments =
                this.findAllByOffer(offer)
                .orElseThrow(CommentNotFoundException::new);

        return comments.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public CommentDisplayView map(CommentEntity comment) {
        return new CommentDisplayView(
                comment.getId(),
                comment.getAuthor().getFullName(),
                comment.getText()
        );
    }

    public CommentDisplayView createComment(CommentCreationDTO commentCreationDto) {
        UserEntity author = userServiceImpl
                .findByUsername(commentCreationDto.getUsername())
                .orElseThrow(UserNotFoundException::new);

        OfferEntity offer = offerServiceImpl
                .findById(commentCreationDto.getOfferId())
                .orElseThrow(() -> new OfferNotFoundException(commentCreationDto.getOfferId()));

        CommentEntity commentEntity = new CommentEntity()
                .setCreated(LocalDateTime.now())
                .setText(commentCreationDto.getMessage())
                .setAuthor(author)
                .setOffer(offer);

        commentRepository.save(commentEntity);

        return this.map(commentEntity);
    }

    @Override
    public CommentDisplayView updateComment(CommentUpdateDTO commentUpdateDTO) {
        CommentEntity commentEntity = commentRepository.findById(commentUpdateDTO.getId()).orElseThrow(CommentNotFoundException::new);
        commentEntity.setText(commentUpdateDTO.getMessage());
        commentRepository.save(commentEntity);

        return this.map((commentEntity));
    }

    @Override
    public void deleteComment(Long id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(commentEntity);
    }
}
