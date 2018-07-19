package com.hoperun.micro.security.response;

public class GetAlarmTypeDistributionResponseBody
{
    private Integer device_failure_times;
    private Integer alarm_date;
    public Integer getDevice_failure_times()
    {
        return device_failure_times;
    }
    public void setDevice_failure_times(Integer device_failure_times)
    {
        this.device_failure_times = device_failure_times;
    }
    public Integer getAlarm_date()
    {
        return alarm_date;
    }
    public void setAlarm_date(Integer alarm_date)
    {
        this.alarm_date = alarm_date;
    }
    @Override
    public String toString()
    {
	StringBuilder builder = new StringBuilder();
	builder.append("GetAlarmTypeDistributionResponseBody [device_failure_times=");
	builder.append(device_failure_times);
	builder.append(", alarm_date=");
	builder.append(alarm_date);
	builder.append("]");
	return builder.toString();
    }
    
}
