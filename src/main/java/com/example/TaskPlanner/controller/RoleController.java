package com.example.TaskPlanner.controller;

import com.example.TaskPlanner.Repository.RoleRepository;
import com.example.TaskPlanner.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }
}
