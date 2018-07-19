package com.hoperun.micro.security.model;

public class HpDevicesAlarmRules {
    private String ruleId;

    private String tenantId;

    private String deviceType;

    private Integer action;

    private Integer always;

    private Integer escalate;

    private String smsId;

    private Integer resendDuration;

    private Integer resendcount;

    private Double min;

    private Double max;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
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

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getAlways() {
        return always;
    }

    public void setAlways(Integer always) {
        this.always = always;
    }

    public Integer getEscalate() {
        return escalate;
    }

    public void setEscalate(Integer escalate) {
        this.escalate = escalate;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public Integer getResendDuration() {
        return resendDuration;
    }

    public void setResendDuration(Integer resendDuration) {
        this.resendDuration = resendDuration;
    }

    public Integer getResendcount() {
        return resendcount;
    }

    public void setResendcount(Integer resendcount) {
        this.resendcount = resendcount;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }
}