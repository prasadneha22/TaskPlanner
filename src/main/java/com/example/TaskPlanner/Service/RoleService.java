package com.example.TaskPlanner.Service;

import com.example.TaskPlanner.Repository.RoleRepository;
import com.example.TaskPlanner.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public void addRoles(){
        Role admin = new Role(1,"ADMIN");
        Role user = new Role(2,"USER");

        if(!roleRepository.existsById(admin.getId())){
            roleRepository.save(admin);
        }
        if(!roleRepository.existsById(user.getId())){
            roleRepository.save(user);
        }
    }

}
