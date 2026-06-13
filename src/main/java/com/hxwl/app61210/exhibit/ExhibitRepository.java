package com.hxwl.app61210.exhibit;

import java.util.LinkedHashMap;
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
                (exhibit_id, inspector_id, environment_note, appearance_status, action_note, abnormal)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                new String[]{"id"}
            );
            ps.setLong(1, exhibitId);
            ps.setObject(2, body.inspectorId());
            ps.setString(3, body.environmentNote());
            ps.setString(4, body.appearanceStatus());
            ps.setString(5, body.actionNote());
            ps.setBoolean(6, body.abnormal());
            return ps;
        }, keyHolder);

        jdbc.update(
            "UPDATE exhibits SET status = ? WHERE id = ?",
            body.abnormal() ? "abnormal" : "normal",
            exhibitId
        );

        Long inspectionId = keyHolder.getKey().longValue();
        return withInspector(jdbc.queryForMap(
            """
            SELECT i.*,
                   ins.id AS "inspectorInfoId",
                   ins.name AS "inspectorName",
                   ins.phone AS "inspectorPhone",
                   ins.responsible_zone AS "inspectorResponsibleZone"
            FROM inspections i
            LEFT JOIN inspectors ins ON ins.id = i.inspector_id
            WHERE i.id = ?
            """,
            inspectionId
        ));
    }

    public List<Map<String, Object>> findInspectionsByExhibitId(long exhibitId) {
        return jdbc.queryForList(
            """
            SELECT i.*,
                   ins.id AS "inspectorInfoId",
                   ins.name AS "inspectorName",
                   ins.phone AS "inspectorPhone",
                   ins.responsible_zone AS "inspectorResponsibleZone"
            FROM inspections i
            LEFT JOIN inspectors ins ON ins.id = i.inspector_id
            WHERE i.exhibit_id = ?
            ORDER BY i.inspected_at DESC, i.id DESC
            """,
            exhibitId
        ).stream().map(this::withInspector).toList();
    }

    public List<Map<String, Object>> findLatestAbnormal() {
        return jdbc.queryForList(
            """
            SELECT e.code, e.name, e.zone, e.owner, i.*,
                   ins.id AS "inspectorInfoId",
                   ins.name AS "inspectorName",
                   ins.phone AS "inspectorPhone",
                   ins.responsible_zone AS "inspectorResponsibleZone"
            FROM inspections i
            JOIN exhibits e ON e.id = i.exhibit_id
            LEFT JOIN inspectors ins ON ins.id = i.inspector_id
            WHERE i.abnormal = TRUE
            ORDER BY i.inspected_at DESC, i.id DESC
            """
        ).stream().map(this::withInspector).toList();
    }

    public boolean existsById(long id) {
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM exhibits WHERE id = ?",
            Integer.class,
            id
        );
        return count != null && count > 0;
    }

    private Map<String, Object> withInspector(Map<String, Object> row) {
        Map<String, Object> result = new LinkedHashMap<>(row);
        Object inspectorId = result.remove("inspectorInfoId");
        Object inspectorName = result.remove("inspectorName");
        Object inspectorPhone = result.remove("inspectorPhone");
        Object inspectorResponsibleZone = result.remove("inspectorResponsibleZone");
        if (inspectorId == null) {
            result.put("inspector", null);
            return result;
        }
        Map<String, Object> inspector = new LinkedHashMap<>();
        inspector.put("id", ((Number) inspectorId).longValue());
        inspector.put("name", inspectorName);
        inspector.put("phone", inspectorPhone);
        inspector.put("responsibleZone", inspectorResponsibleZone);
        result.put("inspector", inspector);
        return result;
    }
}
