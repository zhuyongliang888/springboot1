package com.hoperun.micro.security.response;

import java.util.List;

public class GetAllTerminalDevicesAndStatusOfFloorResponseBody
{
    private String device_type_name;
    private Integer total_count;
    private Integer faulty_count;
    private List<DeviceListResponseBody> device_list;

    public String getDevice_type_name()
    {
	return device_type_name;
    }

    public void setDevice_type_name(String device_type_name)
    {
	this.device_type_name = device_type_name;
    }

    public Integer getTotal_count()
    {
	return total_count;
    }

    public void setTotal_count(Integer total_count)
    {
	this.total_count = total_count;
    }

    public Integer getFaulty_count()
    {
	return faulty_count;
    }

    public void setFaulty_count(Integer faulty_count)
    {
	this.faulty_count = faulty_count;
    }

    public List<DeviceListResponseBody> getDevice_list()
    {
	return device_list;
    }

    public void setDevice_list(List<DeviceListResponseBody> device_list)
    {
	this.device_list = device_list;
    }

}
