package com.hxwl.app61210.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseInitConfig {

    @Bean
    CommandLineRunner initDatabase(JdbcTemplate jdbc) {
        return args -> {
            jdbc.execute("""
                CREATE TABLE IF NOT EXISTS zones (
                    id IDENTITY PRIMARY KEY,
                    name VARCHAR(120) NOT NULL UNIQUE,
                    description VARCHAR(500) DEFAULT ''
                )
                """);
            jdbc.execute("""
                CREATE TABLE IF NOT EXISTS exhibits (
                    id IDENTITY PRIMARY KEY,
                    code VARCHAR(80) NOT NULL,
                    name VARCHAR(160) NOT NULL,
                    zone VARCHAR(120) NOT NULL,
                    status VARCHAR(60) NOT NULL DEFAULT 'normal',
                    owner VARCHAR(120) NOT NULL
                )
                """);
            jdbc.execute("""
                CREATE TABLE IF NOT EXISTS inspections (
                    id IDENTITY PRIMARY KEY,
                    exhibit_id BIGINT NOT NULL,
                    inspected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    environment_note VARCHAR(500) DEFAULT '',
                    appearance_status VARCHAR(80) NOT NULL,
                    action_note VARCHAR(500) DEFAULT '',
                    abnormal BOOLEAN NOT NULL DEFAULT FALSE
                )
                """);
        };
    }
}
