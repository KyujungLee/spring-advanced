package org.example.expert.domain.comment.service;

import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    /**
     * Lv.3-2-2 IRE 예외를 던지는 것으로 매서드명 수정
     */
    public void comment_등록_중_할일을_찾지_못해_IRE_예외가_발생한다() {
        // given
        long todoId = 1;
        CommentSaveRequest request = new CommentSaveRequest("contents");
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        /**
         * Lv.3-2-2 원본코드와 동일한 예외인 InvalidRequestException 을 던지도록 수정
         */
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            commentService.saveComment(authUser, todoId, request);
        });

        // then
        assertEquals("Todo not found", exception.getMessage());
    }

    @Test
    public void comment를_정상적으로_등록한다() {
        // given
        long todoId = 1;
        CommentSaveRequest request = new CommentSaveRequest("contents");
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Todo todo = new Todo("title", "title", "contents", user);
        Comment comment = new Comment(request.getContents(), user, todo);

        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));
        given(commentRepository.save(any())).willReturn(comment);

        // when
        CommentSaveResponse result = commentService.saveComment(authUser, todoId, request);

        // then
        assertNotNull(result);
    }

    @Test
    void 댓글_목록_조회를_할_수_있다() {

        // given
        long todoId = 1L;
        User user = new User("a@a.com", "1", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);
        Todo todo = new Todo("title", "contents", "good", user);
        ReflectionTestUtils.setField(todo, "id", 1L);
        Comment comment = new Comment("댓글", user, todo);
        ReflectionTestUtils.setField(comment, "id", 1L);
        List<Comment> commentList = List.of(comment);
        given(commentRepository.findCommentsByTodoId(anyLong())).willReturn(commentList);

        // when
        List<CommentResponse> dtoList = commentService.getComments(todoId);

        // then
        assertEquals(1, dtoList.size());
        assertEquals(user.getId(), dtoList.get(0).getId());
        assertEquals(user.getEmail(), dtoList.get(0).getUser().getEmail());
    }
}
