package com.example.TaskPlanner.Service;

import com.example.TaskPlanner.DTO.LoginDto;
import com.example.TaskPlanner.Repository.UserRepository;
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

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users users) {
        Users existingUser = userRepository.findByEmail(users.getEmail());
        System.out.println(existingUser);

        if(existingUser != null){
            throw new IllegalArgumentException("Email already exist");
        }
//        if(userRepository.existsByEmail(users.getEmail())){
//            throw new IllegalArgumentException("Email Already exist");
//        }
        users.setPassword(encoder.encode(users.getPassword()));
        return userRepository.save(users);

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
                response.put("role",user.getRole());
                response.put("token",token);
                return response;
            }
        }catch (Exception e){
            response.put("error", "Invalid credentials!" + e);
        }
        return response;
    }

}
