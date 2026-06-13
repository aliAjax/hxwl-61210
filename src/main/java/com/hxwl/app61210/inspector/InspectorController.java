package com.hxwl.app61210.inspector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inspector")
public class InspectorController {

    @Autowired
    private InspectorService inspectorService;

    @PostMapping
    public Map<String, Object> add(@RequestBody Inspector inspector) {
        Map<String, Object> result = new HashMap<>();
        try {
            Inspector saved = inspectorService.add(inspector);
            result.put("success", true);
            result.put("data", saved);
            result.put("message", "添加成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加失败：" + e.getMessage());
        }
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody Inspector inspector) {
        Map<String, Object> result = new HashMap<>();
        try {
            inspector.setId(id);
            Inspector updated = inspectorService.update(inspector);
            result.put("success", true);
            result.put("data", updated);
            result.put("message", "更新成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败：" + e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = inspectorService.delete(id);
            result.put("success", deleted);
            result.put("message", deleted ? "删除成功" : "删除失败，记录不存在");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Inspector inspector = inspectorService.getById(id);
            result.put("success", true);
            result.put("data", inspector);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String phone,
                                    @RequestParam(required = false) String responsibleZone,
                                    @RequestParam(required = false) Integer enabled) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Inspector> list = inspectorService.list(name, phone, responsibleZone, enabled);
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/enabled")
    public Map<String, Object> listEnabled() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Inspector> list = inspectorService.listAllEnabled();
            result.put("success", true);
            result.put("data", list);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
        }
        return result;
    }
}
