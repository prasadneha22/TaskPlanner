package com.example.TaskPlanner.controller;

import com.example.TaskPlanner.DTO.TaskDto;
import com.example.TaskPlanner.DTO.TaskListDto;
import com.example.TaskPlanner.Service.TaskService;
import com.example.TaskPlanner.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestHeader("Authorization") String token, @RequestBody TaskDto taskDto){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{
            TaskDto taskResponse = taskService.createTask(token,taskDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage() );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }

    }



//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PutMapping("/updateTask")
//    public ResponseEntity<?> updateTask(@RequestHeader("Authorization") String token, @RequestBody )

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/getTasks")
    public ResponseEntity<?> getAllTasks(@RequestHeader("Authorization") String token){
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try{

            List<TaskListDto> tasks = taskService.getAllTasks(token);

            if(tasks.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO task found for this user.");

            }
            return ResponseEntity.status(HttpStatus.OK).body(tasks);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred!");
        }
    }
}
