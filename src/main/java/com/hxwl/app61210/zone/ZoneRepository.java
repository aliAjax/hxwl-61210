package com.hxwl.app61210.zone;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.hxwl.app61210.zone.dto.ZoneCreate;
import com.hxwl.app61210.zone.dto.ZoneVO;

@Repository
public class ZoneRepository {

    private final JdbcTemplate jdbc;

    public ZoneRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public ZoneVO save(ZoneCreate zoneCreate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            var ps = con.prepareStatement(
                "INSERT INTO zones (name, description) VALUES (?, ?)",
                new String[]{"id"}
            );
            ps.setString(1, zoneCreate.name());
            ps.setString(2, zoneCreate.description() != null ? zoneCreate.description() : "");
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return findById(id).orElseThrow();
    }

    public Optional<ZoneVO> findById(Long id) {
        List<Map<String, Object>> results = jdbc.queryForList(
            """
            SELECT z.id, z.name, z.description,
                   (SELECT COUNT(*) FROM exhibits e WHERE e.zone = z.name) AS exhibit_count,
                   (SELECT COUNT(*) FROM exhibits e WHERE e.zone = z.name AND e.status = 'abnormal') AS abnormal_exhibit_count
            FROM zones z
            WHERE z.id = ?
            """,
            id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(mapToZoneVO(results.get(0)));
    }

    public Optional<ZoneVO> findByName(String name) {
        List<Map<String, Object>> results = jdbc.queryForList(
            """
            SELECT z.id, z.name, z.description,
                   (SELECT COUNT(*) FROM exhibits e WHERE e.zone = z.name) AS exhibit_count,
                   (SELECT COUNT(*) FROM exhibits e WHERE e.zone = z.name AND e.status = 'abnormal') AS abnormal_exhibit_count
            FROM zones z
            WHERE z.name = ?
            """,
            name
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(mapToZoneVO(results.get(0)));
    }

    public boolean existsByName(String name) {
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM zones WHERE name = ?",
            Integer.class,
            name
        );
        return count != null && count > 0;
    }

    public List<ZoneVO> findAll() {
        List<Map<String, Object>> results = jdbc.queryForList(
            """
            SELECT z.id, z.name, z.description,
                   (SELECT COUNT(*) FROM exhibits e WHERE e.zone = z.name) AS exhibit_count,
                   (SELECT COUNT(*) FROM exhibits e WHERE e.zone = z.name AND e.status = 'abnormal') AS abnormal_exhibit_count
            FROM zones z
            ORDER BY z.name
            """
        );
        return results.stream().map(this::mapToZoneVO).toList();
    }

    private ZoneVO mapToZoneVO(Map<String, Object> row) {
        return new ZoneVO(
            ((Number) row.get("id")).longValue(),
            (String) row.get("name"),
            (String) row.get("description"),
            ((Number) row.get("exhibit_count")).intValue(),
            ((Number) row.get("abnormal_exhibit_count")).intValue()
        );
    }
}
