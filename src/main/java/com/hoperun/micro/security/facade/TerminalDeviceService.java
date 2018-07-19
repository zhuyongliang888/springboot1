package com.hoperun.micro.security.facade;

import java.util.List;

import com.hoperun.micro.security.request.GetAllTerminalDevicesAndStatusOfFloorRequestBody;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.GetAllTerminalDevicesAndStatusOfFloorResponseBody;

/**
 * 终端设备服务接口
 * 
 * @author LS
 *
 */
public interface TerminalDeviceService
{
    /**
     * 获取所有终端设备及状态
     * 
     * @return
     */
    public CommonResponseOfNoPage<List<GetAllTerminalDevicesAndStatusOfFloorResponseBody>> getAllTerminalDevicesAndStatusOfFloor(GetAllTerminalDevicesAndStatusOfFloorRequestBody requestBody,String userId);
}
