package com.hoperun.micro.security.dao;

import com.hoperun.micro.security.model.HpDevicesMfs;

public interface HpDevicesMfsMapper {
    int deleteByPrimaryKey(String mfId);

    int insert(HpDevicesMfs record);

    int insertSelective(HpDevicesMfs record);

    HpDevicesMfs selectByPrimaryKey(String mfId);

    int updateByPrimaryKeySelective(HpDevicesMfs record);

    int updateByPrimaryKey(HpDevicesMfs record);
}