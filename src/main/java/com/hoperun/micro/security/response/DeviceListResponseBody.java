package com.hoperun.micro.security.response;

public class DeviceListResponseBody
{
    private String device_id;
    private String device_address;
    private Integer device_state;

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

    public Integer getDevice_state()
    {
	return device_state;
    }

    public void setDevice_state(Integer device_state)
    {
	this.device_state = device_state;
    }

}
