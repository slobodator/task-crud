package com.slobodator.task.domain;

import jakarta.annotation.Nullable;

public record TaskPatch(
        @Nullable
        TaskDescription description,

        @Nullable
        TaskDeadline deadline,

        @Nullable
        TaskPriority priority,

        @Nullable
        TaskStatus status,

        boolean force
) {
}
