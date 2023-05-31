package ru.itis.zooshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.zooshop.model.entity.UserRoleEntity;
import ru.itis.zooshop.model.enums.UserRoleEnum;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity,Long> {
    UserRoleEntity findByUserRoleEnum(UserRoleEnum userRoleEnum);
}
