package org.wooriverygood.api.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.domain.CommentLike;
import org.wooriverygood.api.member.domain.Member;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);

    boolean existsByCommentAndMember(Comment comment, Member member);

    void deleteAllByComment(Comment comment);
}
