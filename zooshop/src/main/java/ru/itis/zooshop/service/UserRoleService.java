package ru.itis.zooshop.service;

import ru.itis.zooshop.model.entity.UserRoleEntity;
import ru.itis.zooshop.model.enums.UserRoleEnum;

public interface UserRoleService {
    UserRoleEntity findByUserRoleEnum(UserRoleEnum userRoleEnum);
}
