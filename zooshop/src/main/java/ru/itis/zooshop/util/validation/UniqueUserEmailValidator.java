package ru.itis.zooshop.util.validation;

import ru.itis.zooshop.exceptions.UserNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.itis.zooshop.model.entity.UserEntity;
import ru.itis.zooshop.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.security.Principal;
import java.util.Optional;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail,String> {
    private final UserRepository userRepository;
    public UniqueUserEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //! FIX THE EMAIL CHECKER IF THE USER IS CURRENT LOGGED USER
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(value);
        if (userEntityOptional.isPresent()) {
            UserEntity currentLoggedUser =
                    this.userRepository
                            .findByUsername(authentication.getName())
                            .orElseThrow(UserNotFoundException::new);
            if (!currentLoggedUser.getEmail().equals(value)) {
                return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            }
        }
        return true;
    }
}
