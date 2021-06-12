package io.github.katkatee.todoapp.logic;

import io.github.katkatee.todoapp.TaskConfigurationProperties;
import io.github.katkatee.todoapp.model.Project;
import io.github.katkatee.todoapp.model.ProjectRepository;
import io.github.katkatee.todoapp.model.Task;
import io.github.katkatee.todoapp.model.TaskGroup;
import io.github.katkatee.todoapp.model.TaskGroupRepository;
import io.github.katkatee.todoapp.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    ProjectService(ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project saveProject(Project entity) {
        return repository.save(entity);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {

        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed.");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(step -> new Task(
                                    step.getDescription(),
                                    deadline.plusDays(step.getDaysToDeadline()
                            ))).collect(Collectors.toSet()));
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("project  with given id not found"));
        return new GroupReadModel(result);
    }
}
