package com.hoperun.micro.security.model;

public class HpDevicesMaintainsEmployee {
    private String deviceMeId;

    private String tenantId;

    private String deviceType;

    private String employeeId;

    private Integer action;

    public String getDeviceMeId() {
        return deviceMeId;
    }

    public void setDeviceMeId(String deviceMeId) {
        this.deviceMeId = deviceMeId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}