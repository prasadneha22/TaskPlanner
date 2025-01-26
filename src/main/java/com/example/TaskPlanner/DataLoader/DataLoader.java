package com.example.TaskPlanner.DataLoader;

import com.example.TaskPlanner.Service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleService roleService;

    public DataLoader(RoleService roleService){
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.addRoles();

    }
}
