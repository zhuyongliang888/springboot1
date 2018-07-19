package com.hoperun.micro.security.response;

import java.util.List;

public class GetAbnormalAlarmTrendStatisticsResponseBody
{
    private String device_type__name;
    private List<FailureListResponseBody> failure_list;

    public String getDevice_type__name()
    {
	return device_type__name;
    }

    public void setDevice_type__name(String device_type__name)
    {
	this.device_type__name = device_type__name;
    }

    public List<FailureListResponseBody> getFailure_list()
    {
	return failure_list;
    }

    public void setFailure_list(List<FailureListResponseBody> failure_list)
    {
	this.failure_list = failure_list;
    }

}
