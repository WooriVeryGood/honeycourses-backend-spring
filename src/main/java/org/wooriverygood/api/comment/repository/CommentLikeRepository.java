package org.wooriverygood.api.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.domain.CommentLike;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndUsername(Comment comment, String username);

    boolean existsByCommentAndUsername(Comment comment, String username);

    void deleteAllByComment(Comment comment);
}
