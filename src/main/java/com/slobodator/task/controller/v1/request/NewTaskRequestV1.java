package com.slobodator.task.controller.v1.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record NewTaskRequestV1(
        @NotBlank
        String description,

        @Nullable
        Long parentTaskId,

        @Nullable
        TaskPriorityV1 priority,

        @Nullable
        LocalDateTime deadline
) {
}