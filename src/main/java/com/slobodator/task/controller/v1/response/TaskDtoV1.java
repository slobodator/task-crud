package com.slobodator.task.controller.v1.response;

import com.slobodator.task.controller.v1.request.TaskPriorityV1;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDtoV1 {
    private Long id;
    private Long parentTaskId;
    private List<TaskDtoV1> subTasks;

    private String description;
    private TaskPriorityV1 priority;
    private TaskStatusResponseV1 status;
    private LocalDateTime deadline;
}
