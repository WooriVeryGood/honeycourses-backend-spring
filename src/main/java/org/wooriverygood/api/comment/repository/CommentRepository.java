package org.wooriverygood.api.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.post.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

    void deleteAllByPost(Post post);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE comments SET like_count = like_count + 1 WHERE comment_id = :commentId", nativeQuery = true)
    void increaseLikeCount(Long commentId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE comments SET like_count = like_count - 1 WHERE comment_id = :commentId", nativeQuery = true)
    void decreaseLikeCount(Long commentId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE comments SET report_count = report_count + 1 WHERE comment_id = :commentId", nativeQuery = true)
    void increaseReportCount(Long commentId);
}
