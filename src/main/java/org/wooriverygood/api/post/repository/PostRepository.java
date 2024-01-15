package org.wooriverygood.api.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
