package com.hoperun.micro.security.dao;

import java.util.List;
import java.util.Map;

import com.hoperun.micro.security.model.HpDevicesCollection;

public interface HpDevicesCollectionMapper
{
    int deleteByPrimaryKey(String collectId);

    int insert(HpDevicesCollection record);

    int insertSelective(HpDevicesCollection record);

    HpDevicesCollection selectByPrimaryKey(String collectId);

    int updateByPrimaryKeySelective(HpDevicesCollection record);

    int updateByPrimaryKey(HpDevicesCollection record);

    List<HpDevicesCollection> selectAllRecordsByTenantId(String tenantId);

    HpDevicesCollection selectByParams(Map<String, Object> params);
}