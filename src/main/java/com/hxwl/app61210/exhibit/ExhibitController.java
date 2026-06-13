package com.hxwl.app61210.exhibit;

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

import com.hxwl.app61210.exhibit.dto.ExhibitCreate;
import com.hxwl.app61210.exhibit.dto.InspectionCreate;

@RestController
@RequestMapping
public class ExhibitController {

    private final ExhibitService exhibitService;

    public ExhibitController(ExhibitService exhibitService) {
        this.exhibitService = exhibitService;
    }

    @GetMapping("/health")
    Map<String, Object> health() {
        return Map.of("status", "ok", "port", 61210);
    }

    @PostMapping("/exhibits")
    Map<String, Object> create(@RequestBody ExhibitCreate body) {
        return exhibitService.createExhibit(body);
    }

    @GetMapping("/exhibits")
    List<Map<String, Object>> list() {
        return exhibitService.listExhibits();
    }

    @PostMapping("/exhibits/{id}/inspections")
    Map<String, Object> inspect(@PathVariable long id, @RequestBody InspectionCreate body) {
        return exhibitService.addInspection(id, body);
    }

    @GetMapping("/exhibits/{id}/inspections")
    List<Map<String, Object>> inspections(@PathVariable long id) {
        return exhibitService.listInspections(id);
    }

    @GetMapping("/inspections/abnormal/latest")
    List<Map<String, Object>> latestAbnormal() {
        return exhibitService.listLatestAbnormal();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }
}
