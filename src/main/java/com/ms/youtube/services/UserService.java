package com.ms.youtube.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.youtube.models.UserModel;
import com.ms.youtube.repositories.RoleRepository;
import com.ms.youtube.repositories.UserRepository;


@Service
public class UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    

    public boolean findbyName(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public UserModel save(UserModel userModel){
        userModel.hashPassword(userModel.getPassword());
        return userRepository.save(userModel);
    }
}
