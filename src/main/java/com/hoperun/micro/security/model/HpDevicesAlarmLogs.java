package com.hoperun.micro.security.model;

import java.util.Date;

public class HpDevicesAlarmLogs {
    private String logId;

    private String tenantId;

    private String deviceType;

    private String serial;

    private Date attime;

    private String content;

    private Integer status;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getAttime() {
        return attime;
    }

    public void setAttime(Date attime) {
        this.attime = attime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}