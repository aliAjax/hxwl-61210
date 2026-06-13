package com.hxwl.app61210.exhibit;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.hxwl.app61210.exhibit.dto.ExhibitCreate;
import com.hxwl.app61210.exhibit.dto.InspectionCreate;

@Repository
public class ExhibitRepository {

    private final JdbcTemplate jdbc;

    public ExhibitRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Map<String, Object> create(ExhibitCreate body) {
        jdbc.update(
            "INSERT INTO exhibits (code, name, zone, owner) VALUES (?, ?, ?, ?)",
            body.code(), body.name(), body.zone(), body.owner()
        );
        return jdbc.queryForMap("SELECT * FROM exhibits ORDER BY id DESC LIMIT 1");
    }

    public List<Map<String, Object>> findAll() {
        return jdbc.queryForList("SELECT * FROM exhibits ORDER BY zone, code");
    }

    public Map<String, Object> addInspection(long exhibitId, InspectionCreate body) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            var ps = con.prepareStatement(
                """
                INSERT INTO inspections
                (exhibit_id, environment_note, appearance_status, action_note, abnormal)
                VALUES (?, ?, ?, ?, ?)
                """,
                new String[]{"id"}
            );
            ps.setLong(1, exhibitId);
            ps.setString(2, body.environmentNote());
            ps.setString(3, body.appearanceStatus());
            ps.setString(4, body.actionNote());
            ps.setBoolean(5, body.abnormal());
            return ps;
        }, keyHolder);

        jdbc.update(
            "UPDATE exhibits SET status = ? WHERE id = ?",
            body.abnormal() ? "abnormal" : "normal",
            exhibitId
        );

        Long inspectionId = keyHolder.getKey().longValue();
        return jdbc.queryForMap("SELECT * FROM inspections WHERE id = ?", inspectionId);
    }

    public List<Map<String, Object>> findInspectionsByExhibitId(long exhibitId) {
        return jdbc.queryForList(
            "SELECT * FROM inspections WHERE exhibit_id = ? ORDER BY inspected_at DESC, id DESC",
            exhibitId
        );
    }

    public List<Map<String, Object>> findLatestAbnormal() {
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

    public boolean existsById(long id) {
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM exhibits WHERE id = ?",
            Integer.class,
            id
        );
        return count != null && count > 0;
    }
}
