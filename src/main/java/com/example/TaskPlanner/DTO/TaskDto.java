package com.example.TaskPlanner.DTO;

public class TaskDto {

    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
