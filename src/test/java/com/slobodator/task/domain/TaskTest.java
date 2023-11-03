package com.slobodator.task.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("unused")
class TaskTest {
    @Test
    void tryMarkDone() {
        Task parentTask = new Task(
                new TaskDescription("rootTask")
        );
        Task subTask = new Task(
                new TaskDescription("subTask"),
                parentTask
        );
        assertThatThrownBy(
                () -> parentTask.updateStatus(TaskStatus.DONE)
        ).hasMessageMatching("Task .* hasn't been done.*");
    }

    @Test
    void markDoneForce() {
        Task parentTask = new Task(
                new TaskDescription("rootTask")
        );
        Task subTask = new Task(
                new TaskDescription("subTask"),
                parentTask
        );

        parentTask.updateStatus(TaskStatus.DONE, true);

        assertThat(parentTask.getStatus()).isEqualTo(TaskStatus.DONE);
        assertThat(subTask.getStatus()).isEqualTo(TaskStatus.DONE);
    }
}