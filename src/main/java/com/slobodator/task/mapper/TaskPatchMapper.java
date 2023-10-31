package com.slobodator.task.mapper;

import com.slobodator.task.controller.v1.request.TaskPatchRequestV1;
import com.slobodator.task.domain.TaskDeadline;
import com.slobodator.task.domain.TaskDescription;
import com.slobodator.task.domain.TaskPatch;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface TaskPatchMapper {
    TaskPatch toPatch(TaskPatchRequestV1 patchRequest);

    default TaskDescription map(String value) {
        return value != null
                ? new TaskDescription(value)
                : null;
    }

    default TaskDeadline map(LocalDateTime value) {
        return value != null
                ? new TaskDeadline(value)
                : null;
    }
}
