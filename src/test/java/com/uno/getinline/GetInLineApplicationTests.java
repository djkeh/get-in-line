package com.uno.getinline;

import com.uno.getinline.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class GetInLineApplicationTests {

    @Test
    void contextLoads() {
    }


    // TODO: 불필요해지면 나중에 지울 것
    @TestConfiguration
    static class TestConfig {
        @Bean EventRepository eventRepository() { return new EventRepository() {}; }
    }

}
