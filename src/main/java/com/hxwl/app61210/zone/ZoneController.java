package com.hxwl.app61210.zone;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hxwl.app61210.zone.dto.ZoneCreate;
import com.hxwl.app61210.zone.dto.ZoneVO;

@RestController
@RequestMapping("/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public ZoneVO create(@RequestBody ZoneCreate body) {
        return zoneService.createZone(body);
    }

    @GetMapping
    public List<ZoneVO> list() {
        return zoneService.listZones();
    }

    @GetMapping("/{id}")
    public ZoneVO getById(@PathVariable Long id) {
        return zoneService.getZoneById(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
}
