package com.bike.config;

import com.bike.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import org.springframework.context.annotation.Bean;

/** TODO - currently not in use */
// @Configuration
public class JacksonConfig {
  @Bean
  public ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    mapper.registerModule(new JavaTimeModule());
    mapper.registerSubtypes(User.class);
    return mapper;
  }
}
