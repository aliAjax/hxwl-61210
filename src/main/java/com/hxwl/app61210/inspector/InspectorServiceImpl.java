package com.hxwl.app61210.inspector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InspectorServiceImpl implements InspectorService {

    @Autowired
    private InspectorMapper inspectorMapper;

    @Override
    public Inspector add(Inspector inspector) {
        LocalDateTime now = LocalDateTime.now();
        inspector.setCreateTime(now);
        inspector.setUpdateTime(now);
        if (inspector.getEnabled() == null) {
            inspector.setEnabled(1);
        }
        inspectorMapper.insert(inspector);
        return inspector;
    }

    @Override
    public Inspector update(Inspector inspector) {
        inspector.setUpdateTime(LocalDateTime.now());
        inspectorMapper.updateById(inspector);
        return inspectorMapper.selectById(inspector.getId());
    }

    @Override
    public boolean delete(Long id) {
        return inspectorMapper.deleteById(id) > 0;
    }

    @Override
    public Inspector getById(Long id) {
        return inspectorMapper.selectById(id);
    }

    @Override
    public List<Inspector> list(String name, String phone, String responsibleZone, Integer enabled) {
        return inspectorMapper.selectList(name, phone, responsibleZone, enabled);
    }

    @Override
    public List<Inspector> listAllEnabled() {
        return inspectorMapper.selectAllEnabled();
    }
}
