package com.hxwl.app61210;

import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Hxwl61210Application {
    public static void main(String[] args) {
        SpringApplication.run(Hxwl61210Application.class, args);
    }

    @RestController
    static class ExhibitController {
        private final JdbcTemplate jdbc;

        ExhibitController(JdbcTemplate jdbc) {
            this.jdbc = jdbc;
            this.jdbc.execute("""
                CREATE TABLE IF NOT EXISTS exhibits (
                    id IDENTITY PRIMARY KEY,
                    code VARCHAR(80) NOT NULL,
                    name VARCHAR(160) NOT NULL,
                    zone VARCHAR(120) NOT NULL,
                    status VARCHAR(60) NOT NULL DEFAULT 'normal',
                    owner VARCHAR(120) NOT NULL
                )
                """);
            this.jdbc.execute("""
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
        }

        @GetMapping("/health")
        Map<String, Object> health() {
            return Map.of("status", "ok", "port", 61210);
        }

        @PostMapping("/exhibits")
        Map<String, Object> create(@RequestBody ExhibitCreate body) {
            jdbc.update(
                "INSERT INTO exhibits (code, name, zone, owner) VALUES (?, ?, ?, ?)",
                body.code(), body.name(), body.zone(), body.owner()
            );
            return jdbc.queryForMap("SELECT * FROM exhibits ORDER BY id DESC LIMIT 1");
        }

        @GetMapping("/exhibits")
        List<Map<String, Object>> list() {
            return jdbc.queryForList("SELECT * FROM exhibits ORDER BY zone, code");
        }

        @PostMapping("/exhibits/{id}/inspections")
        Map<String, Object> inspect(@PathVariable long id, @RequestBody InspectionCreate body) {
            jdbc.update(
                """
                INSERT INTO inspections
                (exhibit_id, environment_note, appearance_status, action_note, abnormal)
                VALUES (?, ?, ?, ?, ?)
                """,
                id, body.environmentNote(), body.appearanceStatus(), body.actionNote(), body.abnormal()
            );
            jdbc.update(
                "UPDATE exhibits SET status = ? WHERE id = ?",
                body.abnormal() ? "abnormal" : "normal",
                id
            );
            return jdbc.queryForMap("SELECT * FROM inspections ORDER BY id DESC LIMIT 1");
        }

        @GetMapping("/exhibits/{id}/inspections")
        List<Map<String, Object>> inspections(@PathVariable long id) {
            return jdbc.queryForList(
                "SELECT * FROM inspections WHERE exhibit_id = ? ORDER BY inspected_at DESC, id DESC",
                id
            );
        }

        @GetMapping("/inspections/abnormal/latest")
        List<Map<String, Object>> latestAbnormal() {
            return jdbc.queryForList(
                """
                SELECT e.code, e.name, e.zone, e.owner, i.*
                FROM inspections i
                JOIN exhibits e ON e.id = i.exhibit_id
                WHERE i.abnormal = TRUE
                ORDER BY i.inspected_at DESC, i.id DESC
                """
            );
        }
    }

    record ExhibitCreate(String code, String name, String zone, String owner) {}
    record InspectionCreate(String environmentNote, String appearanceStatus, String actionNote, boolean abnormal) {}
}
