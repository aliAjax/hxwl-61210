package com.hxwl.app61210.inspector;

import java.io.Serializable;

public class InspectorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String phone;

    private String responsibleZone;

    public InspectorInfo() {
    }

    public InspectorInfo(Inspector inspector) {
        if (inspector != null) {
            this.id = inspector.getId();
            this.name = inspector.getName();
            this.phone = inspector.getPhone();
            this.responsibleZone = inspector.getResponsibleZone();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getResponsibleZone() {
        return responsibleZone;
    }

    public void setResponsibleZone(String responsibleZone) {
        this.responsibleZone = responsibleZone;
    }
}
