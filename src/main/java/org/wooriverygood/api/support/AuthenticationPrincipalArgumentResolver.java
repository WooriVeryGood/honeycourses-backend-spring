package org.wooriverygood.api.support;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // SecurityContextHolder를 통해 현재 인증된 사용자의 Authentication을 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Authentication 객체에서 Jwt를 추출
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return createAuthInfo(jwt);
        } else {
            throw new RuntimeException("Jwt not found in the authentication context");
        }
    }

    private AuthInfo createAuthInfo(Jwt jwt) {
        String sub = jwt.getClaim("sub");
        String username = jwt.getClaim("username");

        return AuthInfo.builder()
                .sub(sub)
                .username(username)
                .build();
    }
}
