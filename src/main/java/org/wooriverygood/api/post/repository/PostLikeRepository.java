package org.wooriverygood.api.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostLike;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndUsername(Post post, String username);

    boolean existsByPostAndUsername(Post post, String username);

    void deleteAllByPost(Post post);
}
