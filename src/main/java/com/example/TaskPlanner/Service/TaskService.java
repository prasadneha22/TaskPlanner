package com.example.TaskPlanner.Service;

import com.example.TaskPlanner.DTO.TaskDto;
import com.example.TaskPlanner.DTO.TaskListDto;
import com.example.TaskPlanner.Repository.TaskRepository;
import com.example.TaskPlanner.Repository.UserRepository;
import com.example.TaskPlanner.entity.Task;
import com.example.TaskPlanner.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.rowset.spi.SyncResolver;
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


        String userRole = jwtService.extractUserRole(token);
        Integer userId = jwtService.extractUserId(token);
        System.out.println("User id is : " + userId);
        System.out.println("role is : " + userRole);

        Users user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found!!"));

        List<Task> tasks;

        if("ADMIN".equals(userRole)){
          tasks = taskRepository.findAll();
        }else {
            tasks = taskRepository.findByUserId(userId);
        }

        return tasks.stream()
                .map(task -> new TaskListDto(task.getTitle(), task.getDescription(), task.getStatus()))
                .collect(Collectors.toList());

    }


    public TaskListDto getTaskById(String token, Integer taskId) {
        String userRole = jwtService.extractUserRole(token);
        Integer userId = jwtService.extractUserId(token);

//        System.out.println("userid: " + userId);
//        System.out.println("userRole: " + userRole);

        Task task;

        if("ADMIN".equals(userRole)){
            task = taskRepository.findById(taskId)
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for the given ID!"));
        }else{
            task = taskRepository.findByIdAndUserId(taskId,userId)
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied! You can only view your own tasks."));
        }

        return new TaskListDto(task.getTitle(),task.getDescription(),task.getStatus());
    }

    public TaskListDto updateTask(String token, Integer taskId, TaskListDto taskListDto) {
        Integer userId = jwtService.extractUserId(token);
        String role  = jwtService.extractUserRole(token);

        if("ADMIN".equals(role)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin cannot update tasks!");

        }

        Task task = taskRepository.findByIdAndUserId(taskId,userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied! You can only update your own tasks."));

        if(taskListDto.getTitle()!=null){
            task.setTitle(taskListDto.getTitle());
        }
        if(taskListDto.getDescription()!=null){
            task.setDescription(taskListDto.getDescription());
        }
        if(taskListDto.getStatus()!=null){
            task.setStatus(taskListDto.getStatus());
        }
        taskRepository.save(task);

        return new TaskListDto(task.getTitle(),task.getDescription(),task.getStatus());
    }

    public void deleteTask(String token, Integer taskId) {
        String userRole = jwtService.extractUserRole(token);
        Integer userId = jwtService.extractUserId(token);

        if("ADMIN".equals(userRole)){
            if(!taskRepository.existsById(taskId)){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found.");
            }
            taskRepository.deleteById(taskId);
        }else{
            Task task = taskRepository.findByIdAndUserId(taskId,userId)
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied! You can only delete your own tasks."));
            taskRepository.delete(task);
        }
    }
}
