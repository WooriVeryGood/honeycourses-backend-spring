package org.wooriverygood.api.post.service;

import org.springframework.stereotype.Service;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.repository.CommunityRepository;

import java.util.List;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;


    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public List<PostResponse> findAllPosts() {
        List<Post> posts = communityRepository.findAll();
        return posts.stream().map(PostResponse::from).toList();
    }
}
