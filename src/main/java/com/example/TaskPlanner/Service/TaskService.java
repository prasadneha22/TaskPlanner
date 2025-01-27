package com.example.TaskPlanner.Service;

import com.example.TaskPlanner.DTO.TaskDto;
import com.example.TaskPlanner.DTO.TaskListDto;
import com.example.TaskPlanner.Repository.TaskRepository;
import com.example.TaskPlanner.Repository.UserRepository;
import com.example.TaskPlanner.entity.Task;
import com.example.TaskPlanner.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    public TaskDto createTask(String token, TaskDto taskDto) {

        Integer userId = jwtService.extractUserId(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        if(user.getRole().getRole().equals("ADMIN")){
            throw new RuntimeException("Admins cannot create tasks.");
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUser(user);

        taskRepository.save(task);
        return new TaskDto(taskDto.getTitle(), taskDto.getDescription());
    }



    public List<TaskListDto> getAllTasks(String token) {

        Integer userId = jwtService.extractUserId(token);

//

        List<Task> tasks = taskRepository.findAll();


        List<TaskListDto> userTasks = tasks.stream()
                .filter(task->task.getUser().getId().equals(userId))
                .map(task -> new TaskListDto(task.getTitle(),task.getDescription(),task.getStatus()))
                .collect(Collectors.toList());

        if(userTasks.isEmpty()){
            throw new RuntimeException("You dont have permission to access these tasks.");
        }
        return userTasks;





    }
}
