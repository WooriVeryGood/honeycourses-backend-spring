package org.wooriverygood.api.global.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthInfo {

    private final String sub;
    private final String username;

    @Builder
    public AuthInfo(String sub, String username) {
        this.sub = sub;
        this.username = username;
    }
}
