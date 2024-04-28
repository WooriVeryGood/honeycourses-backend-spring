package org.wooriverygood.api.global.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;

@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return createAuthInfo(jwt);
        }

        return AuthInfo.builder().build();
    }

    private AuthInfo createAuthInfo(Jwt jwt) {
        String sub = jwt.getClaim("sub");
        String username = jwt.getClaim("username");

        Member member = memberRepository.findByUsername(username)
                .orElseGet(() -> {
                    Member newMember = Member.builder().username(username).build();
                    return memberRepository.save(newMember);
                });

        return AuthInfo.builder()
                .memberId(member.getId())
                .sub(sub)
                .username(username)
                .build();
    }
}
