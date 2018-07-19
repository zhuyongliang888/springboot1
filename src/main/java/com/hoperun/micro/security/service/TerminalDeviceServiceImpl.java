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
import com.hoperun.micro.security.facade.TerminalDeviceService;
import com.hoperun.micro.security.model.HpDevices;
import com.hoperun.micro.security.model.HpDevicesAlarm;
import com.hoperun.micro.security.model.HpDevicesCollection;
import com.hoperun.micro.security.model.HpUsers;
import com.hoperun.micro.security.request.GetAllTerminalDevicesAndStatusOfFloorRequestBody;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.DeviceListResponseBody;
import com.hoperun.micro.security.response.GetAllTerminalDevicesAndStatusOfFloorResponseBody;

/**
 * 终端设备服务业务
 * 
 * @author LS
 *
 */
@Service("TerminalDeviceService")
public class TerminalDeviceServiceImpl implements TerminalDeviceService
{
    private final static Logger LOGGER = LoggerFactory.getLogger(TerminalDeviceServiceImpl.class);

    @Autowired
    private HpUsersMapper hpUsersMapper;
    @Autowired
    private HpDevicesMapper hpDevicesMapper;
    @Autowired
    private HpDevicesCollectionMapper hpDevicesCollectionMapper;
    @Autowired
    private HpDevicesAlarmMapper hpDevicesAlarmMapper;

    /**
     * 获取某个楼层所有的终端设备及状态
     */
    @Override
    public CommonResponseOfNoPage<List<GetAllTerminalDevicesAndStatusOfFloorResponseBody>> getAllTerminalDevicesAndStatusOfFloor(
	    GetAllTerminalDevicesAndStatusOfFloorRequestBody requestBody, String userId)
    {
	LOGGER.debug("getAllTerminalDevicesAndStatusOfFloor service start...");
	LOGGER.debug(requestBody.toString());
	CommonResponseOfNoPage<List<GetAllTerminalDevicesAndStatusOfFloorResponseBody>> responseOfNoPage = new CommonResponseOfNoPage<List<GetAllTerminalDevicesAndStatusOfFloorResponseBody>>();
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
	List<GetAllTerminalDevicesAndStatusOfFloorResponseBody> responseBodyList = new ArrayList<GetAllTerminalDevicesAndStatusOfFloorResponseBody>();
	GetAllTerminalDevicesAndStatusOfFloorResponseBody responseBody = new GetAllTerminalDevicesAndStatusOfFloorResponseBody();

	String building = requestBody.getBuilding();
	String floor = requestBody.getFloor();
	if (requestBody.getDevice_type() == "")
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_PARAMETER_ERROR);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_PARAMETER_ERROR + ",type is null");
	    LOGGER.error("parameter type is error {}", requestBody.getDevice_type());
	    return responseOfNoPage;
	}
	int deviceType = 0;
	try
	{
	    deviceType = Integer.parseInt(requestBody.getDevice_type());
	} catch (Exception e)
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_PARAMETER_ERROR);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_PARAMETER_ERROR + ",type is error");
	    LOGGER.error("parameter type is error {}", requestBody.getDevice_type());
	    return responseOfNoPage;
	}
	if (StringUtil.isEmpty(building) || StringUtil.isEmpty(floor))
	{
	    responseOfNoPage.setCode(ErrorCode.ERR_CODE_PARAMETER_NOTNULL);
	    responseOfNoPage.setMessage(ErrorCode.ERR_MSG_PARAMETER_NOTNULL);
	    LOGGER.error("parameter building or floor is null");
	    return responseOfNoPage;
	}
	// 查询某一楼层某一类型设备
	Map<String, Object> devicesParams = new HashMap<String, Object>();
	devicesParams.put("building", building);
	devicesParams.put("floor", floor);
	devicesParams.put("deviceType", deviceType);
	devicesParams.put("tenantId", hpUsers.getTenantId());
	List<HpDevices> hpDevicesList = hpDevicesMapper.selectListByParams(devicesParams);
	if (hpDevicesList.size() > 0)
	{
	    Map<String, Object> deviceCollectionParams = new HashMap<String, Object>();
	    deviceCollectionParams.put("deviceType", deviceType);
	    deviceCollectionParams.put("tenantId", hpUsers.getTenantId());
	    HpDevicesCollection hpDevicesCollections = hpDevicesCollectionMapper.selectByParams(deviceCollectionParams);
	    if (hpDevicesCollections != null)
	    {
		// 某一楼层某一类型设备名称
		responseBody.setDevice_type_name(hpDevicesCollections.getName());
		// 某一楼层某一类型设备总数
		responseBody.setTotal_count(hpDevicesList.size());

		List<DeviceListResponseBody> listResponseBody = new ArrayList<DeviceListResponseBody>();
		int faultyCount = 0;
		for (HpDevices hpDevices : hpDevicesList)
		{
		    // 物联网终端唯一识别码
		    String serial = hpDevices.getSerial();
		    // 查询设备故障数
		    Map<String, Object> devicesAlarmParams = new HashMap<String, Object>();
		    devicesAlarmParams.put("serial", serial);
		    devicesAlarmParams.put("deviceType", deviceType);
		    devicesAlarmParams.put("tenantId", hpUsers.getTenantId());
		    devicesAlarmParams.put("exceptType", 1);
		    List<HpDevicesAlarm> hpDevicesAlarms = hpDevicesAlarmMapper.selectListByParams(devicesAlarmParams);
		    DeviceListResponseBody deviceListResponseBody = new DeviceListResponseBody();
		    // 设备状态
		    if (hpDevicesAlarms != null && hpDevicesAlarms.size() > 0)
		    {
			faultyCount++;
			deviceListResponseBody.setDevice_state(0);
		    } else
		    {
			deviceListResponseBody.setDevice_state(1);
		    }
		    // 设备Id
		    deviceListResponseBody.setDevice_id(hpDevices.getDevId());
		    // 设备位置
		    deviceListResponseBody.setDevice_address(hpDevices.getLocation());
		    listResponseBody.add(deviceListResponseBody);
		}
		responseBody.setFaulty_count(faultyCount);
		responseBody.setDevice_list(listResponseBody);
		responseBodyList.add(responseBody);
	    }
	}
	responseOfNoPage.setCode(0);
	responseOfNoPage.setMessage("success");
	responseOfNoPage.setData(responseBodyList);
	LOGGER.debug("getAllTerminalDevicesAndStatusOfFloor service end...");
	return responseOfNoPage;
    }

}
