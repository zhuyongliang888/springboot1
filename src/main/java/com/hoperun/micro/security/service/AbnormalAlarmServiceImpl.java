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
import com.hoperun.micro.security.dao.HpDevicesAlarmMapper;
import com.hoperun.micro.security.dao.HpDevicesAlarmRulesMapper;
import com.hoperun.micro.security.dao.HpDevicesCollectionMapper;
import com.hoperun.micro.security.dao.HpDevicesMapper;
import com.hoperun.micro.security.dao.HpUsersMapper;
import com.hoperun.micro.security.facade.AbnormalAlarmService;
import com.hoperun.micro.security.model.HpDevices;
import com.hoperun.micro.security.model.HpDevicesAlarm;
import com.hoperun.micro.security.model.HpDevicesAlarmRules;
import com.hoperun.micro.security.model.HpDevicesCollection;
import com.hoperun.micro.security.model.HpUsers;
import com.hoperun.micro.security.request.CommonDateRequestBody;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.GetAlarmTypeDistributionResponse;
import com.hoperun.micro.security.response.GetAlarmTypeDistributionResponseBody;
import com.hoperun.micro.security.response.GetDeviceAddressDetailsResponse;
import com.hoperun.micro.security.response.GetDeviceProcessingStateResponse;
import com.hoperun.micro.security.response.GetDeviceProcessingStateResponseBody;

@Service
public class AbnormalAlarmServiceImpl implements AbnormalAlarmService
{

    private final static Logger LOGGER = LoggerFactory.getLogger(AbnormalAlarmServiceImpl.class);
    private final static int ONE_DAY_SECONDS = 24 * 3600;

    @Autowired
    private HpDevicesAlarmMapper hpDevicesAlarmMapper;

    @Autowired
    private HpUsersMapper hpUsersMapper;
    
    @Autowired
    private HpDevicesCollectionMapper hpDevicesCollectionMapper;
    
    @Autowired
    private HpDevicesAlarmRulesMapper hpDevicesAlarmsRulesMapper;
    
    @Autowired
    private HpDevicesMapper hpDevicesMapper;

    /**
     * 告警类型分布业务实现类
     * 
     * @param requestData
     * @param userId
     */
    @Override
    public CommonResponseOfNoPage<List<GetAlarmTypeDistributionResponse>> getAlarmTypeDistribution(
	    CommonDateRequestBody requestData, String userId)
    {
	LOGGER.debug("begin AbnormalAlarmServiceImpl.getAlarmTypeDistribution");
	LOGGER.debug("Enter params:" + requestData);

	List<GetAlarmTypeDistributionResponse> dataList = new ArrayList<GetAlarmTypeDistributionResponse>();
	CommonResponseOfNoPage<List<GetAlarmTypeDistributionResponse>> commonResponseOfNoPage = new CommonResponseOfNoPage<List<GetAlarmTypeDistributionResponse>>();

	// TODO 根据userId 获取 tenantId
	// 1.查看userId是否存在于数据库中
	HpUsers hpUser = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUser == null)
	{
	    commonResponseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    commonResponseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    return commonResponseOfNoPage;
	}

	String tenantId = hpUser.getTenantId();

	// 根据时间段查询此段时间的所有类型的告警设备数目
	// 1.根据表hp_devices_collection 查询出所有的type
	// 2.遍历type
	List<HpDevicesCollection> HpDevicesCollectionList = hpDevicesCollectionMapper.selectAllRecordsByTenantId(tenantId);
	
