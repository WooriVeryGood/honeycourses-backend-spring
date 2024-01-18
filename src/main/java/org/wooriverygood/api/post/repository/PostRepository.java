package org.wooriverygood.api.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.post.domain.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthor(String author);
}
