package org.wooriverygood.api.global.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthInfo {

    private Long memberId;

    private final String sub;

    private final String username;

    @Builder
    public AuthInfo(Long memberId, String sub, String username) {
        this.memberId = memberId;
        this.sub = sub;
        this.username = username;
    }
}
