package com.hxwl.app61210.inspector;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InspectorServiceImpl implements InspectorService {

    private final InspectorRepository inspectorRepository;

    public InspectorServiceImpl(InspectorRepository inspectorRepository) {
        this.inspectorRepository = inspectorRepository;
    }

    @Override
    public Inspector add(Inspector inspector) {
        validateRequired(inspector);
        LocalDateTime now = LocalDateTime.now();
        inspector.setCreateTime(now);
        inspector.setUpdateTime(now);
        if (inspector.getEnabled() == null) {
            inspector.setEnabled(1);
        }
        return inspectorRepository.insert(inspector);
    }

    @Override
    public Inspector update(Inspector inspector) {
        if (inspector.getId() == null || inspectorRepository.findById(inspector.getId()) == null) {
            throw new IllegalArgumentException("巡检人员不存在: id=" + inspector.getId());
        }
        inspector.setUpdateTime(LocalDateTime.now());
        inspectorRepository.updateById(inspector);
        return inspectorRepository.findById(inspector.getId());
    }

    @Override
    public boolean delete(Long id) {
        return inspectorRepository.deleteById(id) > 0;
    }

    @Override
    public Inspector getById(Long id) {
        return inspectorRepository.findById(id);
    }

    @Override
    public boolean isEnabledInspector(Long id) {
        Inspector inspector = inspectorRepository.findById(id);
        return inspector != null && inspector.getEnabled() != null && inspector.getEnabled() == 1;
    }

    @Override
    public List<Inspector> list(String name, String phone, String responsibleZone, Integer enabled) {
        return inspectorRepository.findAll(name, phone, responsibleZone, enabled);
    }

    @Override
    public List<Inspector> listAllEnabled() {
        return inspectorRepository.findAll(null, null, null, 1);
    }

    private void validateRequired(Inspector inspector) {
        if (inspector.getName() == null || inspector.getName().isBlank()) {
            throw new IllegalArgumentException("巡检人员姓名不能为空");
        }
    }
}
