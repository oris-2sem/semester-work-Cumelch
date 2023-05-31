package ru.itis.zooshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itis.zooshop.exceptions.ExceptionConstants;
import ru.itis.zooshop.exceptions.UserNotFoundException;
import ru.itis.zooshop.model.dto.user.ChangeUserPasswordDTO;
import ru.itis.zooshop.model.dto.user.UpdateUserDTO;
import ru.itis.zooshop.model.dto.user.UserRegisterDTO;
import ru.itis.zooshop.model.entity.UserEntity;
import ru.itis.zooshop.model.entity.UserRoleEntity;
import ru.itis.zooshop.model.enums.UserRoleEnum;
import ru.itis.zooshop.model.view.UserDetailsView;
import ru.itis.zooshop.model.view.UserResponse;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserService {
    void registerAndLogin(UserRegisterDTO userRegisterDTO);
    void login(String username);
    UserResponse getAllUsersAdminRest(int pageNo, int pageSize, String sortBy, String sortDir);
    UserDetailsView editUser(String username, UpdateUserDTO editUser, Principal principal);
    boolean changeUserPassword(ChangeUserPasswordDTO changeUserPasswordDTO);

    boolean isOwner(String principalName, String username);
    Long getAllUsersCount();

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    boolean isUserActive(String username);

    Optional<UserEntity> findById(Long id);

}
