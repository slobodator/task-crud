package com.slobodator.task.domain;

import jakarta.annotation.Nullable;

public record TaskPatch(
        @Nullable
        TaskDescription description,

        @Nullable
        TaskStatus status,

        boolean force
) {
}
