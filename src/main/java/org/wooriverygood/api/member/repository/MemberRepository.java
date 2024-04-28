package org.wooriverygood.api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

}
