package org.wooriverygood.api.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.exception.CommentNotFoundException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.CommentUpdateRequest;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentUpdateService {

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;


    public void updateComment(Long commentId, CommentUpdateRequest request, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        comment.validateAuthor(member);

        comment.updateContent(request.getContent());
    }

}
