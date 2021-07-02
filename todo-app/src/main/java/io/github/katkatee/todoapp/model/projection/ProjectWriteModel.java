package io.github.katkatee.todoapp.model.projection;

import io.github.katkatee.todoapp.model.Project;
import io.github.katkatee.todoapp.model.ProjectStep;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class ProjectWriteModel {
  @NotBlank(message = "Project's description can not be empty.")
  private String description;
  @Valid
  private List<ProjectStep> steps = new ArrayList<>();

  public ProjectWriteModel() {
    steps.add(new ProjectStep());
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ProjectStep> getSteps() {
    return steps;
  }

  public void setSteps(List<ProjectStep> steps) {
    this.steps = steps;
  }

  public Project toProject() {
    var result = new Project();
    result.setDescription(description);
    steps.forEach(step -> step.setProject(result));
    result.setSteps(new HashSet<>(steps));
    return result;
  }
}
