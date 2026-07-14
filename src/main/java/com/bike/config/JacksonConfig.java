package com.bike.config;

import java.text.SimpleDateFormat;

import com.bike.model.User;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
/** TODO - currently not in use */
//@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        mapper.registerModule(new JavaTimeModule());
        mapper.registerSubtypes(User.class);
        return mapper;
    }
}
