package com.hoperun.micro.security.response;

public class GetStateSummaryResponseBody
{
    private Integer security_rating;
    private Float device_good_rate;
    private Integer week_alarm_times;

    public Integer getSecurity_rating()
    {
	return security_rating;
    }

    public void setSecurity_rating(Integer security_rating)
    {
	this.security_rating = security_rating;
    }

    public Float getDevice_good_rate()
    {
	return device_good_rate;
    }

    public void setDevice_good_rate(Float device_good_rate)
    {
	this.device_good_rate = device_good_rate;
    }

    public Integer getWeek_alarm_times()
    {
	return week_alarm_times;
    }

    public void setWeek_alarm_times(Integer week_alarm_times)
    {
	this.week_alarm_times = week_alarm_times;
    }

    @Override
    public String toString()
    {
	return "GetStateSummaryResponseBody [security_rating=" + security_rating + ", device_good_rate="
		+ device_good_rate + ", week_alarm_times=" + week_alarm_times + "]";
    }

}
