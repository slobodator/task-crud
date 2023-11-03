package com.slobodator.task.domain;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.Delegate;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public class TaskDescription implements CharSequence {
    @Delegate
    private String description;
}
