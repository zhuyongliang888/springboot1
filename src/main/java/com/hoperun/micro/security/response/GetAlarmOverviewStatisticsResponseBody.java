package com.hoperun.micro.security.response;

public class GetAlarmOverviewStatisticsResponseBody
{
    private Integer all_alarm_count;
    private Integer processed_alarm_count;
    private Integer processing_alarm_count;
    private Integer unprocessed_alarm_count;

    public Integer getAll_alarm_count()
    {
	return all_alarm_count;
    }

    public void setAll_alarm_count(Integer all_alarm_count)
    {
	this.all_alarm_count = all_alarm_count;
    }

    public Integer getProcessed_alarm_count()
    {
	return processed_alarm_count;
    }

    public void setProcessed_alarm_count(Integer processed_alarm_count)
    {
	this.processed_alarm_count = processed_alarm_count;
    }

    public Integer getProcessing_alarm_count()
    {
	return processing_alarm_count;
    }

    public void setProcessing_alarm_count(Integer processing_alarm_count)
    {
	this.processing_alarm_count = processing_alarm_count;
    }

    public Integer getUnprocessed_alarm_count()
    {
	return unprocessed_alarm_count;
    }

    public void setUnprocessed_alarm_count(Integer unprocessed_alarm_count)
    {
	this.unprocessed_alarm_count = unprocessed_alarm_count;
    }

}
