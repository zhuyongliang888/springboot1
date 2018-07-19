package com.hoperun.micro.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoperun.micro.security.common.ErrorCode;
import com.hoperun.micro.security.common.StringUtil;
import com.hoperun.micro.security.dao.HpDevicesAlarmMapper;
import com.hoperun.micro.security.dao.HpDevicesCollectionMapper;
import com.hoperun.micro.security.dao.HpDevicesMapper;
import com.hoperun.micro.security.dao.HpUsersMapper;
import com.hoperun.micro.security.facade.OverviewPanelService;
import com.hoperun.micro.security.model.HpDevices;
import com.hoperun.micro.security.model.HpDevicesAlarm;
import com.hoperun.micro.security.model.HpDevicesCollection;
import com.hoperun.micro.security.model.HpUsers;
import com.hoperun.micro.security.request.CommonDateRequestBody;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.FailureListResponseBody;
import com.hoperun.micro.security.response.GetAbnormalAlarmStatisticsResponseBody;
import com.hoperun.micro.security.response.GetAbnormalAlarmTrendStatisticsResponseBody;
import com.hoperun.micro.security.response.GetAlarmOverviewStatisticsResponseBody;
import com.hoperun.micro.security.response.GetDeviceStatusStatisticsResponseBody;
import com.hoperun.micro.security.response.GetFireSituationyStatisticsResponseBody;
import com.hoperun.micro.security.response.GetStateSummaryResponseBody;
import com.hoperun.micro.security.response.GetTodayInspectionStatisticsResponseBody;

