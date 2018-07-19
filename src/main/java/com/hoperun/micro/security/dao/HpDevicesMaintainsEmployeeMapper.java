package com.hoperun.micro.security.dao;

import com.hoperun.micro.security.model.HpDevicesMaintainsEmployee;

public interface HpDevicesMaintainsEmployeeMapper {
    int deleteByPrimaryKey(String deviceMeId);

    int insert(HpDevicesMaintainsEmployee record);

    int insertSelective(HpDevicesMaintainsEmployee record);

    HpDevicesMaintainsEmployee selectByPrimaryKey(String deviceMeId);

    int updateByPrimaryKeySelective(HpDevicesMaintainsEmployee record);

    int updateByPrimaryKey(HpDevicesMaintainsEmployee record);
}