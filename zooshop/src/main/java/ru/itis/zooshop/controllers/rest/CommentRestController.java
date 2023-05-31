package ru.itis.zooshop.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.zooshop.exceptions.CommentNotFoundException;
import ru.itis.zooshop.exceptions.OfferNotFoundException;
import ru.itis.zooshop.exceptions.UserNotFoundException;
import ru.itis.zooshop.model.dto.comment.CommentCreationDTO;
import ru.itis.zooshop.model.dto.comment.CommentMessageDTO;
import ru.itis.zooshop.model.dto.comment.CommentUpdateDTO;
import ru.itis.zooshop.model.user.UserDetails;
import ru.itis.zooshop.model.view.CommentDisplayView;
import ru.itis.zooshop.model.view.OfferResponse;
import ru.itis.zooshop.service.CommentService;
import ru.itis.zooshop.service.impl.CommentServiceImpl;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentRestController {
    private final CommentService commentService;


    @Operation(summary = "Getting a list of comments by offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of comments by offer",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDisplayView.class))
                    })
    })
    @GetMapping("/{offerId}/comments")
    public ResponseEntity<List<CommentDisplayView>> getComments(@PathVariable("offerId") Long offerId) {
        return ResponseEntity.ok(commentService.getAllCommentsForOffer(offerId));
    }

    @Operation(summary = "Getting a list of comments by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of comments by username",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDisplayView.class))
                    })
    })
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDisplayView>> getCommentsByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(commentService.findAllByUsername(username));
    }

    @Operation(summary = "Creating comment by offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment successfully created.",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDisplayView.class))
                    })
    })
    @PostMapping(value = "/{offerId}/comments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentDisplayView> createComment(@PathVariable("offerId") Long offerId,
                                                            @AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody CommentMessageDTO commentDto) {
        CommentCreationDTO commentCreationDto =
                new CommentCreationDTO(
                userDetails.getUsername(),
                offerId,
                commentDto.getMessage()
        );

        CommentDisplayView comment = commentService.createComment(commentCreationDto);

        return ResponseEntity
                .created(URI.create(String.format("/api/%d/comments/%d", offerId, comment.getId())))
                .body(comment);
    }

    @Operation(summary = "Deleting comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comment successfully deleted.",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDisplayView.class))
                    })
    })

    @DeleteMapping(value = "/comments/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Updating comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comment successfully updated.",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDisplayView.class))
                    }),
    })
    @PutMapping("/comments/edit")
    public ResponseEntity<CommentDisplayView> updateComment(@RequestBody CommentUpdateDTO commentDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                commentService.updateComment(commentDto)
        );
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public String onUserNotFound(UserNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({CommentNotFoundException.class})
    public String onCommentNotFound(CommentNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({OfferNotFoundException.class})
    public String onOfferNotFound(OfferNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
}
