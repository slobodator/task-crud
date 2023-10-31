package com.slobodator.task.controller.v1.request;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record TaskPatchRequestV1(
        @Nullable
        String description,

        @Nullable
        TaskPriorityV1 priority,

        @Nullable
        TaskStatusRequestV1 status,

        @Nullable
        LocalDateTime deadline,

        boolean force
) {
}
