package com.hoperun.micro.security.dao;

import java.util.List;
import java.util.Map;

import com.hoperun.micro.security.model.HpDevicesAlarm;

public interface HpDevicesAlarmMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HpDevicesAlarm record);

    int insertSelective(HpDevicesAlarm record);

    HpDevicesAlarm selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HpDevicesAlarm record);

    int updateByPrimaryKey(HpDevicesAlarm record);
    
    List<HpDevicesAlarm> selectListByParams(Map<String, Object> params);
    
    int selectCountByParams(Map<String, Object> params);
    
    HpDevicesAlarm selectBySerial(String serial);
}