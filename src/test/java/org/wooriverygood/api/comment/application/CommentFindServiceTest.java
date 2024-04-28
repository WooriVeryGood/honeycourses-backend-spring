package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.CommentsResponse;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CommentFindServiceTest extends CommentServiceTest {

    @InjectMocks
    private CommentFindService commentFindService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private MemberRepository memberRepository;

    private final int COMMENT_COUNT = 10;

    private List<Comment> comments = new ArrayList<>();


    @BeforeEach
    void setUp() {
        for (long i = 0; i < COMMENT_COUNT; i++) {
            Comment comment = Comment.builder()
                    .id(i)
                    .content("comment" + i)
                    .post(post)
                    .member(member)
                    .build();
            comments.add(comment);
        }
    }

    @Test
    @DisplayName("유효한 id를 통해 특정 게시글의 댓글들을 불러온다.")
    void findAllCommentsByPostId() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findAllByPostId(anyLong()))
                .thenReturn(comments);
        when(commentLikeRepository.existsByCommentAndMember(any(Comment.class), any(Member.class)))
                .thenReturn(true);

        CommentsResponse response = commentFindService.findAllCommentsByPostId(2L, authInfo);

        assertThat(response.getComments().size()).isEqualTo(COMMENT_COUNT);
    }

    @Test
    @DisplayName("삭제 처리될 댓글의 내용은 비어있다.")
    void findAllCommentsByPostId_softRemoved() {
        comments = new ArrayList<>();
        comments.add(Comment.builder()
                .id(1L)
                .content("Content")
                .post(post)
                .member(member)
                .softRemoved(true)
                .build());
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findAllByPostId(anyLong()))
                .thenReturn(comments);

        CommentsResponse response = commentFindService.findAllCommentsByPostId(2L, authInfo);

        assertThat(response.getComments().get(0).getCommentContent()).isEqualTo(null);
    }

    @Test
    @DisplayName("대댓글만 불러올 수 없다.")
    void findAllCommentsByPostId_cannot_only_reply() {
        comments = new ArrayList<>();
        comments.add(reply);
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findAllByPostId(anyLong()))
                .thenReturn(comments);

        CommentsResponse response = commentFindService.findAllCommentsByPostId(2L, authInfo);

        assertThat(response.getComments().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글과 대댓글들을 모두 불러온다.")
    void findAllCommentsByPostId_withReplies() {
        comments = new ArrayList<>();
        comments.add(comment);
        comments.add(reply);
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findAllByPostId(anyLong()))
                .thenReturn(comments);

        CommentsResponse response = commentFindService.findAllCommentsByPostId(2L, authInfo);

        assertThat(response.getComments().get(0).getReplies().size()).isEqualTo(1);
    }

}