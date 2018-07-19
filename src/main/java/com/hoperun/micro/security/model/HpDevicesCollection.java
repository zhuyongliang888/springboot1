package com.hoperun.micro.security.model;

public class HpDevicesCollection {
    private String collectId;

    private String deviceType;

    private String tenantId;

    private String name;

    private String metaDeviceId;

    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetaDeviceId() {
        return metaDeviceId;
    }

    public void setMetaDeviceId(String metaDeviceId) {
        this.metaDeviceId = metaDeviceId;
    }
}