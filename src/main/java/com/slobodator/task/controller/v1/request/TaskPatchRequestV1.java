package com.slobodator.task.controller.v1.request;

import jakarta.annotation.Nullable;

public record TaskPatchRequestV1(
        @Nullable
        String description,

        @Nullable
        TaskStatusRequestV1 status,

        boolean force
) {
}