	if (HpDevicesCollectionList != null && HpDevicesCollectionList.size() > 0)
	{
	    for (int i = 0; i < HpDevicesCollectionList.size(); i++)
	    {
		GetAlarmTypeDistributionResponse getAlarmTypeDistributionResponse = new GetAlarmTypeDistributionResponse();

		HpDevicesCollection hpDevicesCollectionTemp = HpDevicesCollectionList.get(i);

		if (hpDevicesCollectionTemp != null && hpDevicesCollectionTemp.getDeviceType() != null)
		{
		    // set 设备类型名称
		    getAlarmTypeDistributionResponse.setDevice_type_name(hpDevicesCollectionTemp.getName());

		    String type = hpDevicesCollectionTemp.getDeviceType();

		    // 查询出此类型在此时间段范围内的告警次数
		    Map<String, Object> alarmParams = new HashMap<String, Object>();
		    alarmParams.put("deviceType", type);
		    alarmParams.put("tenantId", tenantId);
		    alarmParams.put("startTime", requestData.getStart_time());
		    alarmParams.put("endTime", requestData.getEnd_time());
		    int count = hpDevicesAlarmMapper.selectCountByParams(alarmParams);

		    getAlarmTypeDistributionResponse.setAlarm_times(count);

		    if (count == 0)
		    {
			// TODO 此处 count ==0的判断可能需要除掉
			// count ==0表示在此时间段内没有告警

			// failureList.add(getAlarmTypeDistributionResponseBody);
			// getAlarmTypeDistributionResponse.setFailure_list(failureList);
		    } else
		    {
			List<GetAlarmTypeDistributionResponseBody> failureList = new ArrayList<GetAlarmTypeDistributionResponseBody>();
			// TODO 统计在startTime endTime 时间范围内的每一天的告警次数？
			// start_time 2018-6-8 00:00:00
			// end_time 2018-6-8 23:59:59
			int startTime = requestData.getStart_time();
			int endTime = requestData.getEnd_time();
			while (startTime < endTime)
			{

			    Map<String, Object> tempParams = new HashMap<String, Object>();
			    tempParams.put("deviceType", type);
			    tempParams.put("tenantId", tenantId);
			    tempParams.put("startTime", startTime);
			    tempParams.put("endTime", startTime + ONE_DAY_SECONDS);
			    int oneDayAlarmCount = hpDevicesAlarmMapper.selectCountByParams(tempParams);
			    LOGGER.debug("oneDayAlarmCount = " + oneDayAlarmCount);
			    LOGGER.debug("startTime = " + startTime);
			    LOGGER.debug("endTime = " + (startTime + ONE_DAY_SECONDS));
			    GetAlarmTypeDistributionResponseBody getAlarmTypeDistributionResponseBody = new GetAlarmTypeDistributionResponseBody();
			    getAlarmTypeDistributionResponseBody.setAlarm_date(startTime);
			    getAlarmTypeDistributionResponseBody.setDevice_failure_times(oneDayAlarmCount);
			    failureList.add(getAlarmTypeDistributionResponseBody);

			    startTime += ONE_DAY_SECONDS;
			}

			getAlarmTypeDistributionResponse.setFailure_list(failureList);

		    }
		    dataList.add(getAlarmTypeDistributionResponse);
		}
	    }

	}
	commonResponseOfNoPage.setCode(0);
	commonResponseOfNoPage.setData(dataList);
	commonResponseOfNoPage.setMessage(ErrorCode.MSG_OK);
	LOGGER.debug("End AbnormalAlarmServiceImpl.GetAlarmTypeDistributionResponse");
	LOGGER.debug("result result :" + commonResponseOfNoPage);