@Service("OverviewPanelService")
public class OverviewPanelServiceImpl implements OverviewPanelService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(OverviewPanelServiceImpl.class);

    private final static int ONE_DAY_SECONDS = 24 * 3600;
    @Autowired
    private HpDevicesAlarmMapper hpDevicesAlarmMapper;
    @Autowired
    private HpDevicesMapper hpDevicesMapper;
    @Autowired
    private HpUsersMapper hpUsersMapper;
    @Autowired
    private HpDevicesCollectionMapper hpDevicesCollectionMapper;

    /**
     * 状态汇总
     */
    @Override
    public CommonResponseOfNoPage<GetStateSummaryResponseBody> getStateSummary(CommonDateRequestBody requestBody,
	    String userId)
    {
	LOGGER.debug("getStateSummary service start...");
	LOGGER.debug(requestBody.toString());
	CommonResponseOfNoPage<GetStateSummaryResponseBody> responseOfNoPage = new CommonResponseOfNoPage<GetStateSummaryResponseBody>();
	if (StringUtil.isEmpty(userId))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_EXPIRED_TOKEN + ",userId=" + userId);
	    LOGGER.error("Access-Token Expired {}", userId);
	    return responseOfNoPage;
	}
	HpUsers hpUsers = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUsers == null)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    LOGGER.error("this user not exists");
	    return responseOfNoPage;
	}
	if (StringUtil.isEmpty(hpUsers.getTenantId()))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_TENANT_ID_IS_NULL);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_TENANT_ID_IS_NULL);
	    LOGGER.error("tenant_id is null");
	    return responseOfNoPage;
	}
	GetStateSummaryResponseBody responseBody = new GetStateSummaryResponseBody();
	Integer startTime = requestBody.getStart_time();
	Integer endTime = requestBody.getEnd_time();
	if (startTime > endTime)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_PARAMETER_ERROR);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_PARAMETER_ERROR);
	    LOGGER.error("parameter startTime is error");
	    return responseOfNoPage;
	}

	// 查询一周告警次数
	Map<String, Object> timeParams = new HashMap<String, Object>();
	timeParams.put("startTime", startTime);
	timeParams.put("endTime", endTime);
	timeParams.put("tenantId", hpUsers.getTenantId());
	timeParams.put("exceptType", 0);
	List<HpDevicesAlarm> hpDevicesAlarms = hpDevicesAlarmMapper.selectListByParams(timeParams);
	int weekAlarmTimes = 0;
	if (hpDevicesAlarms != null && hpDevicesAlarms.size() > 0)
	{
	    weekAlarmTimes = hpDevicesAlarms.size();
	    responseBody.setWeek_alarm_times(weekAlarmTimes);
	}

	// 查询故障设备数
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("tenantId", hpUsers.getTenantId());
	params.put("exceptType", 1);
	List<HpDevicesAlarm> hpDevicesAlarmList = hpDevicesAlarmMapper.selectListByParams(params);
	int deviceFailureTimes = 0;
	if (hpDevicesAlarmList != null && hpDevicesAlarmList.size() > 0)
	{
	    deviceFailureTimes = hpDevicesAlarms.size();
	}
	// 查询所有设备
	Map<String, Object> devicesParams = new HashMap<String, Object>();
	devicesParams.put("tenantId", hpUsers.getTenantId());
	List<HpDevices> hpDevicesList = hpDevicesMapper.selectListByParams(devicesParams);
	if (hpDevicesList != null && hpDevicesList.size() > 0)
	{
	    // 计算设备完好率
	    Float deviceGoodRate = (float) ((float) (hpDevicesList.size() - deviceFailureTimes)
		    / (float) hpDevicesList.size());
	    responseBody.setDevice_good_rate(deviceGoodRate);
	}
	// 安防评级
	responseBody.setSecurity_rating(1);
	responseOfNoPage.setCode(0);
	responseOfNoPage.setMessage("success");
	responseOfNoPage.setData(responseBody);
	LOGGER.debug("getStateSummary service end...");
	return responseOfNoPage;
    }

    /**
     * 告警概况
     */
    @Override
    public CommonResponseOfNoPage<GetAlarmOverviewStatisticsResponseBody> getAlarmOverviewStatistics(String userId)
    {
	LOGGER.debug("getAlarmOverviewStatistics service start...");
	CommonResponseOfNoPage<GetAlarmOverviewStatisticsResponseBody> responseOfNoPage = new CommonResponseOfNoPage<GetAlarmOverviewStatisticsResponseBody>();
	if (StringUtil.isEmpty(userId))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_EXPIRED_TOKEN + ",userId=" + userId);
	    LOGGER.error("Access-Token Expired {}", userId);
	    return responseOfNoPage;
	}
	HpUsers hpUsers = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUsers == null)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    LOGGER.error("this uer not exists");
	    return responseOfNoPage;
	}
	if (StringUtil.isEmpty(hpUsers.getTenantId()))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_TENANT_ID_IS_NULL);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_TENANT_ID_IS_NULL);
	    LOGGER.error("tenant_id is null");
	    return responseOfNoPage;
	}
	GetAlarmOverviewStatisticsResponseBody responseBody = new GetAlarmOverviewStatisticsResponseBody();
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("tenantId", hpUsers.getTenantId());
	params.put("exceptType", 0);
	// 全部告警数量
	Integer allAlarmCount = hpDevicesAlarmMapper.selectCountByParams(params);
	responseBody.setAll_alarm_count(allAlarmCount);
	List<HpDevicesAlarm> hpDevicesAlarms = hpDevicesAlarmMapper.selectListByParams(params);
	if (allAlarmCount > 0)
	{
	    Integer unprocessedAlarmCount = 0;
	    Integer processingAlarmCount = 0;
	    Integer processedAlarmCount = 0;
	    for (HpDevicesAlarm hpDevicesAlarm : hpDevicesAlarms)
	    {
		if (hpDevicesAlarm.getStatus() == 0)
		{
		    // 未处理告警数量
		    unprocessedAlarmCount++;
		} else if (hpDevicesAlarm.getStatus() == 1)
		{
		    // 处理中告警数量
		    processingAlarmCount++;
		} else
		{
		    // 已处理告警数量
		    processedAlarmCount++;
		}
	    }
	    responseBody.setProcessed_alarm_count(processedAlarmCount);
	    responseBody.setProcessing_alarm_count(processingAlarmCount);
	    responseBody.setUnprocessed_alarm_count(unprocessedAlarmCount);
	}
	responseOfNoPage.setCode(0);
	responseOfNoPage.setMessage("success");
	responseOfNoPage.setData(responseBody);
	LOGGER.debug("getAlarmOverviewStatistics service end...");
	return responseOfNoPage;
    }

    /**
     * 告警异常数据
     */
    @Override
    public CommonResponseOfNoPage<List<GetAbnormalAlarmStatisticsResponseBody>> getAbnormalAlarmStatistics(
	    CommonDateRequestBody requestBody, String userId)
    {
	LOGGER.debug("getAbnormalAlarmStatistics service start...");
	CommonResponseOfNoPage<List<GetAbnormalAlarmStatisticsResponseBody>> responseOfNoPage = new CommonResponseOfNoPage<List<GetAbnormalAlarmStatisticsResponseBody>>();
	if (StringUtil.isEmpty(userId))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_EXPIRED_TOKEN + ",userId=" + userId);
	    LOGGER.error("Access-Token Expired {}", userId);
	    return responseOfNoPage;
	}
	HpUsers hpUsers = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUsers == null)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    LOGGER.error("this uer not exists");
	    return responseOfNoPage;
	}
	if (StringUtil.isEmpty(hpUsers.getTenantId()))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_TENANT_ID_IS_NULL);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_TENANT_ID_IS_NULL);
	    LOGGER.error("tenant_id is null");
	    return responseOfNoPage;
	}
	List<GetAbnormalAlarmStatisticsResponseBody> responseBodyList = new ArrayList<GetAbnormalAlarmStatisticsResponseBody>();
	Integer startTime = requestBody.getStart_time();
	Integer endTime = requestBody.getEnd_time();
	if (startTime > endTime)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_PARAMETER_ERROR);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_PARAMETER_ERROR);
	    LOGGER.error("parameter startTime is error");
	    return responseOfNoPage;
	}

	// 查询所有设备类型
	List<HpDevicesCollection> hpDevicesCollections = hpDevicesCollectionMapper
		.selectAllRecordsByTenantId(hpUsers.getTenantId());
	if (hpDevicesCollections != null && hpDevicesCollections.size() > 0)
	{
	    for (HpDevicesCollection hpDevicesCollection : hpDevicesCollections)
	    {
		if (hpDevicesCollection.getDeviceType() != null)
		{
		    GetAbnormalAlarmStatisticsResponseBody responseBody = new GetAbnormalAlarmStatisticsResponseBody();
		    responseBody.setDevice_type_name(hpDevicesCollection.getName());
		    Map<String, Object> timeParams = new HashMap<String, Object>();
		    timeParams.put("startTime", startTime);
		    timeParams.put("endTime", endTime);
		    timeParams.put("deviceType", hpDevicesCollection.getDeviceType());
		    timeParams.put("tenantId", hpUsers.getTenantId());
		    timeParams.put("exceptType", 0);
		    // 根据设备Id查询设备告警
		    List<HpDevicesAlarm> hpDevicesAlarms = hpDevicesAlarmMapper.selectListByParams(timeParams);
		    if (hpDevicesAlarms != null && hpDevicesAlarms.size() > 0)
		    {
			responseBody.setDeivce_alarm_count(hpDevicesAlarms.size());
		    } else
		    {
			responseBody.setDeivce_alarm_count(0);
		    }
		    responseBodyList.add(responseBody);
		}
	    }
	}
	responseOfNoPage.setCode(0);
	responseOfNoPage.setMessage("success");
	responseOfNoPage.setData(responseBodyList);
	LOGGER.debug("getAbnormalAlarmStatistics service end...responseOfNoPage is {}", responseOfNoPage.toString());
	return responseOfNoPage;

    }

    /**
     * 消防综合情况
     */
    @Override
    public CommonResponseOfNoPage<List<GetFireSituationyStatisticsResponseBody>> getFireSituationyStatistics(
	    String userId)
    {
	LOGGER.debug("getFireSituationyStatistics service start...");
	CommonResponseOfNoPage<List<GetFireSituationyStatisticsResponseBody>> responseOfNoPage = new CommonResponseOfNoPage<List<GetFireSituationyStatisticsResponseBody>>();
	if (StringUtil.isEmpty(userId))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_EXPIRED_TOKEN + ",userId=" + userId);
	    LOGGER.error("Access-Token Expired {}", userId);
	    return responseOfNoPage;
	}
	HpUsers hpUsers = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUsers == null)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    LOGGER.error("this uer not exists");
	    return responseOfNoPage;
	}
	if (StringUtil.isEmpty(hpUsers.getTenantId()))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_TENANT_ID_IS_NULL);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_TENANT_ID_IS_NULL);
	    LOGGER.error("tenant_id is null");
	    return responseOfNoPage;
	}
	List<GetFireSituationyStatisticsResponseBody> responseBodyList = new ArrayList<GetFireSituationyStatisticsResponseBody>();
	String[] str = { "高危项", "危险项", "一般项", "正常项" };
	for (int i = 0; i < str.length; i++)
	{
	    GetFireSituationyStatisticsResponseBody responseBody = new GetFireSituationyStatisticsResponseBody();
	    responseBody.setName(str[i]);
	    responseBody.setCount(i + ((int) (Math.random() * 10)));
	    responseBodyList.add(responseBody);
	}
	responseOfNoPage.setCode(0);
	responseOfNoPage.setMessage("success");
	responseOfNoPage.setData(responseBodyList);
	LOGGER.debug("getFireSituationyStatistics service end...");
	return responseOfNoPage;
    }

    /**
     * 今日巡查
     */
    @Override
    public CommonResponseOfNoPage<GetTodayInspectionStatisticsResponseBody> getTodayInspectionStatistics(String userId)
    {
	LOGGER.debug("getTodayInspectionStatistics service start...");
	CommonResponseOfNoPage<GetTodayInspectionStatisticsResponseBody> responseOfNoPage = new CommonResponseOfNoPage<GetTodayInspectionStatisticsResponseBody>();
	if (StringUtil.isEmpty(userId))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_EXPIRED_TOKEN + ",userId=" + userId);
	    LOGGER.error("Access-Token Expired {}", userId);
	    return responseOfNoPage;
	}
	HpUsers hpUsers = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUsers == null)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    LOGGER.error("this uer not exists");
	    return responseOfNoPage;
	}
	if (StringUtil.isEmpty(hpUsers.getTenantId()))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_TENANT_ID_IS_NULL);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_TENANT_ID_IS_NULL);
	    LOGGER.error("tenant_id is null");
	    return responseOfNoPage;
	}
	GetTodayInspectionStatisticsResponseBody responseBody = new GetTodayInspectionStatisticsResponseBody();

	responseBody.setFire_control_personnel_count(1 + ((int) (Math.random() * 10)));
	responseBody.setInspection_times(1 + ((int) (Math.random() * 3)));
	responseBody.setInspector_count(1 + ((int) (Math.random() * 8)));

	responseOfNoPage.setCode(0);
	responseOfNoPage.setMessage("success");
	responseOfNoPage.setData(responseBody);
	LOGGER.debug("getTodayInspectionStatistics service end...");
	return responseOfNoPage;
    }

    /**
     * 终端设备状态
     */
    @Override
    public CommonResponseOfNoPage<List<GetDeviceStatusStatisticsResponseBody>> getDeviceStatusStatistics(String userId)
    {
	LOGGER.debug("getDeviceStatusStatistics service start...");
	CommonResponseOfNoPage<List<GetDeviceStatusStatisticsResponseBody>> responseOfNoPage = new CommonResponseOfNoPage<List<GetDeviceStatusStatisticsResponseBody>>();
	if (StringUtil.isEmpty(userId))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_EXPIRED_TOKEN + ",userId=" + userId);
	    LOGGER.error("Access-Token Expired {}", userId);
	    return responseOfNoPage;
	}
	HpUsers hpUsers = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUsers == null)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    LOGGER.error("this uer not exists");
	    return responseOfNoPage;
	}
	if (StringUtil.isEmpty(hpUsers.getTenantId()))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_TENANT_ID_IS_NULL);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_TENANT_ID_IS_NULL);
	    LOGGER.error("tenant_id is null");
	    return responseOfNoPage;
	}
	List<GetDeviceStatusStatisticsResponseBody> responseBodyList = new ArrayList<GetDeviceStatusStatisticsResponseBody>();
	List<HpDevicesCollection> hpDevicesCollections = hpDevicesCollectionMapper
		.selectAllRecordsByTenantId(hpUsers.getTenantId());
	if (hpDevicesCollections != null && hpDevicesCollections.size() > 0)
	{
	    for (HpDevicesCollection hpDevicesCollection : hpDevicesCollections)
	    {
		GetDeviceStatusStatisticsResponseBody responseBody = new GetDeviceStatusStatisticsResponseBody();
		// 设备类型名称
		responseBody.setDevice_type_name(hpDevicesCollection.getName());
		int deviceFailureTimes = 0;
		// 查询某一类型故障设备
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tenantId", hpUsers.getTenantId());
		params.put("deviceType", hpDevicesCollection.getDeviceType());
		params.put("exceptType", 1);
		List<HpDevicesAlarm> hpDevicesAlarms = hpDevicesAlarmMapper.selectListByParams(params);
		if (hpDevicesAlarms != null && hpDevicesAlarms.size() > 0)
		{
		    deviceFailureTimes = hpDevicesAlarms.size();
		    responseBody.setDevice_failure_times(deviceFailureTimes);
		} else
		{
		    responseBody.setDevice_failure_times(0);
		}
		// 查询某一类型所有设备
		Map<String, Object> devicesParams = new HashMap<String, Object>();
		devicesParams.put("tenantId", hpUsers.getTenantId());
		devicesParams.put("deviceType", hpDevicesCollection.getDeviceType());
		List<HpDevices> hpDevicesList = hpDevicesMapper.selectListByParams(devicesParams);
		if (hpDevicesList != null && hpDevicesList.size() > 0)
		{
		    // 计算完好率
		    Float deviceGoodRate = (float) ((float) (hpDevicesList.size() - deviceFailureTimes)
			    / (float) hpDevicesList.size());
		    responseBody.setDevice_good_rate(deviceGoodRate);
		}
		responseBodyList.add(responseBody);
	    }
	}
	responseOfNoPage.setCode(0);
	responseOfNoPage.setMessage("success");
	responseOfNoPage.setData(responseBodyList);
	LOGGER.debug("getDeviceStatusStatistics service end...");
	return responseOfNoPage;
    }

    /**
     * 告警异常趋势
     */
    @Override
    public CommonResponseOfNoPage<List<GetAbnormalAlarmTrendStatisticsResponseBody>> getAbnormalAlarmTrendStatistics(
	    CommonDateRequestBody requestBody, String userId)
    {
	LOGGER.debug("getAbnormalAlarmTrendStatistics service start...");
	CommonResponseOfNoPage<List<GetAbnormalAlarmTrendStatisticsResponseBody>> responseOfNoPage = new CommonResponseOfNoPage<List<GetAbnormalAlarmTrendStatisticsResponseBody>>();
	if (StringUtil.isEmpty(userId))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_EXPIRED_TOKEN + ",userId=" + userId);
	    LOGGER.error("Access-Token Expired {}", userId);
	    return responseOfNoPage;
	}
	HpUsers hpUsers = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUsers == null)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    LOGGER.error("this uer not exists");
	    return responseOfNoPage;
	}
	if (StringUtil.isEmpty(hpUsers.getTenantId()))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_TENANT_ID_IS_NULL);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_TENANT_ID_IS_NULL);
	    LOGGER.error("tenant_id is null");
	    return responseOfNoPage;
	}
	List<GetAbnormalAlarmTrendStatisticsResponseBody> responseBodyList = new ArrayList<GetAbnormalAlarmTrendStatisticsResponseBody>();

	if ((requestBody.getStart_time()) > (requestBody.getEnd_time()))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_PARAMETER_ERROR);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_PARAMETER_ERROR);
	    LOGGER.error("parameter startTime is error");
	    return responseOfNoPage;
	}
	List<HpDevicesCollection> hpDevicesCollections = hpDevicesCollectionMapper
		.selectAllRecordsByTenantId(hpUsers.getTenantId());
	if (hpDevicesCollections != null && hpDevicesCollections.size() > 0)
	{
	    for (HpDevicesCollection hpDevicesCollection : hpDevicesCollections)
	    {
		GetAbnormalAlarmTrendStatisticsResponseBody responseBody = new GetAbnormalAlarmTrendStatisticsResponseBody();
		// 设备类型名称
		responseBody.setDevice_type__name(hpDevicesCollection.getName());
		// 设备类型
		String type = hpDevicesCollection.getDeviceType();
		Integer startTime = requestBody.getStart_time();
		Integer endTime = requestBody.getEnd_time();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("deviceType", type);
		params.put("tenantId", hpUsers.getTenantId());
		List<HpDevicesAlarm> hpDevicesAlarmsList = hpDevicesAlarmMapper.selectListByParams(params);
		if (hpDevicesAlarmsList != null && hpDevicesAlarmsList.size() > 0)
		{
		    List<FailureListResponseBody> listResponseBody = new ArrayList<FailureListResponseBody>();
		    while (startTime < endTime)
		    {
			Map<String, Object> tempParams = new HashMap<String, Object>();
			tempParams.put("deviceType", type);
			tempParams.put("startTime", startTime);
			tempParams.put("endTime", startTime + ONE_DAY_SECONDS);
			tempParams.put("tenantId", hpUsers.getTenantId());
			int oneDayAlarmCount = hpDevicesAlarmMapper.selectCountByParams(tempParams);
			FailureListResponseBody failureListResponseBody = new FailureListResponseBody();
			failureListResponseBody.setAlarm_date(startTime);
			failureListResponseBody.setDevice_failure_times(oneDayAlarmCount);
			listResponseBody.add(failureListResponseBody);
			startTime += ONE_DAY_SECONDS;
		    }
		    responseBody.setFailure_list(listResponseBody);
		}
		responseBodyList.add(responseBody);
	    }
	}
	responseOfNoPage.setCode(0);
	responseOfNoPage.setMessage("success");
	responseOfNoPage.setData(responseBodyList);
	LOGGER.debug("getAbnormalAlarmTrendStatistics service end...");
	return responseOfNoPage;
    }

}
