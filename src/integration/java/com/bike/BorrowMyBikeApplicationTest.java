package com.bike;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = BorrowMyBikeApplication.class)
@ActiveProfiles("integration")
class BorrowMyBikeApplicationTest {

  @Autowired private ServerProperties serverProperties;

  @Test
  void contextLoads() {}

  @Test
  public void givenFixedPortAsServerPort_whenReadServerProps_thenGetThePort() {
    int port = serverProperties.getPort();
    assertEquals(8081, port);
  }
}
