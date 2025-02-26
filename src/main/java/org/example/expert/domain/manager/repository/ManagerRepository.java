package org.example.expert.domain.manager.repository;

import org.example.expert.domain.manager.entity.Manager;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    /**
     * Lv.5 @EntityGraph 를 사용하여 가독성 개선
     */
    @EntityGraph(attributePaths = {"users"})
    List<Manager> findManagersByTodoId(Long todoId);
}