	return commonResponseOfNoPage;
    }

    /**
     * 告警设备
     * 
     * @param requestData
     * @param userId
     * @return
     * 
     */
    @Override
    public CommonResponseOfNoPage<List<GetDeviceProcessingStateResponse>> getDeviceProcessingState(
	    CommonDateRequestBody requestData, String userId)
    {
	LOGGER.debug("begin AbnormalAlarmServiceImpl.getDeviceProcessingState");
	LOGGER.debug("Enter params:" + requestData + " userId :" + userId);

	List<GetDeviceProcessingStateResponse> dataList = new ArrayList<GetDeviceProcessingStateResponse>();
	CommonResponseOfNoPage<List<GetDeviceProcessingStateResponse>> commonResponseOfNoPage = new CommonResponseOfNoPage<List<GetDeviceProcessingStateResponse>>();

	// TODO 根据userId 获取 tenantId
	// 1.查看userId是否存在于数据库中
	HpUsers hpUser = hpUsersMapper.selectByPrimaryKey(userId);
	if (hpUser == null)
	{
	    commonResponseOfNoPage.setCode(ErrorCode.ERR_CODE_USER_NOT_EXISTS);
	    commonResponseOfNoPage.setMessage(ErrorCode.ERR_MSG_USER_NOT_EXISTS);
	    return commonResponseOfNoPage;
	}

	String tenantId = hpUser.getTenantId();

	List<HpDevicesCollection> HpDevicesCollectionList = hpDevicesCollectionMapper.selectAllRecordsByTenantId(tenantId);
	if (HpDevicesCollectionList != null && HpDevicesCollectionList.size() > 0)
	{
	    for (int i = 0; i < HpDevicesCollectionList.size(); i++)
	    {
		GetDeviceProcessingStateResponse getDeviceProcessingStateResponse = new GetDeviceProcessingStateResponse();
		List<GetDeviceProcessingStateResponseBody> alarmList = new ArrayList<GetDeviceProcessingStateResponseBody>();
		HpDevicesCollection hpDevicesCollectionTemp = HpDevicesCollectionList.get(i);

		if (hpDevicesCollectionTemp != null && hpDevicesCollectionTemp.getDeviceType() != null)
		{
		    getDeviceProcessingStateResponse.setDevice_type(hpDevicesCollectionTemp.getDeviceType());
		    getDeviceProcessingStateResponse.setDevice_type_name(hpDevicesCollectionTemp.getName());

		    String deviceType = hpDevicesCollectionTemp.getDeviceType();
		    // 根据type查询表alarm_rules,非主键查询可能存在多条记录，只取list中的第一条
		    List<HpDevicesAlarmRules> alarmRulesList = hpDevicesAlarmsRulesMapper.selectByDeviceTypeAndTenantId(deviceType, tenantId);
		    if (alarmRulesList != null && alarmRulesList.size() > 0)
		    {
			getDeviceProcessingStateResponse.setMin_value(alarmRulesList.get(0).getMin());
			getDeviceProcessingStateResponse.setMax_value(alarmRulesList.get(0).getMax());
		    }

		    // 查询出此类型在此时间段范围内的告警次数
		    Map<String, Object> alarmParams = new HashMap<String, Object>();
		    alarmParams.put("deviceType", deviceType);
		    alarmParams.put("tenantId", tenantId);
		    alarmParams.put("startTime", requestData.getStart_time());
		    alarmParams.put("endTime", requestData.getEnd_time());
		    alarmParams.put("orderField", "attime");
		    alarmParams.put("orderFieldType", "DESC");
		    List<HpDevicesAlarm> hpDevicesAlarmList = hpDevicesAlarmMapper.selectListByParams(alarmParams);
		    // 设置默认值0
		    getDeviceProcessingStateResponse.setAlarm_times(0);
		    if (hpDevicesAlarmList != null && hpDevicesAlarmList.size() > 0)
		    {
			getDeviceProcessingStateResponse.setAlarm_times(hpDevicesAlarmList.size());

			for (HpDevicesAlarm hpDevicesAlarm : hpDevicesAlarmList)
			{

			    GetDeviceProcessingStateResponseBody getDeviceProcessingStateResponseBody = new GetDeviceProcessingStateResponseBody();
			    String serial = hpDevicesAlarm.getSerial();
			    if (serial != null)
			    {
				//根据serial查询表 hp_devices,非主键使用list接收
				List<HpDevices> hpDevicesList = hpDevicesMapper.selectRecordsBySerial(serial);
				if (hpDevicesList != null && hpDevicesList.size()>0)
				{
				    // 获取位置信息
				    getDeviceProcessingStateResponseBody.setDevice_address(hpDevicesList.get(0).getLocation());
				}
				
				//TODO 此处使用serial 返回 为deviceId
				getDeviceProcessingStateResponseBody.setDevice_id(serial);

				getDeviceProcessingStateResponseBody.setAlarm_date(hpDevicesAlarm.getAttime());
				getDeviceProcessingStateResponseBody.setAlarm_info("");
				getDeviceProcessingStateResponseBody.setAlarm_state(hpDevicesAlarm.getStatus());
				getDeviceProcessingStateResponseBody.setProcess_date(hpDevicesAlarm.getProcesstime());
				
				//返回处理Id
				getDeviceProcessingStateResponseBody.setAlarm_id(hpDevicesAlarm.getId());
				getDeviceProcessingStateResponseBody.setAlarm_val(hpDevicesAlarm.getAlarmVal());
				alarmList.add(getDeviceProcessingStateResponseBody);
				getDeviceProcessingStateResponse.setAlarm_list(alarmList);
			    }

			}

		    }

		}

		dataList.add(getDeviceProcessingStateResponse);
	    }

	}

	commonResponseOfNoPage.setCode(0);
	commonResponseOfNoPage.setData(dataList);
	commonResponseOfNoPage.setMessage(ErrorCode.MSG_OK);
	LOGGER.debug("End AbnormalAlarmServiceImpl.getDeviceProcessingState");
	LOGGER.debug("result result :" + commonResponseOfNoPage);

	return commonResponseOfNoPage;
    }

    /**
     * 获取设备所在图示 业务实现类
     * 
     * @param deviceId
     * @return
     */
    @Override
    public CommonResponseOfNoPage<GetDeviceAddressDetailsResponse> getDeviceAddressDetails(String deviceId)
    {
	LOGGER.debug("begin AbnormalAlarmServiceImpl.getDeviceAddressDetails");
	LOGGER.debug("Enter params: deviceId  =" + deviceId);
	CommonResponseOfNoPage<GetDeviceAddressDetailsResponse> commonResponseOfNoPage = new CommonResponseOfNoPage<GetDeviceAddressDetailsResponse>();

	// 验证deviceId 是否在数据库中存在
	HpDevices hpDevices = hpDevicesMapper.selectByPrimaryKey(deviceId);
	if (hpDevices == null)
	{
	    // 错误码返回
	    commonResponseOfNoPage.setCode(ErrorCode.ERR_CODE_DEVICE_NOT_EXISTS);
	    commonResponseOfNoPage.setMessage(ErrorCode.ERR_MSG_DEVICE_NOT_EXISTS);
	    return commonResponseOfNoPage;
	}
	GetDeviceAddressDetailsResponse getDeviceAddressDetailsResponse = new GetDeviceAddressDetailsResponse();

	getDeviceAddressDetailsResponse.setBuilding(hpDevices.getBuilding());
	// TODO 暂时保留字段
	getDeviceAddressDetailsResponse.setDevice_address_url("");
	getDeviceAddressDetailsResponse.setFloor(hpDevices.getFloor());
	getDeviceAddressDetailsResponse.setLocation(hpDevices.getLocation());
	getDeviceAddressDetailsResponse.setRoom(hpDevices.getRoom());

	commonResponseOfNoPage.setCode(0);
	commonResponseOfNoPage.setMessage(ErrorCode.MSG_OK);
	commonResponseOfNoPage.setData(getDeviceAddressDetailsResponse);

	return commonResponseOfNoPage;
    }

}
