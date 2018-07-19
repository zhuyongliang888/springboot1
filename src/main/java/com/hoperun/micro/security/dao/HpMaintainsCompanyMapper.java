package com.hoperun.micro.security.dao;

import com.hoperun.micro.security.model.HpMaintainsCompany;

public interface HpMaintainsCompanyMapper {
    int deleteByPrimaryKey(String mtcId);

    int insert(HpMaintainsCompany record);

    int insertSelective(HpMaintainsCompany record);

    HpMaintainsCompany selectByPrimaryKey(String mtcId);

    int updateByPrimaryKeySelective(HpMaintainsCompany record);

    int updateByPrimaryKey(HpMaintainsCompany record);
}