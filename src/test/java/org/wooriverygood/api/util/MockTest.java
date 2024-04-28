package org.wooriverygood.api.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;

@ExtendWith(MockitoExtension.class)
public class MockTest {

    protected AuthInfo authInfo = AuthInfo.builder()
            .memberId(1L)
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();

    protected Member member = new Member(1L, authInfo.getUsername());

}
