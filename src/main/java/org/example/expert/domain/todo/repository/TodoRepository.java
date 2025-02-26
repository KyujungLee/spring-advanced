package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * Lv.2 @EntityGrapg 를 사용하여 자바의 언어로만 쿼리관계를 작성.
     * JPQL에서 제공하는 SQL과 유사한 문법이 눈에 보이지 않아 유지보수성 향상.
     */
    @EntityGraph(attributePaths = {"users"})
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    /**
     * Lv.5 @EntityGraph 를 사용하여 가독성 개선
     */
    @EntityGraph(attributePaths = {"users"})
    Optional<Todo> findTodoById(Long todoId);
}
