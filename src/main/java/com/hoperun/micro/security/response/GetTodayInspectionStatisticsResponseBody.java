package com.hoperun.micro.security.response;

public class GetTodayInspectionStatisticsResponseBody
{
    private Integer fire_control_personnel_count;
    private Integer inspector_count;
    private Integer inspection_times;

    public Integer getFire_control_personnel_count()
    {
	return fire_control_personnel_count;
    }

    public void setFire_control_personnel_count(Integer fire_control_personnel_count)
    {
	this.fire_control_personnel_count = fire_control_personnel_count;
    }

    public Integer getInspector_count()
    {
	return inspector_count;
    }

    public void setInspector_count(Integer inspector_count)
    {
	this.inspector_count = inspector_count;
    }

    public Integer getInspection_times()
    {
	return inspection_times;
    }

    public void setInspection_times(Integer inspection_times)
    {
	this.inspection_times = inspection_times;
    }

}
