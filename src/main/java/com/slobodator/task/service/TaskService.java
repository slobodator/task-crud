package com.slobodator.task.service;

import com.slobodator.task.controller.v1.request.NewTaskRequestV1;
import com.slobodator.task.controller.v1.request.TaskHierarchyV1;
import com.slobodator.task.controller.v1.request.TaskPatchRequestV1;
import com.slobodator.task.controller.v1.response.TaskDtoV1;
import com.slobodator.task.domain.Task;
import com.slobodator.task.domain.TaskDescription;
import com.slobodator.task.domain.TaskPatch;
import com.slobodator.task.mapper.TaskMapper;
import com.slobodator.task.mapper.TaskPatchMapper;
import com.slobodator.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskPatchMapper taskPatchMapper;

    public TaskDtoV1 create(NewTaskRequestV1 newTaskRequest) {
        Task parentTask = Optional
                .ofNullable(newTaskRequest.parentTaskId())
                .map(
                        id -> taskRepository
                                .findById(id)
                                .orElseThrow(
                                        () -> new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Task %d not found".formatted(id)
                                        )
                                )
                )
                .orElse(null);
        return taskMapper
                .toDtoV1(
                        taskRepository
                                .save(
                                        new Task(
                                                new TaskDescription(newTaskRequest.description()), parentTask
                                        )
                                )
                );
    }

    @Transactional(readOnly = true)
    public TaskDtoV1 get(Long taskId, TaskHierarchyV1 taskHierarchy) {
        return taskMapper
                .toDtoV1(
                        taskRepository
                                .findById(taskId)
                                .orElseThrow(
                                        () -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Task %d not found".formatted(taskId)
                                        )
                                ),
                        taskHierarchy
                );
    }

    public TaskDtoV1 patch(Long taskId, TaskPatchRequestV1 patchRequest) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Task %d not found".formatted(taskId)
                        )
                );

        TaskPatch taskPatch = taskPatchMapper.toPatch(patchRequest);

        task.patch(taskPatch);

        return taskMapper
                .toDtoV1(task);
    }
}
