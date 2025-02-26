package org.example.expert.domain.comment.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Lv.5 @EntityGraph 를 사용하여 가독성 개선
     */
    @EntityGraph(attributePaths = {"users"})
    List<Comment> findCommentsByTodoId(Long todoId);
}
