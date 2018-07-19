package com.hoperun.micro.security.response;

import java.util.List;

public class GetAlarmTypeDistributionResponse
{
    private String device_type_name;
    private Integer alarm_times;
    private List<GetAlarmTypeDistributionResponseBody> failure_list;
    public String getDevice_type_name()
    {
        return device_type_name;
    }
    public void setDevice_type_name(String device_type_name)
    {
        this.device_type_name = device_type_name;
    }
    public Integer getAlarm_times()
    {
        return alarm_times;
    }
    public void setAlarm_times(Integer alarm_times)
    {
        this.alarm_times = alarm_times;
    }
    public List<GetAlarmTypeDistributionResponseBody> getFailure_list()
    {
        return failure_list;
    }
    public void setFailure_list(List<GetAlarmTypeDistributionResponseBody> failure_list)
    {
        this.failure_list = failure_list;
    }
    @Override
    public String toString()
    {
	StringBuilder builder = new StringBuilder();
	builder.append("GetAlarmTypeDistributionResponse [device_type_name=");
	builder.append(device_type_name);
	builder.append(", alarm_times=");
	builder.append(alarm_times);
	builder.append(", failure_list=");
	builder.append(failure_list);
	builder.append("]");
	return builder.toString();
    }
    

}
