package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.RecyclersServiceRange;

public interface RecyclersServiceRangeService extends IService<RecyclersServiceRange> {
  /**
   * //储存经理和区域的关联关系
   * @param recycleId : 回收人员id
   * @param areaId : 区域id
   * @return
   */
   void  saveRangeRecycleByAreaId(Integer recycleId, Integer areaId);

}
