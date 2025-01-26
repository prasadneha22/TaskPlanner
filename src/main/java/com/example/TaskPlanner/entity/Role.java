package com.example.TaskPlanner.entity;

import jakarta.persistence.*;

@Entity
public class Role {

    @Id
    private Integer id;
    private String role;



    public Role(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Role(Integer id, String role) {
        this.id = id;
        this.role = role;
    }
}
