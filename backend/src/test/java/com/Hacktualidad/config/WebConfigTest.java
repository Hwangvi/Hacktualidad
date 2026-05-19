package com.Hacktualidad.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void resourceHandlerMappingShouldBeConfigured() {
        Map<String, SimpleUrlHandlerMapping> beans = applicationContext.getBeansOfType(SimpleUrlHandlerMapping.class);

        boolean hasUploadsHandler = beans.values().stream()
                .anyMatch(mapping -> mapping.getUrlMap().containsKey("/uploads/**"));

        assertThat(hasUploadsHandler).as("Debe existir un handler para /uploads/**").isTrue();
    }
}