package com.hoperun.micro.security.dao;

import com.hoperun.micro.security.model.HpDevicesModels;

public interface HpDevicesModelsMapper {
    int deleteByPrimaryKey(String modelId);

    int insert(HpDevicesModels record);

    int insertSelective(HpDevicesModels record);

    HpDevicesModels selectByPrimaryKey(String modelId);

    int updateByPrimaryKeySelective(HpDevicesModels record);

    int updateByPrimaryKey(HpDevicesModels record);
}