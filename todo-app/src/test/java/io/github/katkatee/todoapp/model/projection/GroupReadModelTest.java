package io.github.katkatee.todoapp.model.projection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.github.katkatee.todoapp.model.Task;
import io.github.katkatee.todoapp.model.TaskGroup;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GroupReadModelTest {

  @Test
  void constructore_noDeadlines_createsNullDeadline() {
    //given
    var source = new TaskGroup();
    source.setDescription("jakiś opis");
    source.setTasks(Set.of(new Task("bat", null)));

    //when
    var result = new GroupReadModel(source);

    //then
    assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
  }

}