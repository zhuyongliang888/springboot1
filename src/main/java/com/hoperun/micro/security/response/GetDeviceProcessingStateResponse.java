package com.hoperun.micro.security.response;

import java.util.List;

public class GetDeviceProcessingStateResponse
{
    private String device_type;
    private String device_type_name;
    private Integer alarm_times;
    private Double min_value;
    private Double max_value;
    private List<GetDeviceProcessingStateResponseBody> alarm_list;
    public String getDevice_type()
    {
        return device_type;
    }
    public void setDevice_type(String device_type)
    {
        this.device_type = device_type;
    }
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
    public Double getMin_value()
    {
        return min_value;
    }
    public void setMin_value(Double min_value)
    {
        this.min_value = min_value;
    }
    public Double getMax_value()
    {
        return max_value;
    }
    public void setMax_value(Double max_value)
    {
        this.max_value = max_value;
    }
    public List<GetDeviceProcessingStateResponseBody> getAlarm_list()
    {
        return alarm_list;
    }
    public void setAlarm_list(List<GetDeviceProcessingStateResponseBody> alarm_list)
    {
        this.alarm_list = alarm_list;
    }
    @Override
    public String toString()
    {
	StringBuilder builder = new StringBuilder();
	builder.append("GetDeviceProcessingStateResponse [device_type=");
	builder.append(device_type);
	builder.append(", device_type_name=");
	builder.append(device_type_name);
	builder.append(", alarm_times=");
	builder.append(alarm_times);
	builder.append(", min_value=");
	builder.append(min_value);
	builder.append(", max_value=");
	builder.append(max_value);
	builder.append(", alarm_list=");
	builder.append(alarm_list);
	builder.append("]");
	return builder.toString();
    }
    
    

}
