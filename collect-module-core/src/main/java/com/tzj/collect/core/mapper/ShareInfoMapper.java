package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.ShareInfo;
import com.tzj.collect.entity.Sharer;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ShareInfoMapper extends BaseMapper<ShareInfo>{

    Map<String,Object> getShareInfoData(@Param("shareId") String shareId,@Param("date") String date);

}
