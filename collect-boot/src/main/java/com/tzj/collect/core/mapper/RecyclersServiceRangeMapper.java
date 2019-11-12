package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.RecyclersServiceRange;
import org.apache.ibatis.annotations.Param;

public interface RecyclersServiceRangeMapper extends BaseMapper<RecyclersServiceRange> {
    /**
     * //储存经理和区域的关联关系
     * @param recycleId : 回收人员id
     * @param areaId : 区域id
     * @return
     */
    void  saveRangeRecycleByAreaId(@Param("recycleId") Integer recycleId, @Param("areaId") Integer areaId);

}
