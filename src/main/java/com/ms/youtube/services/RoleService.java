package com.ms.youtube.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.youtube.models.RoleModel;
import com.ms.youtube.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public RoleModel save(RoleModel roleModel){
        return roleRepository.save(roleModel);
    }

    public RoleModel findByRoleName(String roleName){
        return roleRepository.findByRoleName(roleName);
    }
}
