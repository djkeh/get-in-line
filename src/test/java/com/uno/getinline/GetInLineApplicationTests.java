package com.uno.getinline;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("다른 테스트의 속도 향상을 위해 비활성화")
@DisplayName("스프링 부트 기본 테스트")
@SpringBootTest
class GetInLineApplicationTests {

    @Test
    void contextLoads() {
    }

}
