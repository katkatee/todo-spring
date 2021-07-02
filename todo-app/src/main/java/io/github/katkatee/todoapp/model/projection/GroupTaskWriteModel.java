package io.github.katkatee.todoapp.model.projection;

import io.github.katkatee.todoapp.model.Task;
import io.github.katkatee.todoapp.model.TaskGroup;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class GroupTaskWriteModel {
    @NotBlank(message = "Task's description must not be empty")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    Task toTask(final TaskGroup group) {
        return new Task(description, deadline, group);
    }
}
