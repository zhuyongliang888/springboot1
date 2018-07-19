package com.hoperun.micro.security.response;

public class GetAbnormalAlarmStatisticsResponseBody
{
    private String device_type_name;
    private Integer deivce_alarm_count;

    public String getDevice_type_name()
    {
	return device_type_name;
    }

    public void setDevice_type_name(String device_type_name)
    {
	this.device_type_name = device_type_name;
    }

    public Integer getDeivce_alarm_count()
    {
	return deivce_alarm_count;
    }

    public void setDeivce_alarm_count(Integer deivce_alarm_count)
    {
	this.deivce_alarm_count = deivce_alarm_count;
    }

}
