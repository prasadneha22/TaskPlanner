package com.example.TaskPlanner.Repository;

import com.example.TaskPlanner.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findByEmail(String email);
    boolean existsByEmail(String email);
}
