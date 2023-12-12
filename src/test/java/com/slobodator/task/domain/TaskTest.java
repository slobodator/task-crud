package com.slobodator.task.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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

    @Test
    void markDoneForceLazyAccess() {
        Task parentTask = new Task(
                new TaskDescription("rootTask")
        );
        Task subTask = new Task(
                new TaskDescription("subTask"),
                parentTask
        );
        Task nestedTask = mock(Task.class);
        when(nestedTask.isDone()).thenReturn(true);
        subTask.addSubTask(nestedTask);
        subTask.updateStatus(TaskStatus.DONE);

        parentTask.updateStatus(TaskStatus.DONE, true);

        verify(
                nestedTask,
                never().description("The nested task is not accessed anymore")
        ).markDone(true);
    }
}