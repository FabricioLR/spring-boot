package com.ms.youtube.controllers;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ms.youtube.configs.security.JwtConfiguration;
import com.ms.youtube.configs.security.token.TokenCreator;
import com.ms.youtube.dtos.UserDto;
import com.ms.youtube.models.RoleModel;
import com.ms.youtube.models.UserModel;
import com.ms.youtube.repositories.RoleRepository;
import com.ms.youtube.services.UserService;
import com.nimbusds.jwt.SignedJWT;

import jakarta.validation.Valid;
import lombok.SneakyThrows;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    JwtConfiguration jwtConfiguration;

    @Autowired
    TokenCreator tokenCreator;

    @Autowired
    RoleRepository roleRepository;

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserDto userDto){
        HttpHeaders headers = new HttpHeaders();
        if (userDto.getPassword() == null || userDto.getPassword() == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("credentials cant be empty");
        }
        if (userService.findbyName(userDto.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("user already exists");
        }
        UserModel userModel = new UserModel();
        RoleModel roleModel = roleRepository.findByRoleName("ROLE_USER");
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserRole(roleModel);
        SignedJWT signedJWT = tokenCreator.createSignedJWT(userModel);

        String encryptedToken = tokenCreator.encryptToken(signedJWT);

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        headers.add("Access-Control-Expose-Headers", "XSRF-TOKEN, " + jwtConfiguration.getHeader().getName());
        headers.add(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + encryptedToken);
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(userService.save(userModel));
    }
    
    @GetMapping("/userInfo")
    public ResponseEntity<UserModel> userInfo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(((UserModel)principal));
    }
}
