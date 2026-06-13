package com.hxwl.app61210.zone;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hxwl.app61210.zone.dto.ZoneCreate;
import com.hxwl.app61210.zone.dto.ZoneVO;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public ZoneVO createZone(ZoneCreate zoneCreate) {
        if (zoneCreate.name() == null || zoneCreate.name().isBlank()) {
            throw new IllegalArgumentException("展区名称不能为空");
        }
        if (zoneRepository.existsByName(zoneCreate.name())) {
            throw new IllegalArgumentException("展区名称已存在: " + zoneCreate.name());
        }
        return zoneRepository.save(zoneCreate);
    }

    public List<ZoneVO> listZones() {
        return zoneRepository.findAll();
    }

    public ZoneVO getZoneById(Long id) {
        return zoneRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("展区不存在: id=" + id));
    }

    public boolean zoneExists(String name) {
        return zoneRepository.existsByName(name);
    }
}
