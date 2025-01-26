package com.example.TaskPlanner.Service;

import com.example.TaskPlanner.DTO.LoginDto;
import com.example.TaskPlanner.DTO.UserDto;
import com.example.TaskPlanner.Repository.RoleRepository;
import com.example.TaskPlanner.Repository.UserRepository;
import com.example.TaskPlanner.entity.Role;
import com.example.TaskPlanner.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RoleRepository roleRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(UserDto registrationDto) {

        Users existingUser = userRepository.findByEmail(registrationDto.getEmail());
        if(existingUser!=null){
            throw new RuntimeException("Email Already Exists!");
        }

        Role role = roleRepository.findById(registrationDto.getRoleId())
                .orElseThrow(()->new RuntimeException("Role not found!"));

        Users user = new Users();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(encoder.encode(registrationDto.getPassword()));
        user.setRole(role);
        user.setActive(true);

        return userRepository.save(user);

    }

    public Map<String, Object> verify(LoginDto loginDto) {
        Map<String,Object> response = new HashMap<>();

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
            );

            if(authentication.isAuthenticated()){
                Users user = userRepository.findByEmail(loginDto.getEmail());
                if(user == null){
                    response.put("Error", "User not found!");
                    return response;
                }

                String token = jwtService.generateToken(user);

                response.put("id",user.getId());
                response.put("email",user.getEmail());
                response.put("name",user.getName());
                response.put("role",user.getRole().getRole());
                response.put("isActive",user.getActive());
                response.put("token",token);

                return response;
            }
        }catch (Exception e){
            response.put("error", "Invalid credentials!" + e);
        }
        return response;
    }

}
