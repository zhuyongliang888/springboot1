package com.hoperun.micro.security.dao;

import com.hoperun.micro.security.model.HpUsers;

public interface HpUsersMapper {
    int deleteByPrimaryKey(String userId);

    int insert(HpUsers record);

    int insertSelective(HpUsers record);

    HpUsers selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(HpUsers record);

    int updateByPrimaryKeyWithBLOBs(HpUsers record);

    int updateByPrimaryKey(HpUsers record);
}