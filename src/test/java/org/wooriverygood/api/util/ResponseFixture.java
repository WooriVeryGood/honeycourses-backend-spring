package org.wooriverygood.api.util;

import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.dto.PostsResponse;
import org.wooriverygood.api.support.AuthInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResponseFixture {


    public static PostResponse postResponse(long id, AuthInfo authInfo) {
        String category = "자유";
        if (id % 3 == 0) category = "중고거래";
        else if (id % 5 == 0) category = "구인";
        else if (id % 7 == 0) category = "질문";
        return postResponse(id, category, authInfo);
    }

    public static PostResponse postResponse(long id, String category, AuthInfo authInfo) {
        return PostResponse.builder()
                .post_id(id)
                .post_title("title_" + id)
                .post_category(category)
                .post_content("content_" + id)
                .post_author(authInfo.getUsername())
                .post_comments((int) (Math.random() * 100))
                .post_likes((int) (Math.random() * 100))
                .post_time(LocalDateTime.now())
                .liked(id % 6 == 0)
                .updated(id % 9 == 0)
                .reported(false)
                .build();
    }

    public static PostResponse reportedPostResponse(long id, AuthInfo authInfo) {
        String category = "자유";
        if (id % 3 == 0) category = "중고거래";
        else if (id % 5 == 0) category = "구인";
        else if (id % 7 == 0) category = "질문";
        return reportedPostResponse(id, category, authInfo);
    }

    public static PostResponse reportedPostResponse(long id, String category, AuthInfo authInfo) {
        return PostResponse.builder()
                .post_id(id)
                .post_title(null)
                .post_category(category)
                .post_content(null)
                .post_author(authInfo.getUsername())
                .post_comments((int) (Math.random() * 100))
                .post_likes((int) (Math.random() * 100))
                .post_time(LocalDateTime.now())
                .liked(id % 6 == 0)
                .updated(id % 9 == 0)
                .reported(true)
                .build();
    }

    public static PostsResponse postsResponse(int page, int totalCount, AuthInfo authInfo) {
        List<PostResponse> responses = new ArrayList<>();
        int bound = Math.min(totalCount - page * 10 + 1, 10);
        int start = page * 10 + 1;
        for (int i = start; i < start + bound; i++) {
            if (i % 5 == 0) responses.add(reportedPostResponse(i, authInfo));
            else responses.add(postResponse(i, authInfo));
        }
        return PostsResponse.builder()
                .posts(responses)
                .totalPostCount(totalCount)
                .totalPageCount((int) Math.ceil((double) totalCount / 10))
                .build();
    }

    public static PostsResponse postsResponse(int page, int totalCount, String category, AuthInfo authInfo) {
        List<PostResponse> responses = new ArrayList<>();
        int bound = Math.min(totalCount - page * 10 + 1, 10);
        int start = page * 10 + 1;
        for (int i = start; i < start + bound; i++) {
            if (i % 5 == 0) responses.add(reportedPostResponse(i, authInfo));
            else responses.add(postResponse(i, authInfo));
        }
        return PostsResponse.builder()
                .posts(responses)
                .totalPostCount(totalCount)
                .totalPageCount((int) Math.ceil((double) totalCount / 10))
                .build();
    }
}

