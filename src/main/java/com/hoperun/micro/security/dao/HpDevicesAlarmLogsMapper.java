package com.hoperun.micro.security.dao;

import com.hoperun.micro.security.model.HpDevicesAlarmLogs;

public interface HpDevicesAlarmLogsMapper {
    int deleteByPrimaryKey(String logId);

    int insert(HpDevicesAlarmLogs record);

    int insertSelective(HpDevicesAlarmLogs record);

    HpDevicesAlarmLogs selectByPrimaryKey(String logId);

    int updateByPrimaryKeySelective(HpDevicesAlarmLogs record);

    int updateByPrimaryKey(HpDevicesAlarmLogs record);
}