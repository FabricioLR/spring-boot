package com.ms.youtube.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ms.youtube.dtos.RoleDto;
import com.ms.youtube.models.RoleModel;
import com.ms.youtube.services.RoleService;

import jakarta.validation.Valid;

@RestController
public class RoleController {
    @Autowired
    RoleService roleService;

    @PutMapping("/updateUserRole")
    public ResponseEntity<RoleModel> updateUserRole(@RequestBody @Valid RoleDto roleDto){
        RoleModel roleModel = new RoleModel();
        BeanUtils.copyProperties(roleDto, roleModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.save(roleModel));
    }
}
