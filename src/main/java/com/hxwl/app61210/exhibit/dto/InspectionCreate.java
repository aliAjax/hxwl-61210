package com.hxwl.app61210.exhibit.dto;

public record InspectionCreate(
    String environmentNote,
    String appearanceStatus,
    String actionNote,
    boolean abnormal,
    Long inspectorId
) {}
