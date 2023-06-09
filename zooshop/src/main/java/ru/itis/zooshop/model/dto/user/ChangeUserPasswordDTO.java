package ru.itis.zooshop.model.dto.user;

import lombok.Getter;
import ru.itis.zooshop.util.validation.FieldMatch;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@FieldMatch(
        first = "newPassword",
        second = "confirmPassword",
        message = "Passwords do not match"
)

@Getter
public class ChangeUserPasswordDTO {
    private String username;
    @NotEmpty(message = "Password is required.")
    @Size(min = 5,message = "Password must be at least 5 symbols.")
    private String oldPassword;
    @NotEmpty(message = "New Password is required.")
    @Size(min = 5,message = "New Password must be at least 5 symbols.")
    private String newPassword;
    @NotEmpty(message = "Confirm Password is required.")
    @Size(min = 5,message = "Confirm Password must be at least 5 symbols.")
    private String confirmPassword;

    public ChangeUserPasswordDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public ChangeUserPasswordDTO setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public ChangeUserPasswordDTO setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }
    public ChangeUserPasswordDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
