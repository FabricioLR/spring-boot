package com.ms.youtube.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ms.youtube.models.RoleModel;
import com.ms.youtube.models.UserModel;
import com.ms.youtube.repositories.RoleRepository;
import com.ms.youtube.repositories.UserRepository;
import com.ms.youtube.services.UserService;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        String[] roles = {"ROLE_USER", "ROLE_ADMIN"};
        for (String role : roles) {
            RoleModel roleModel = roleRepository.findByRoleName(role);
            if (roleModel == null) {
                RoleModel roleModel2 = new RoleModel();
                roleModel2.setRoleName(role);
                roleRepository.save(roleModel2);
            }
        }
        if (!userRepository.findByUsername("fabricio").isPresent()) {
            UserModel userModel = new UserModel();
            userModel.setPassword("123");
            userModel.setUsername("fabricio");
            RoleModel roleModel = roleRepository.findByRoleName("ROLE_ADMIN");
            userModel.setUserRole(roleModel);
            userService.save(userModel);
        }
        if (!userRepository.findByUsername("fabricio2").isPresent()){
            UserModel userModel = new UserModel();
            userModel.setPassword("123");
            userModel.setUsername("fabricio2");
            RoleModel roleModel = roleRepository.findByRoleName("ROLE_USER");
            userModel.setUserRole(roleModel);
            userService.save(userModel);
        }
    }
}
