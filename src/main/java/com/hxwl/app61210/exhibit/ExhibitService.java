package com.hxwl.app61210.exhibit;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hxwl.app61210.exhibit.dto.ExhibitCreate;
import com.hxwl.app61210.exhibit.dto.InspectionCreate;
import com.hxwl.app61210.zone.ZoneService;

@Service
public class ExhibitService {

    private final ExhibitRepository exhibitRepository;
    private final ZoneService zoneService;

    public ExhibitService(ExhibitRepository exhibitRepository, ZoneService zoneService) {
        this.exhibitRepository = exhibitRepository;
        this.zoneService = zoneService;
    }

    public Map<String, Object> createExhibit(ExhibitCreate body) {
        if (body.zone() == null || body.zone().isBlank()) {
            throw new IllegalArgumentException("展区不能为空");
        }
        if (!zoneService.zoneExists(body.zone())) {
            throw new IllegalArgumentException("展区不存在: " + body.zone());
        }
        if (body.code() == null || body.code().isBlank()) {
            throw new IllegalArgumentException("展品编号不能为空");
        }
        if (body.name() == null || body.name().isBlank()) {
            throw new IllegalArgumentException("展品名称不能为空");
        }
        if (body.owner() == null || body.owner().isBlank()) {
            throw new IllegalArgumentException("负责人不能为空");
        }
        return exhibitRepository.create(body);
    }

    public List<Map<String, Object>> listExhibits() {
        return exhibitRepository.findAll();
    }

    public Map<String, Object> addInspection(long exhibitId, InspectionCreate body) {
        if (!exhibitRepository.existsById(exhibitId)) {
            throw new IllegalArgumentException("展品不存在: id=" + exhibitId);
        }
        if (body.appearanceStatus() == null || body.appearanceStatus().isBlank()) {
            throw new IllegalArgumentException("外观状态不能为空");
        }
        return exhibitRepository.addInspection(exhibitId, body);
    }

    public List<Map<String, Object>> listInspections(long exhibitId) {
        if (!exhibitRepository.existsById(exhibitId)) {
            throw new IllegalArgumentException("展品不存在: id=" + exhibitId);
        }
        return exhibitRepository.findInspectionsByExhibitId(exhibitId);
    }

    public List<Map<String, Object>> listLatestAbnormal() {
        return exhibitRepository.findLatestAbnormal();
    }
}
