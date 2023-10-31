package com.slobodator.task.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.Delegate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class TaskDeadline implements Temporal, TemporalAdjuster, ChronoLocalDateTime<LocalDate> {
    @Delegate
    @Nullable
    private LocalDateTime deadline;
}
