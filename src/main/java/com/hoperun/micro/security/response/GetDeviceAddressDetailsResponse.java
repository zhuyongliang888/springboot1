package com.hoperun.micro.security.response;

public class GetDeviceAddressDetailsResponse
{
    private String device_address_url;
    private String location;
    private String building;
    private String floor;
    private String room;
    public String getDevice_address_url()
    {
        return device_address_url;
    }
    public void setDevice_address_url(String device_address_url)
    {
        this.device_address_url = device_address_url;
    }
    public String getLocation()
    {
        return location;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }
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
    public String getRoom()
    {
        return room;
    }
    public void setRoom(String room)
    {
        this.room = room;
    }
    @Override
    public String toString()
    {
	StringBuilder builder = new StringBuilder();
	builder.append("GetDeviceAddressDetailsResponse [device_address_url=");
	builder.append(device_address_url);
	builder.append(", location=");
	builder.append(location);
	builder.append(", building=");
	builder.append(building);
	builder.append(", floor=");
	builder.append(floor);
	builder.append(", room=");
	builder.append(room);
	builder.append("]");
	return builder.toString();
    }
    
    

}
