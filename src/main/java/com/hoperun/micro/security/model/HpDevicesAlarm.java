package com.hoperun.micro.security.model;

public class HpDevicesAlarm {
    private Integer id;

    private String deviceType;

    private String serial;

    private String tenantId;

    private Integer attime;

    private Integer processtime;

    private Integer status;

    private String alarmVal;

    private Integer exceptType;

    private Integer exceptLevel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getAttime() {
        return attime;
    }

    public void setAttime(Integer attime) {
        this.attime = attime;
    }

    public Integer getProcesstime() {
        return processtime;
    }

    public void setProcesstime(Integer processtime) {
        this.processtime = processtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAlarmVal() {
        return alarmVal;
    }

    public void setAlarmVal(String alarmVal) {
        this.alarmVal = alarmVal;
    }

    public Integer getExceptType() {
        return exceptType;
    }

    public void setExceptType(Integer exceptType) {
        this.exceptType = exceptType;
    }

    public Integer getExceptLevel() {
        return exceptLevel;
    }

    public void setExceptLevel(Integer exceptLevel) {
        this.exceptLevel = exceptLevel;
    }
}