package com.linjiao.cmslive.mapper.test;

import com.linjiao.cmslive.model.CMSLive;

public interface CMSLiveMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CMSLive record);

    int insertSelective(CMSLive record);

    CMSLive selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CMSLive record);

    int updateByPrimaryKeyWithBLOBs(CMSLive record);

    int updateByPrimaryKey(CMSLive record);

    Integer selectBiggestSourceId(String source);
}