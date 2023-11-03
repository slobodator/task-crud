package com.slobodator.task.controller.v1.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record NewTaskRequestV1(
        @NotBlank
        String description,

        @Nullable
        Long parentTaskId
) {
}