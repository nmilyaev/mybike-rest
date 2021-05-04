package com.bike;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@SpringBootTest(
        classes = BorrowMyBikeApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class BikeServiceIntegrationTest {

    @Test
    void contextLoads() {
        log.info("............classpath: {}", System.getProperty("java.class.path"));
    }
}
