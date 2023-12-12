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

    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    private Task parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> subtasks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Embedded
    private TaskDescription description;

    private Task(
            TaskDescription description,
            TaskStatus status,
            @Nullable Task parent
    ) {
        this.parent = parent;
        this.status = Objects.requireNonNull(status);
        this.description = Objects.requireNonNull(description);
    }

    public Task(TaskDescription description) {
        this(
                description,
                null
        );
    }

    public Task(TaskDescription description, @Nullable Task parent) {
        this(
                description,
                TaskStatus.NEW,
                parent
        );
        if (parent != null) {
            parent.addSubTask(this);
        }
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

    void markDone(boolean force) {
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
        if (this.status != TaskStatus.DONE) {
            this.subtasks
                    .forEach(
                            t -> t.markDone(force)
                    );
        }
        this.status = TaskStatus.DONE;
    }

    boolean isDone() {
        return this.status == TaskStatus.DONE;
    }

    public void patch(TaskPatch patch) {
        if (patch.description() != null) {
            this.description = patch.description();
        }

        if (patch.status() != null) {
            this.updateStatus(patch.status(), patch.force());
        }
    }
}
