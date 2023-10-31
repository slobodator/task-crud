package com.slobodator.task.mapper;

import com.slobodator.task.controller.v1.request.TaskPriorityV1;
import com.slobodator.task.domain.TaskPriority;
import org.mapstruct.Mapper;

@Mapper
public interface TaskPriorityMapper {
    TaskPriority toPriority(TaskPriorityV1 priority);
}
