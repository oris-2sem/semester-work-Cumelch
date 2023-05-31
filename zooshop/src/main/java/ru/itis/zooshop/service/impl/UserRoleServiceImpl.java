package ru.itis.zooshop.service.impl;

import org.springframework.stereotype.Service;
import ru.itis.zooshop.model.entity.UserRoleEntity;
import ru.itis.zooshop.model.enums.UserRoleEnum;
import ru.itis.zooshop.repository.UserRoleRepository;
import ru.itis.zooshop.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRoleEntity findByUserRoleEnum(UserRoleEnum userRoleEnum){
        return this.userRoleRepository.findByUserRoleEnum(userRoleEnum);
    }
}
