package com.hxwl.app61210.inspector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class InspectorRepository {

    private final JdbcTemplate jdbc;

    public InspectorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Inspector insert(Inspector inspector) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            var ps = con.prepareStatement(
                """
                INSERT INTO inspectors (name, phone, responsible_zone, enabled, create_time, update_time)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                new String[]{"id"}
            );
            ps.setString(1, inspector.getName());
            ps.setString(2, inspector.getPhone() != null ? inspector.getPhone() : "");
            ps.setString(3, inspector.getResponsibleZone() != null ? inspector.getResponsibleZone() : "");
            ps.setBoolean(4, inspector.getEnabled() == null || inspector.getEnabled() == 1);
            ps.setTimestamp(5, Timestamp.valueOf(inspector.getCreateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(inspector.getUpdateTime()));
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return findById(id);
    }

    public int updateById(Inspector inspector) {
        StringBuilder sql = new StringBuilder("UPDATE inspectors SET ");
        List<Object> args = new ArrayList<>();
        if (inspector.getName() != null) {
            sql.append("name = ?, ");
            args.add(inspector.getName());
        }
        if (inspector.getPhone() != null) {
            sql.append("phone = ?, ");
            args.add(inspector.getPhone());
        }
        if (inspector.getResponsibleZone() != null) {
            sql.append("responsible_zone = ?, ");
            args.add(inspector.getResponsibleZone());
        }
        if (inspector.getEnabled() != null) {
            sql.append("enabled = ?, ");
            args.add(inspector.getEnabled() == 1);
        }
        sql.append("update_time = ? WHERE id = ?");
        args.add(Timestamp.valueOf(inspector.getUpdateTime()));
        args.add(inspector.getId());
        return jdbc.update(sql.toString(), args.toArray());
    }

    public int deleteById(Long id) {
        return jdbc.update("DELETE FROM inspectors WHERE id = ?", id);
    }

    public Inspector findById(Long id) {
        List<Map<String, Object>> rows = jdbc.queryForList(
            "SELECT * FROM inspectors WHERE id = ?",
            id
        );
        return rows.isEmpty() ? null : mapToInspector(rows.get(0));
    }

    public List<Inspector> findAll(String name, String phone, String responsibleZone, Integer enabled) {
        StringBuilder sql = new StringBuilder("SELECT * FROM inspectors WHERE 1 = 1");
        List<Object> args = new ArrayList<>();
        if (name != null && !name.isBlank()) {
            sql.append(" AND name LIKE ?");
            args.add("%" + name + "%");
        }
        if (phone != null && !phone.isBlank()) {
            sql.append(" AND phone LIKE ?");
            args.add("%" + phone + "%");
        }
        if (responsibleZone != null && !responsibleZone.isBlank()) {
            sql.append(" AND responsible_zone LIKE ?");
            args.add("%" + responsibleZone + "%");
        }
        if (enabled != null) {
            sql.append(" AND enabled = ?");
            args.add(enabled == 1);
        }
        sql.append(" ORDER BY name ASC, id DESC");
        return jdbc.queryForList(sql.toString(), args.toArray()).stream()
            .map(this::mapToInspector)
            .toList();
    }

    private Inspector mapToInspector(Map<String, Object> row) {
        Inspector inspector = new Inspector();
        inspector.setId(((Number) row.get("ID")).longValue());
        inspector.setName((String) row.get("NAME"));
        inspector.setPhone((String) row.get("PHONE"));
        inspector.setResponsibleZone((String) row.get("RESPONSIBLE_ZONE"));
        inspector.setEnabled(Boolean.TRUE.equals(row.get("ENABLED")) ? 1 : 0);
        Timestamp createTime = (Timestamp) row.get("CREATE_TIME");
        Timestamp updateTime = (Timestamp) row.get("UPDATE_TIME");
        if (createTime != null) {
            inspector.setCreateTime(createTime.toLocalDateTime());
        }
        if (updateTime != null) {
            inspector.setUpdateTime(updateTime.toLocalDateTime());
        }
        return inspector;
    }
}
