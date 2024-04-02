package org.wooriverygood.api.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wooriverygood.api.global.auth.AuthInfo;

@ExtendWith(MockitoExtension.class)
public class MockTest {

    protected AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();

}
