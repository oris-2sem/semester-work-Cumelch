package ru.itis.zooshop.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.zooshop.exceptions.CategoryNotFoundException;
import ru.itis.zooshop.exceptions.OfferNotFoundException;
import ru.itis.zooshop.exceptions.UserNotFoundException;
import ru.itis.zooshop.model.view.OfferResponse;
import ru.itis.zooshop.model.view.UserResponse;
import ru.itis.zooshop.service.OfferService;
import ru.itis.zooshop.service.UserService;
import ru.itis.zooshop.service.impl.OfferServiceImpl;
import ru.itis.zooshop.service.impl.UserServiceImpl;
import ru.itis.zooshop.util.RestPaginationConstants;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminRestController {
    private final OfferService offerService;
    private final UserService userService;

    @Operation(summary = "Getting a list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
                    })
    })
    @GetMapping("/users")
    public UserResponse getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = RestPaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestPaginationConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestPaginationConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestPaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir

    ) {
        return userService.getAllUsersAdminRest(pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(summary = "Getting a list of offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of offers",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OfferResponse.class))
                    })
    })
    @GetMapping("/offers")
    public OfferResponse getAllOffers(
            @RequestParam(value = "pageNo", defaultValue = RestPaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestPaginationConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestPaginationConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestPaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir

    ) {
        return offerService.getAllOffersAdminRest(pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(summary = "Getting a list of offers to approve")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of offers to approve",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OfferResponse.class))
                    })
    })
    @GetMapping("/offers/approve")
    public OfferResponse getAllNotApproveOffers(
            @RequestParam(value = "pageNo", defaultValue = RestPaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestPaginationConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestPaginationConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestPaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir

    ) {
        return offerService.getAllNotApproveOffers(pageNo, pageSize, sortBy, sortDir);
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public String onUserNotFound(UserNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({CategoryNotFoundException.class})
    public String onCategoryNotFound(CategoryNotFoundException exception, Model model){
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
