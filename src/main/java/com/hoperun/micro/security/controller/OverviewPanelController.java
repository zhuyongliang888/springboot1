package com.hoperun.micro.security.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hoperun.micro.security.common.RedisUtils;
import com.hoperun.micro.security.facade.OverviewPanelService;
import com.hoperun.micro.security.request.CommonDateRequestBody;
import com.hoperun.micro.security.request.CommonRequestOfData;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.GetAbnormalAlarmStatisticsResponseBody;
import com.hoperun.micro.security.response.GetAbnormalAlarmTrendStatisticsResponseBody;
import com.hoperun.micro.security.response.GetAlarmOverviewStatisticsResponseBody;
import com.hoperun.micro.security.response.GetDeviceStatusStatisticsResponseBody;
import com.hoperun.micro.security.response.GetFireSituationyStatisticsResponseBody;
import com.hoperun.micro.security.response.GetStateSummaryResponseBody;
import com.hoperun.micro.security.response.GetTodayInspectionStatisticsResponseBody;

@RestController
public class OverviewPanelController
{
    private final static Logger LOGGER = LoggerFactory.getLogger(OverviewPanelController.class);

    @Autowired
    private OverviewPanelService overviewPanelService;
    @Autowired
    private RedisUtils redisUtils;

//    private final String userId = "c4592b8027444333b50a46169528c991";

    /**
     * 状态汇总
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/overview/summary", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getStateSummary(@RequestBody CommonRequestOfData<CommonDateRequestBody> commonRequestOfData,
	    HttpServletRequest request, HttpServletResponse response)
    {
	LOGGER.debug("getStateSummary controller start...");
	String accessToken = request.getHeader("Access-Token");
	String userId = redisUtils.getAdminIdByAccessToken(accessToken);
	CommonDateRequestBody requestBody = commonRequestOfData.getData();
	CommonResponseOfNoPage<GetStateSummaryResponseBody> responseOfNoPage = overviewPanelService
		.getStateSummary(requestBody, userId);
	if (responseOfNoPage.getCode() != 0)
	{
	    response.setStatus(responseOfNoPage.getCode() / 10000);
	}
	String result = JSON.toJSONString(responseOfNoPage, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getStateSummary controller end...");
	return result;
    }

    /**
     * 告警概况
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/overview/alarm_overview", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String getAlarmOverviewStatistics(HttpServletRequest request, HttpServletResponse response)
    {
	LOGGER.debug("getAlarmOverviewStatistics controller start...");
	String accessToken = request.getHeader("Access-Token");
	String userId = redisUtils.getAdminIdByAccessToken(accessToken);
	CommonResponseOfNoPage<GetAlarmOverviewStatisticsResponseBody> responseOfNoPage = overviewPanelService
		.getAlarmOverviewStatistics(userId);
	if (responseOfNoPage.getCode() != 0)
	{
	    response.setStatus(responseOfNoPage.getCode() / 10000);
	}
	String result = JSON.toJSONString(responseOfNoPage, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getAlarmOverviewStatistics controller end...");
	return result;
    }

    /**
     * 告警异常数据
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/overview/abnormal_statistic", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getAbnormalAlarmStatistics(
	    @RequestBody CommonRequestOfData<CommonDateRequestBody> commonRequestOfData, HttpServletRequest request,
	    HttpServletResponse response)
    {
	LOGGER.debug("getAbnormalAlarmStatistics controller start...");
	CommonDateRequestBody requestBody = commonRequestOfData.getData();
	String accessToken = request.getHeader("Access-Token");
	String userId = redisUtils.getAdminIdByAccessToken(accessToken);
	CommonResponseOfNoPage<List<GetAbnormalAlarmStatisticsResponseBody>> responseOfNoPage = overviewPanelService
		.getAbnormalAlarmStatistics(requestBody, userId);
	if (responseOfNoPage.getCode() != 0)
	{
	    response.setStatus(responseOfNoPage.getCode() / 10000);
	}
	String result = JSON.toJSONString(responseOfNoPage, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getAbnormalAlarmStatistics controller end...");
	return result;

    }

    /**
     * 消防综合情况
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/overview/fire_situation", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String getFireSituationyStatistics(HttpServletRequest request, HttpServletResponse response)
    {
	LOGGER.debug("getFireSituationyStatistics controller start...");
	String accessToken = request.getHeader("Access-Token");
	String userId = redisUtils.getAdminIdByAccessToken(accessToken);
	CommonResponseOfNoPage<List<GetFireSituationyStatisticsResponseBody>> responseOfNoPage = overviewPanelService
		.getFireSituationyStatistics(userId);
	if (responseOfNoPage.getCode() != 0)
	{
	    response.setStatus(responseOfNoPage.getCode() / 10000);
	}
	String result = JSON.toJSONString(responseOfNoPage, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getFireSituationyStatistics controller end...");
	return result;

    }

    /**
     * 今日巡查
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/overview/today_inspection", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String getTodayInspectionStatistics(HttpServletRequest request, HttpServletResponse response)
    {
	LOGGER.debug("getTodayInspectionStatistics controller start...");
	String accessToken = request.getHeader("Access-Token");
	String userId = redisUtils.getAdminIdByAccessToken(accessToken);
	CommonResponseOfNoPage<GetTodayInspectionStatisticsResponseBody> responseOfNoPage = overviewPanelService
		.getTodayInspectionStatistics(userId);
	if (responseOfNoPage.getCode() != 0)
	{
	    response.setStatus(responseOfNoPage.getCode() / 10000);
	}
	String result = JSON.toJSONString(responseOfNoPage, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getTodayInspectionStatistics controller end...");
	return result;

    }

    /**
     * 终端设备状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/overview/device_status", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String getDeviceStatusStatistics(HttpServletRequest request, HttpServletResponse response)
    {
	LOGGER.debug("getDeviceStatusStatistics controller start...");
	String accessToken = request.getHeader("Access-Token");
	String userId = redisUtils.getAdminIdByAccessToken(accessToken);
	CommonResponseOfNoPage<List<GetDeviceStatusStatisticsResponseBody>> responseOfNoPage = overviewPanelService
		.getDeviceStatusStatistics(userId);
	if (responseOfNoPage.getCode() != 0)
	{
	    response.setStatus(responseOfNoPage.getCode() / 10000);
	}
	String result = JSON.toJSONString(responseOfNoPage, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getDeviceStatusStatistics controller end...");
	return result;

    }

    /**
     * 告警异常趋势
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/overview/alarm_abnormal_trend", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getAbnormalAlarmTrendStatistics(
	    @RequestBody CommonRequestOfData<CommonDateRequestBody> commonRequestOfData, HttpServletRequest request,
	    HttpServletResponse response)
    {
	LOGGER.debug("getAbnormalAlarmTrendStatistics controller start...");
	CommonDateRequestBody requestBody = commonRequestOfData.getData();
	String accessToken = request.getHeader("Access-Token");
	String userId = redisUtils.getAdminIdByAccessToken(accessToken);
	CommonResponseOfNoPage<List<GetAbnormalAlarmTrendStatisticsResponseBody>> responseOfNoPage = overviewPanelService
		.getAbnormalAlarmTrendStatistics(requestBody, userId);
	if (responseOfNoPage.getCode() != 0)
	{
	    response.setStatus(responseOfNoPage.getCode() / 10000);
	}
	String result = JSON.toJSONString(responseOfNoPage, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getAbnormalAlarmTrendStatistics controller end...");
	return result;

    }
}
