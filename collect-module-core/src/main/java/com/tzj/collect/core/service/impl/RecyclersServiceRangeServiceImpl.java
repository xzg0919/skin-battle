package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.RecyclersServiceRangeMapper;
import com.tzj.collect.core.service.RecyclersServiceRangeService;
import com.tzj.collect.entity.RecyclersServiceRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RecyclersServiceRangeServiceImpl extends ServiceImpl<RecyclersServiceRangeMapper,RecyclersServiceRange> implements RecyclersServiceRangeService {

    @Autowired
    private RecyclersServiceRangeMapper recyclersServiceRangeMapper;

    /**
     * //储存经理和区域的关联关系
     * @param recycleId : 回收人员id
     * @param areaId : 区域id
     * @return
     */
    @Transactional
    @Override
    public void  saveRangeRecycleByAreaId(Integer recycleId,Integer areaId) {
         recyclersServiceRangeMapper.saveRangeRecycleByAreaId(recycleId, areaId);
    }
}
