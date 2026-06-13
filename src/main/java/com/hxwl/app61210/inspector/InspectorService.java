package com.hxwl.app61210.inspector;

import java.util.List;

public interface InspectorService {

    Inspector add(Inspector inspector);

    Inspector update(Inspector inspector);

    boolean delete(Long id);

    Inspector getById(Long id);

    boolean isEnabledInspector(Long id);

    List<Inspector> list(String name, String phone, String responsibleZone, Integer enabled);

    List<Inspector> listAllEnabled();
}
