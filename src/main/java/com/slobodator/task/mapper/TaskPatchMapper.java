package com.slobodator.task.mapper;

import com.slobodator.task.controller.v1.request.TaskPatchRequestV1;
import com.slobodator.task.domain.TaskDescription;
import com.slobodator.task.domain.TaskPatch;
import org.mapstruct.Mapper;

@Mapper
public interface TaskPatchMapper {
    TaskPatch toPatch(TaskPatchRequestV1 patchRequest);

    @SuppressWarnings("unused")
    default TaskDescription map(String value) {
        return value != null
                ? new TaskDescription(value)
                : null;
    }
}
