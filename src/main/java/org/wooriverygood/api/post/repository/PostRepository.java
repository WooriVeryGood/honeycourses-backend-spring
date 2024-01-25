package org.wooriverygood.api.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.post.domain.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByIdDesc(Pageable pageable);

    List<Post> findByAuthor(String author);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET like_count = like_count + 1 WHERE post_id = :postId", nativeQuery = true)
    void increaseLikeCount(Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE posts SET like_count = like_count - 1 WHERE post_id = :postId", nativeQuery = true)
    void decreaseLikeCount(Long postId);
}
