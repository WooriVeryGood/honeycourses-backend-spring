package org.wooriverygood.api.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.review.domain.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "old_username")
    private String oldUsername;

    private String email;

    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();


    @Builder
    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public boolean isSame(Member member) {
        return Objects.equals(this.id, member.getId());
    }

    public void verify(Member member) {
        if (!isSame(member)) {
            throw new AuthorizationException();
        }
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

}