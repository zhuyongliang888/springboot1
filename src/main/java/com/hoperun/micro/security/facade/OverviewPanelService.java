package com.hoperun.micro.security.facade;

import java.util.List;

import com.hoperun.micro.security.request.CommonDateRequestBody;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.GetAbnormalAlarmStatisticsResponseBody;
import com.hoperun.micro.security.response.GetAbnormalAlarmTrendStatisticsResponseBody;
import com.hoperun.micro.security.response.GetAlarmOverviewStatisticsResponseBody;
import com.hoperun.micro.security.response.GetDeviceStatusStatisticsResponseBody;
import com.hoperun.micro.security.response.GetFireSituationyStatisticsResponseBody;
import com.hoperun.micro.security.response.GetStateSummaryResponseBody;
import com.hoperun.micro.security.response.GetTodayInspectionStatisticsResponseBody;

public interface OverviewPanelService
{
    public CommonResponseOfNoPage<GetStateSummaryResponseBody> getStateSummary(CommonDateRequestBody requestBody,
	    String userId);

    public CommonResponseOfNoPage<GetAlarmOverviewStatisticsResponseBody> getAlarmOverviewStatistics(String userId);

    public CommonResponseOfNoPage<List<GetAbnormalAlarmStatisticsResponseBody>> getAbnormalAlarmStatistics(
	    CommonDateRequestBody requestBody, String userId);

    public CommonResponseOfNoPage<List<GetFireSituationyStatisticsResponseBody>> getFireSituationyStatistics(
	    String userId);

    public CommonResponseOfNoPage<GetTodayInspectionStatisticsResponseBody> getTodayInspectionStatistics(String userId);

    public CommonResponseOfNoPage<List<GetDeviceStatusStatisticsResponseBody>> getDeviceStatusStatistics(String userId);

    public CommonResponseOfNoPage<List<GetAbnormalAlarmTrendStatisticsResponseBody>> getAbnormalAlarmTrendStatistics(
	    CommonDateRequestBody requestBody, String userId);
}
