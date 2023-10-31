package com.slobodator.task.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.function.Predicate.not;

@SuppressWarnings({"FieldMayBeFinal", "JpaDataSourceORMInspection"})
@Entity
@Table(name = "tasks")

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Slf4j
public class Task {
    @SuppressWarnings("unused")
    @Id
    @SequenceGenerator(name = "tasks_seq", sequenceName = "tasks_seq")
    @GeneratedValue(generator = "tasks_seq", strategy = GenerationType.SEQUENCE)
    @ToString.Include
    private Long id;

    @ManyToOne
    @Nullable
    private Task parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> subtasks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Embedded
    private TaskDescription description;

    @Embedded
    @Nullable
    private TaskDeadline deadline;

    private Task(
            @Nullable Task parent,
            TaskPriority priority,
            TaskStatus status,
            TaskDescription description,
            @Nullable TaskDeadline deadline
    ) {
        this.parent = parent;
        this.priority = Objects.requireNonNull(priority);
        this.status = Objects.requireNonNull(status);
        this.description = Objects.requireNonNull(description);
        this.deadline = deadline;
    }

    public Task(
            @Nullable Task parentTask,
            @Nullable TaskPriority priority,
            TaskDescription description,
            @Nullable TaskDeadline deadline
    ) {
        this(
                parentTask,
                Optional.ofNullable(priority).orElse(TaskPriority.MEDIUM),
                TaskStatus.NEW,
                description,
                deadline
        );
    }

    public Task(TaskDescription description) {
        this(
                null,
                null,
                description,
                null
        );
    }

    public Task(TaskDescription description, Task parent) {
        this(
                parent,
                null,
                description,
                null
        );
        parent.addSubTask(this);
    }

    public void addSubTask(Task task) {
        this.subtasks.add(task);
        task.assignParent(this);
    }

    private void assignParent(Task task) {
        if (this.parent != null && this.parent != task) {
            throw new IllegalStateException(
                    "Task has already had the parent assigned!"
            );
        }
        this.parent = task;
    }

    public void updateStatus(TaskStatus status, boolean force) {
        switch (status) {
            case NEW, IN_PROGRESS -> this.status = status;
            case DONE -> this.markDone(force);
        }
    }

    public void updateStatus(TaskStatus status) {
        updateStatus(status, false);
    }

    private void markDone(boolean force) {
        if (!force) {
            this.subtasks
                    .stream()
                    .filter(not(Task::isDone))
                    .findFirst()
                    .ifPresent(
                            s -> {
                                throw new IllegalStateException(
                                        "Task %d hasn't been done. Complete it or use the 'force' flag"
                                                .formatted(s.id)
                                );
                            }
                    );
        }
        this.subtasks
                .forEach(
                        t -> t.markDone(force)
                );

        this.status = TaskStatus.DONE;
    }

    private boolean isDone() {
        return this.status == TaskStatus.DONE;
    }

    public void patch(TaskPatch patch) {
        if (patch.description() != null) {
            this.description = patch.description();
        }

        if (patch.deadline() != null) {
            this.deadline = patch.deadline();
        }

        if (patch.priority() != null) {
            this.priority = patch.priority();
        }

        if (patch.status() != null) {
            this.updateStatus(patch.status(), patch.force());
        }
    }
}
