package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;
import com.tzj.green.entity.CompanyRecycler;
import com.tzj.green.mapper.CompanyRecyclerMapper;
import com.tzj.green.service.CompanyRecyclerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [回收人员和企业关联表service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CompanyRecyclerServiceImpl extends ServiceImpl<CompanyRecyclerMapper, CompanyRecycler> implements CompanyRecyclerService
{
    @Resource
    private CompanyRecyclerMapper companyRecyclerMapper;

    @Override
    public Object getApplyRecyclerList(Long companyId) {
        return companyRecyclerMapper.getApplyRecyclerList(companyId);
    }
}