package com.example.TaskPlanner.DTO;

public class UserDto {

    private String name;
    private String email;
    private String password;
    private Integer roleId;  // Only roleId, no need to send the entire role object
//    private Boolean isActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }


    @Override
    public String toString() {
        return "RegistrationDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roleId=" + roleId +

                '}';
    }

    public UserDto(String name, String email, String password, Integer roleId, Boolean isActive) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleId = roleId;

    }
}
