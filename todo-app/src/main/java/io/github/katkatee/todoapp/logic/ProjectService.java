package io.github.katkatee.todoapp.logic;

import io.github.katkatee.todoapp.TaskConfigurationProperties;
import io.github.katkatee.todoapp.model.Project;
import io.github.katkatee.todoapp.model.ProjectRepository;
import io.github.katkatee.todoapp.model.TaskGroupRepository;
import io.github.katkatee.todoapp.model.projection.GroupReadModel;
import io.github.katkatee.todoapp.model.projection.GroupTaskWriteModel;
import io.github.katkatee.todoapp.model.projection.GroupWriteModel;

import io.github.katkatee.todoapp.model.projection.ProjectWriteModel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService service;

    ProjectService(ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfigurationProperties config, final TaskGroupService service) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
        this.service = service;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final ProjectWriteModel entity) {
        return repository.save(entity.toProject());
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {

        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed.");
        }
      return repository.findById(projectId)
          .map(project -> {
            var targetGroup = new GroupWriteModel();
            targetGroup.setDescription(project.getDescription());
            targetGroup.setTasks(
                project.getSteps().stream()
                    .map(projectStep -> {
                          var task = new GroupTaskWriteModel();
                          task.setDescription(projectStep.getDescription());
                          task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                          return task;
                        }
                    ).collect(Collectors.toList())
            );
            return service.createGroup(targetGroup, project);
          }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
