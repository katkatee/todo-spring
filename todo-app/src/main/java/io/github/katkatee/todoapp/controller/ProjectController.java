package io.github.katkatee.todoapp.controller;

import io.github.katkatee.todoapp.logic.ProjectService;
import io.github.katkatee.todoapp.model.Project;
import io.github.katkatee.todoapp.model.ProjectStep;
import io.github.katkatee.todoapp.model.projection.ProjectWriteModel;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
class ProjectController {
  private final ProjectService service;

  ProjectController(final ProjectService service) {
    this.service = service;
  }

  @GetMapping
  String showProjects(Model model) {
    model.addAttribute("project", new ProjectWriteModel());
    return "projects";
  }

  @PostMapping
  String addProject(
      @ModelAttribute("project") @Valid ProjectWriteModel current,
      BindingResult bindingResult,
      Model model
  ) {
    if (bindingResult.hasErrors()) {
      return "projects";
    }
    service.save(current);
    model.addAttribute("project", new ProjectWriteModel());
    model.addAttribute("projects", getProjects());
    model.addAttribute("message", "Dodano projekt!");
    return "projects";
  }

  @PostMapping(params = "addStep")
  String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
    current.getSteps().add(new ProjectStep());
    return "projects";
  }

  @PostMapping("/{id}")
  String createGroup(
      @ModelAttribute("project") ProjectWriteModel current,
      Model model,
      @PathVariable int id,
      @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
  ) {
    try {
      service.createGroup(deadline, id);
      model.addAttribute("message", "Dodano grupę!");
    } catch (IllegalStateException | IllegalArgumentException e) {
      model.addAttribute("message", "Błąd podczas tworzenia grupy!");
    }
    return "projects";
  }

  @ModelAttribute("projects")
  List<Project> getProjects() {
    return service.readAll();
  }
}