package com.dhanushmart.DhanushMart.controller;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseController {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db-status")
    public Map<String, Object> databaseStatus() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Map.of(
                "database", "MySQL",
                "status", result != null && result == 1 ? "CONNECTED" : "NOT_CONNECTED",
                "testQuery", "SELECT 1"
            );
        } catch (Exception e) {
            return Map.of(
                "database", "MySQL",
                "status", "NOT_CONNECTED",
                "error", e.getMessage() == null ? "Unknown database error" : e.getMessage()
            );
        }
    }
}
