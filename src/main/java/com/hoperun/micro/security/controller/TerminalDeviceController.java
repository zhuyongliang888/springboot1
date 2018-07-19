package com.hoperun.micro.security.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hoperun.micro.security.common.RedisUtils;
import com.hoperun.micro.security.facade.TerminalDeviceService;
import com.hoperun.micro.security.request.GetAllTerminalDevicesAndStatusOfFloorRequestBody;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.GetAllTerminalDevicesAndStatusOfFloorResponseBody;

/**
 * 终端设备服务controller
 * 
 * @author LS
 *
 */
@RestController
public class TerminalDeviceController
{
    private final static Logger LOGGER = LoggerFactory.getLogger(TerminalDeviceController.class);

    @Autowired
    private TerminalDeviceService terminalDeviceService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取所有终端设备及状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/api/v1/terminal_device/summary", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public String getAllTerminalDevicesAndStatusOfFloor(GetAllTerminalDevicesAndStatusOfFloorRequestBody requestBody,
	    HttpServletRequest request, HttpServletResponse response)
    {
	LOGGER.debug("getAllTerminalDevicesAndStatusOfFloor controller start...");
	String accessToken = request.getHeader("Access-Token");
	String userId = redisUtils.getAdminIdByAccessToken(accessToken);
	CommonResponseOfNoPage<List<GetAllTerminalDevicesAndStatusOfFloorResponseBody>> responseOfNoPage = terminalDeviceService
		.getAllTerminalDevicesAndStatusOfFloor(requestBody,userId);
	if (responseOfNoPage.getCode() != 0)
	{
	    response.setStatus(responseOfNoPage.getCode() / 10000);
	}
	String result = JSON.toJSONString(responseOfNoPage, SerializerFeature.WriteMapNullValue);
	LOGGER.debug("getAllTerminalDevicesAndStatusOfFloor controller end...");
	return result;

    }
}
