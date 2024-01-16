package org.wooriverygood.api.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.comment.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

}
