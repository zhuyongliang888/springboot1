package com.hoperun.micro.security.response;

public class GetDeviceProcessingStateResponseBody
{
    private String device_id;
    private String device_address;
    private String alarm_info;
    private Integer alarm_date;
    private Integer process_date;
    private Integer alarm_state;
    private Integer alarm_id;
    private String alarm_val;

    public String getDevice_id()
    {
	return device_id;
    }

    public void setDevice_id(String device_id)
    {
	this.device_id = device_id;
    }

    public String getDevice_address()
    {
	return device_address;
    }

    public void setDevice_address(String device_address)
    {
	this.device_address = device_address;
    }

    public String getAlarm_info()
    {
	return alarm_info;
    }

    public void setAlarm_info(String alarm_info)
    {
	this.alarm_info = alarm_info;
    }

    public Integer getAlarm_date()
    {
	return alarm_date;
    }

    public void setAlarm_date(Integer alarm_date)
    {
	this.alarm_date = alarm_date;
    }

    public Integer getProcess_date()
    {
	return process_date;
    }

    public void setProcess_date(Integer process_date)
    {
	this.process_date = process_date;
    }

    public Integer getAlarm_state()
    {
	return alarm_state;
    }

    public void setAlarm_state(Integer alarm_state)
    {
	this.alarm_state = alarm_state;
    }

    public Integer getAlarm_id()
    {
	return alarm_id;
    }

    public void setAlarm_id(Integer alarm_id)
    {
	this.alarm_id = alarm_id;
    }

    public String getAlarm_val()
    {
	return alarm_val;
    }

    public void setAlarm_val(String alarm_val)
    {
	this.alarm_val = alarm_val;
    }

    @Override
    public String toString()
    {
	StringBuilder builder = new StringBuilder();
	builder.append("GetDeviceProcessingStateResponseBody [device_id=");
	builder.append(device_id);
	builder.append(", device_address=");
	builder.append(device_address);
	builder.append(", alarm_info=");
	builder.append(alarm_info);
	builder.append(", alarm_date=");
	builder.append(alarm_date);
	builder.append(", process_date=");
	builder.append(process_date);
	builder.append(", alarm_state=");
	builder.append(alarm_state);
	builder.append(", alarm_id=");
	builder.append(alarm_id);
	builder.append(", alarm_val=");
	builder.append(alarm_val);
	builder.append("]");
	return builder.toString();
    }
}
