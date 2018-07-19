package com.hoperun.micro.security.dao;

import com.hoperun.micro.security.model.HpMaintainsEmployee;

public interface HpMaintainsEmployeeMapper {
    int deleteByPrimaryKey(String employeeId);

    int insert(HpMaintainsEmployee record);

    int insertSelective(HpMaintainsEmployee record);

    HpMaintainsEmployee selectByPrimaryKey(String employeeId);

    int updateByPrimaryKeySelective(HpMaintainsEmployee record);

    int updateByPrimaryKey(HpMaintainsEmployee record);
}