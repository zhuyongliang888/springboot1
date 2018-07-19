package com.hoperun.micro.security.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hoperun.micro.security.common.ErrorCode;
import com.hoperun.micro.security.common.PortalUtils;
import com.hoperun.micro.security.common.RedisUtils;
import com.hoperun.micro.security.common.StringUtil;
import com.hoperun.micro.security.facade.AbnormalAlarmService;
import com.hoperun.micro.security.request.CommonDateRequestBody;
import com.hoperun.micro.security.request.CommonRequestOfData;
import com.hoperun.micro.security.response.CommonResponseBody;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.GetAlarmTypeDistributionResponse;
import com.hoperun.micro.security.response.GetDeviceAddressDetailsResponse;
import com.hoperun.micro.security.response.GetDeviceProcessingStateResponse;

/**
 * 告警异常模块
 * 
 * @author zhuyl
 *
 */
@RestController
public class AbnormalAlarmController
{
    private final static Logger LOGGER = LoggerFactory.getLogger(AbnormalAlarmController.class);
    
    @Autowired
    private AbnormalAlarmService abnormalAlarmService;
    
    @Autowired
    private RedisUtils RedisUtil;
    
    /**
     * 过滤所有的 options请求
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/**", produces = "application/json;charset=UTF-8", method = RequestMethod.OPTIONS)
    public String optionsHandler(HttpServletRequest request, HttpServletResponse response)
    {
	// set attribution to response
	PortalUtils.setResponseAttribute(response);
	Map<String, Object> resultMap = new HashMap<String, Object>();
	PortalUtils.setResponseStatus(response, resultMap);
	return PortalUtils.resultMapToString(resultMap);
    }
    
    /**
     * 处理 no token
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/noaccesstoken", produces = "application/json;charset=UTF-8")
    public String noAccessToken(HttpServletRequest request, HttpServletResponse response)
    {
	// set attribution to response
	PortalUtils.setResponseAttribute(response);
	Map<String, Object> resultMap = new HashMap<String, Object>();
	resultMap.put(ErrorCode.KEYWORD_CODE, ErrorCode.ERR_CODE_NO_TOKEN);
	resultMap.put(ErrorCode.KEYWORD_MSG, ErrorCode.ERR_MSG_NO_TOKEN);
	resultMap.put(ErrorCode.KEYWORD_STATUS, ErrorCode.CODE_FORBIDDEN);

	LOGGER.error("have not Access-Token");

	PortalUtils.setResponseStatus(response, resultMap);
	return PortalUtils.resultMapToString(resultMap);
    }

    /**
     * 处理 token error
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/erroraccesstoken", produces = "application/json;charset=UTF-8")
    public String errorAccessToken(HttpServletRequest request, HttpServletResponse response)
    {
	// set attribution to response
	PortalUtils.setResponseAttribute(response);
	Map<String, Object> resultMap = new HashMap<String, Object>();
	resultMap.put(ErrorCode.KEYWORD_CODE, ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	resultMap.put(ErrorCode.KEYWORD_MSG, ErrorCode.ERR_MSG_EXPIRED_TOKEN);
	resultMap.put(ErrorCode.KEYWORD_STATUS, ErrorCode.CODE_FORBIDDEN);

	LOGGER.error("Access-Token Expired");

	PortalUtils.setResponseStatus(response, resultMap);
	return PortalUtils.resultMapToString(resultMap);
    }
    
    /**
     * 告警异常模块
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/abnormal_alarm/alarm_type_distribution", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getAlarmTypeDistribution(@RequestBody CommonRequestOfData<CommonDateRequestBody> requestbody,HttpServletRequest request, HttpServletResponse response)
    {
	LOGGER.debug("getAlarmTypeDistribution begin...");
	LOGGER.debug("Enter params : "+requestbody);
	
	PortalUtils.setResponseAttribute(response);
	
	//TODO 需要从token 中获取userId
	String accessToken = request.getHeader("Access-Token");
	String userId = RedisUtil.getAdminIdByAccessToken(accessToken);
	if(StringUtil.isEmpty(userId)) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("userId is not in redis");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	if(requestbody == null || requestbody.getData()==null) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("requestbody or data is null");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	CommonDateRequestBody requestData = requestbody.getData();
	if(requestData.getStart_time() == null || requestData.getEnd_time()==null) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("param start_time or end_time is null");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	if(requestData.getStart_time()>requestData.getEnd_time()) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("start_time can not greater than end_time");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	//计算出所有传感器的告警的数量
	CommonResponseOfNoPage<List<GetAlarmTypeDistributionResponse>> returnResponse = abnormalAlarmService.getAlarmTypeDistribution(requestData,userId);
	if (returnResponse.getCode() != 0)
	{
	    response.setStatus(returnResponse.getCode() / 10000);
	}
	String result = JSON.toJSONString(returnResponse, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getAlarmTypeDistribution end...");
	LOGGER.debug("return result : "+result);
	return result;
    }
    
    /**
     * 告警设备处理状态显示
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/abnormal_alarm/device_processing_state", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getDeviceProcessingState(@RequestBody CommonRequestOfData<CommonDateRequestBody> requestbody,HttpServletRequest request, HttpServletResponse response)
    {
	LOGGER.debug("getDeviceProcessingState begin...");
	LOGGER.debug("Enter params : "+requestbody);
	
	PortalUtils.setResponseAttribute(response);
	
	//TODO 需要从token 中获取userId
	String accessToken = request.getHeader("Access-Token");
	String userId = RedisUtil.getAdminIdByAccessToken(accessToken);
	if(StringUtil.isEmpty(userId)) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("userId is not in redis");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	if(requestbody == null || requestbody.getData()==null) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("requestbody or data is null");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	CommonDateRequestBody requestData = requestbody.getData();
	if(requestData.getStart_time() == null || requestData.getEnd_time()==null) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("param start_time or end_time is null");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	if(requestData.getStart_time()>requestData.getEnd_time()) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("start_time can not greater than end_time");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	//计算出所有传感器的告警的数量
	CommonResponseOfNoPage<List<GetDeviceProcessingStateResponse>> returnResponse = abnormalAlarmService.getDeviceProcessingState(requestData,userId);
	if (returnResponse.getCode() != 0)
	{
	    response.setStatus(returnResponse.getCode() / 10000);
	}
	String result = JSON.toJSONString(returnResponse, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getDeviceProcessingState end...");
	LOGGER.debug("return result : "+result);
	return result;
    }
    
    /**
     * 获取设备所在地址图示
     * 
     * @param request
     * @param response
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/api/v1/abnormal_alarm/device_address/{device_id}", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String getDeviceAddressDetails(HttpServletRequest request, HttpServletResponse response,@PathVariable("device_id") String deviceId)
    {
	LOGGER.debug("getDeviceAddressDetails begin...");
	LOGGER.debug("Enter params : "+deviceId);
	
	PortalUtils.setResponseAttribute(response);
	
	if(StringUtil.isEmpty(deviceId)) {
	    CommonResponseBody returnResponse = new CommonResponseBody();
	    returnResponse.setCode(ErrorCode.ERROR_CODE_PARAMETER_ERROR);
	    returnResponse.setMessage("param deviceId can not null or empty!");
	    response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	    String result = JSON.toJSONString(returnResponse);
	    return result;
	}
	
	//计算出所有传感器的告警的数量
	CommonResponseOfNoPage<GetDeviceAddressDetailsResponse> returnResponse = abnormalAlarmService.getDeviceAddressDetails(deviceId);
	if (returnResponse.getCode() != 0)
	{
	    response.setStatus(returnResponse.getCode() / 10000);
	}
	String result = JSON.toJSONString(returnResponse, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getDeviceProcessingState end...");
	LOGGER.debug("return result : "+result);
	return result;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
