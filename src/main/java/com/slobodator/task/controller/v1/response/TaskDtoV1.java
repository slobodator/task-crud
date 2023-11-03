package com.slobodator.task.controller.v1.response;

import lombok.Data;

import java.util.List;

@Data
public class TaskDtoV1 {
    private Long id;
    private Long parentTaskId;
    private List<TaskDtoV1> subTasks;

    private String description;
    private TaskStatusResponseV1 status;
}
