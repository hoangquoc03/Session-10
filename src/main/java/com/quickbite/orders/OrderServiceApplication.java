package com.quickbite.orders;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OrderServiceApplication implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceApplication.class);

    private final JdbcTemplate jdbcTemplate;

    public OrderServiceApplication(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        String database = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
        log.info("Successfully connected to PostgreSQL database '{}'", database);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        String database = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
        return Map.of("status", "UP", "database", database);
    }
}
