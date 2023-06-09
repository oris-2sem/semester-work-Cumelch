package ru.itis.zooshop.model.view;

import java.util.List;

public class UserDetailsView {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String age;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private boolean isAdmin;
    private List<String> userRoles;


    public UserDetailsView() {
    }

    public UserDetailsView(Long id, String username, String email, String phone, String age, String firstName, String lastName, boolean isActive, boolean isAdmin, List<String> userRoles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.isAdmin = isAdmin;
        this.userRoles = userRoles;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public UserDetailsView setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
        return this;
    }

    public UserDetailsView setAdmin(boolean admin) {
        isAdmin = admin;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isActive() {
        return isActive;
    }

    public UserDetailsView setActive(boolean active) {
        isActive = active;
        return this;
    }

    public Long getId() {
        return id;
    }

    public UserDetailsView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDetailsView setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDetailsView setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserDetailsView setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAge() {
        return age;
    }

    public UserDetailsView setAge(String age) {
        this.age = age;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDetailsView setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDetailsView setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
