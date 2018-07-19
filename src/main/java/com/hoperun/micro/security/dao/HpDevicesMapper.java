package com.hoperun.micro.security.dao;

import java.util.List;
import java.util.Map;

import com.hoperun.micro.security.model.HpDevices;

public interface HpDevicesMapper {

    int deleteByPrimaryKey(String devId);

    int insert(HpDevices record);

    int insertSelective(HpDevices record);

    HpDevices selectByPrimaryKey(String devId);

    int updateByPrimaryKeySelective(HpDevices record);

    int updateByPrimaryKey(HpDevices record);
    
    List<HpDevices> selectListByParams(Map<String, Object> params);

    List<HpDevices> selectRecordsBySerial(String serial);
}