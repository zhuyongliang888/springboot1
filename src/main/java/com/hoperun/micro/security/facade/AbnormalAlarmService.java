package com.hoperun.micro.security.facade;

import java.util.List;

import com.hoperun.micro.security.request.CommonDateRequestBody;
import com.hoperun.micro.security.response.CommonResponseOfNoPage;
import com.hoperun.micro.security.response.GetAlarmTypeDistributionResponse;
import com.hoperun.micro.security.response.GetDeviceAddressDetailsResponse;
import com.hoperun.micro.security.response.GetDeviceProcessingStateResponse;

public interface AbnormalAlarmService
{

    /**
     * 告警类型分布
     * 
     * @param requestData
     * @param userId
     * @return
     */
    CommonResponseOfNoPage<List<GetAlarmTypeDistributionResponse>> getAlarmTypeDistribution(CommonDateRequestBody requestData,String userId);

    /**
     * 告警设备处理状态显示
     * 
     * @param requestData
     * @param userId
     * @return
     */
    CommonResponseOfNoPage<List<GetDeviceProcessingStateResponse>> getDeviceProcessingState(CommonDateRequestBody requestData,String userId);

    /**
     * 获取设备所在的详细信息
     * 
     * @param deviceId
     * @return
     */
    CommonResponseOfNoPage<GetDeviceAddressDetailsResponse> getDeviceAddressDetails(String deviceId);

}
