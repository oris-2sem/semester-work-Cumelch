package ru.itis.zooshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfferNotFoundException extends RuntimeException {
    private final Long id;
    private final String message;

    public OfferNotFoundException(String message, Long id) {
        super(message);
        this.message = message;
        this.id = id;
    }

    public OfferNotFoundException(Long id) {
        this.id = id;
        this.message = "Offer with id " + id + " was not found!";
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
