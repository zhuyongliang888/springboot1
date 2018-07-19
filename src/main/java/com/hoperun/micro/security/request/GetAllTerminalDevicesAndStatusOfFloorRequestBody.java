package com.hoperun.micro.security.request;

public class GetAllTerminalDevicesAndStatusOfFloorRequestBody
{
    private String building;
    private String floor;
    private String device_type;

    public String getBuilding()
    {
	return building;
    }

    public void setBuilding(String building)
    {
	this.building = building;
    }

    public String getFloor()
    {
	return floor;
    }

    public void setFloor(String floor)
    {
	this.floor = floor;
    }

    public String getDevice_type()
    {
	return device_type;
    }

    public void setDevice_type(String device_type)
    {
	this.device_type = device_type;
    }

    @Override
    public String toString()
    {
	return "GetAllTerminalDevicesAndStatusOfFloorRequestBody [building=" + building + ", floor=" + floor
		+ ", device_type=" + device_type + "]";
    }

}
