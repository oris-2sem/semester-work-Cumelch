package ru.itis.zooshop.util.validation;

import org.hibernate.dialect.function.TemplateRenderer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.itis.zooshop.model.entity.UserEntity;
import ru.itis.zooshop.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername,String> {
    private final UserRepository userRepository;

    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(value);
        if (userEntityOptional.isPresent()) {

            Optional<UserEntity> currentLoggedUser = this.userRepository
                    .findByUsername(authentication.getName());

            if (authentication.getName().equals(value)) {
                return true;
            }

            if (currentLoggedUser.isPresent()) {
                if (!currentLoggedUser.get().getUsername().equals(value)) {
                    return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                }
            }
            return false;
        }
        return true;
    }
}
