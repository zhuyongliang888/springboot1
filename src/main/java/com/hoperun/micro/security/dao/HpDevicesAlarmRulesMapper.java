package com.hoperun.micro.security.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hoperun.micro.security.model.HpDevicesAlarmRules;

public interface HpDevicesAlarmRulesMapper {
    int deleteByPrimaryKey(String ruleId);

    int insert(HpDevicesAlarmRules record);

    int insertSelective(HpDevicesAlarmRules record);

    HpDevicesAlarmRules selectByPrimaryKey(String ruleId);

    int updateByPrimaryKeySelective(HpDevicesAlarmRules record);

    int updateByPrimaryKey(HpDevicesAlarmRules record);

    List<HpDevicesAlarmRules> selectByDeviceTypeAndTenantId(@Param("deviceType") String deviceType, @Param("tenantId") String tenantId);
}