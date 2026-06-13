package com.hxwl.app61210.inspector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巡检人员集成辅助工具类
 * 用于在巡检记录查询接口中辅助填充巡检人员信息
 */
@Component
public class InspectorIntegrationHelper {

    @Autowired
    private InspectorService inspectorService;

    /**
     * 根据巡检人员ID获取简化的巡检人员信息
     * 当ID为null时返回null，兼容历史旧记录
     *
     * @param inspectorId 巡检人员ID（可为null）
     * @return 巡检人员信息或null
     */
    public InspectorInfo getInspectorInfo(Long inspectorId) {
        if (inspectorId == null) {
            return null;
        }
        Inspector inspector = inspectorService.getById(inspectorId);
        if (inspector == null) {
            return null;
        }
        return new InspectorInfo(inspector);
    }

    /**
     * 批量获取巡检人员信息
     * 用于批量查询巡检记录时一次性加载所有巡检人员，避免N+1查询问题
     *
     * @param inspectorIds 巡检人员ID列表（可含null）
     * @return ID -> InspectorInfo 的映射，不含null键
     */
    public Map<Long, InspectorInfo> getInspectorInfoMap(List<Long> inspectorIds) {
        Map<Long, InspectorInfo> result = new HashMap<>();
        if (inspectorIds == null || inspectorIds.isEmpty()) {
            return result;
        }
        List<Long> validIds = new ArrayList<>();
        for (Long id : inspectorIds) {
            if (id != null && !result.containsKey(id)) {
                validIds.add(id);
            }
        }
        for (Long id : validIds) {
            InspectorInfo info = getInspectorInfo(id);
            if (info != null) {
                result.put(id, info);
            }
        }
        return result;
    }

    /**
     * 校验巡检人员ID是否有效（当传入ID不为null时）
     * 用于创建巡检记录时校验
     *
     * @param inspectorId 巡检人员ID（可为null）
     * @return true-有效（含null情况），false-无效
     */
    public boolean validateInspectorId(Long inspectorId) {
        if (inspectorId == null) {
            return true;
        }
        Inspector inspector = inspectorService.getById(inspectorId);
        return inspector != null && inspector.getEnabled() != null && inspector.getEnabled() == 1;
    }
}
