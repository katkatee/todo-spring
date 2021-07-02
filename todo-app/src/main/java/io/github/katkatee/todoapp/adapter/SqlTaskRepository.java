package io.github.katkatee.todoapp.adapter;

import io.github.katkatee.todoapp.model.Task;
import io.github.katkatee.todoapp.model.TaskRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer id);

    @Override
    List<Task> findAllByGroup_Id(Integer groupId);
}
