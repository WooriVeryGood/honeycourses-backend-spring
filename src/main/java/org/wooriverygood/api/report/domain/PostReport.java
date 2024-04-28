package org.wooriverygood.api.report.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.post.domain.Post;

@Entity
@Getter
@Table(name = "post_reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String message;


    @Builder
    public PostReport(Long id, Post post, Member member, String message) {
        this.id = id;
        this.post = post;
        this.member = member;
        this.message = message;
    }

    public boolean isOwner(Member member) {
        return this.member.equals(member);
    }

}
