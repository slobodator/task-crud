package com.slobodator.task.controller.v1;

import com.slobodator.task.controller.v1.request.NewTaskRequestV1;
import com.slobodator.task.controller.v1.request.TaskHierarchyV1;
import com.slobodator.task.controller.v1.request.TaskPatchRequestV1;
import com.slobodator.task.controller.v1.response.TaskDtoV1;
import com.slobodator.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskControllerV1 {
    private final TaskService taskService;

    @PostMapping
    public TaskDtoV1 create(
            @Valid
            @RequestBody
            NewTaskRequestV1 newTaskRequest
    ) {
        return taskService.create(newTaskRequest);
    }

    @GetMapping("/{taskId}")
    public TaskDtoV1 get(
            @PathVariable("taskId")
            Long taskId,

            @RequestParam(name = "hierarchy", defaultValue = "NONE")
            TaskHierarchyV1 taskHierarchy
    ) {
        return taskService.get(taskId, taskHierarchy);
    }

    @PatchMapping("/{taskId}")
    public TaskDtoV1 patch(
            @PathVariable("taskId")
            Long taskId,

            @Valid
            @RequestBody
            TaskPatchRequestV1 patchTaskRequest
    ) {
        return taskService.patch(taskId, patchTaskRequest);
    }
}
