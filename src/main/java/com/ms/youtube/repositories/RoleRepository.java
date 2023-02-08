package com.ms.youtube.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ms.youtube.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, UUID>{
    RoleModel findByRoleName(String roleName);
}
