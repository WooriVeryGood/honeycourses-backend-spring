package org.wooriverygood.api.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostLike;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndMember(Post post, Member member);

    boolean existsByPostAndMember(Post post, Member member);

    void deleteAllByPost(Post post);
}
