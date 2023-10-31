package com.slobodator.task.mapper;

import com.slobodator.task.controller.v1.request.TaskHierarchyV1;
import com.slobodator.task.controller.v1.response.TaskDtoV1;
import com.slobodator.task.domain.Task;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

import static com.slobodator.task.controller.v1.request.TaskHierarchyV1.ENTIRE_GRAPH;

@Mapper
public interface TaskMapper {
    @Mapping(target = "parentTaskId", source = "parent.id")
    @Mapping(target = "description", source = "description.description")
    @Mapping(target = "deadline", source = "deadline.deadline")
    @Mapping(target = "subTasks", expression = "java(subTasks(task, hierarchy))")
    TaskDtoV1 toDtoV1(Task task, @Context TaskHierarchyV1 hierarchy);

    default TaskDtoV1 toDtoV1(Task task) {
        return toDtoV1(task, TaskHierarchyV1.NONE);
    }

    @AfterMapping
    default List<TaskDtoV1> subTasks(Task task, TaskHierarchyV1 hierarchy) {
        return switch (hierarchy) {
            case NONE -> Collections.emptyList();
            case FIRST_LEVEL -> task
                    .getSubtasks()
                    .stream()
                    .map(this::toDtoV1)
                    .toList();
            case ENTIRE_GRAPH -> task
                    .getSubtasks()
                    .stream()
                    .map(t -> toDtoV1(t, ENTIRE_GRAPH))
                    .toList();
        };
    }
}
