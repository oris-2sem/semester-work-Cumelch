package ru.itis.zooshop.model.dto.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.itis.zooshop.util.validation.FieldMatch;
import ru.itis.zooshop.util.validation.UniqueUserEmail;
import ru.itis.zooshop.util.validation.UniqueUsername;

import javax.validation.constraints.*;

@FieldMatch(
        first = "password",
        second = "confirmPassword",
        message = "Passwords do not match"
)
@RequiredArgsConstructor
@Getter
public class UserRegisterDTO {
    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 2,max = 20,message = "Must be between 2 and 20 symbols.")
    @UniqueUsername(message = "Username should be unique.")
    private String username;
    @NotEmpty(message = "First Name is required.")
    @Size(min = 2,max = 20,message = "First Name must be between 2 nad 20 symbols.")
    private String firstName;
    @NotEmpty(message = "User email should be provided.")
    @Email(message = "User email should be valid.")
    @UniqueUserEmail(message = "User email should be unique.")
    private String email;
    @NotEmpty(message = "Last Name is required.")
    @Size(min = 2,max = 20,message = "Last Name must be between 2 nad 20 symbols.")
    private String lastName;
    @NotEmpty(message = "Phone is required.")
    @Pattern(regexp = "[0-9]\\d{1,20}",message = "Phone is invalid.")
    private String phone;
    @Min(value = 10,message = "Minimum age is 10.")
    @Max(value = 99,message = "Maximum age is 99.")
    private Integer age;
    @NotEmpty(message = "Password is required.")
    @Size(min = 5,message = "Password must be at least 5 symbols.")
    private String password;
    private String confirmPassword;

    public UserRegisterDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserRegisterDTO setAge(Integer age) {
        this.age = age;
        return this;
    }

    public UserRegisterDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserRegisterDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserRegisterDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserRegisterDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserRegisterDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserRegisterDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}
