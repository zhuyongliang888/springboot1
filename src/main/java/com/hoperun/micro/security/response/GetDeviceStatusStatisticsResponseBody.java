package com.hoperun.micro.security.response;

public class GetDeviceStatusStatisticsResponseBody
{
    private String device_type_name;
    private Integer device_failure_times;
    private Float device_good_rate;

    public String getDevice_type_name()
    {
	return device_type_name;
    }

    public void setDevice_type_name(String device_type_name)
    {
	this.device_type_name = device_type_name;
    }

    public Integer getDevice_failure_times()
    {
	return device_failure_times;
    }

    public void setDevice_failure_times(Integer device_failure_times)
    {
	this.device_failure_times = device_failure_times;
    }

    public Float getDevice_good_rate()
    {
	return device_good_rate;
    }

    public void setDevice_good_rate(Float device_good_rate)
    {
	this.device_good_rate = device_good_rate;
    }

}
