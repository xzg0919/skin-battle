package com.tzj.green.serviceImpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import javax.annotation.Resource;
import com.tzj.green.entity.CompanyRecycler;
import com.tzj.green.entity.RecyclersRange;
import com.tzj.green.mapper.CompanyRecyclerMapper;
import com.tzj.green.param.PageBean;
import com.tzj.green.param.RecyclerBean;
import com.tzj.green.service.CompanyRecyclerService;
import com.tzj.green.service.RecyclersRangeService;
import com.tzj.green.service.RecyclersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RecyclersRangeService recyclersRangeService;
    @Autowired
    private RecyclersService recyclersService;

    @Override
    public Object getApplyRecyclerList(Long companyId) {
        return companyRecyclerMapper.getApplyRecyclerList(companyId);
    }

    @Override
    @Transactional
    public Object updateApplyRecyclerStatus(RecyclerBean recyclerBean,Long companyId) {
        CompanyRecycler companyRecycler = this.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclerBean.getId()).eq("company_id",companyId));
        if ("2".equals(recyclerBean.getStatus())){
            companyRecycler.setStatus("2");
            this.updateById(companyRecycler);
        }else if ("1".equals(recyclerBean.getStatus())){
            companyRecycler.setStatus("1");
            companyRecycler.setCardType(recyclerBean.getCardType());
            this.updateById(companyRecycler);
//            RecyclersRange recyclersRange = recyclersRangeService.selectOne(new EntityWrapper<RecyclersRange>().eq("recyclers_id", recyclerBean.getId()));
//            if (null == recyclersRange){
//                recyclersRange = new RecyclersRange();
//            }
//            recyclersRange.setRecyclersId(Long.parseLong(recyclerBean.getId()));
//            recyclersRange.setCompanyId(companyId);
//            recyclersRange.setCommunityId(Long.parseLong(recyclerBean.getCommunityId()));
//            recyclersRange.setCommunityHouseId(Long.parseLong(recyclerBean.getHouseId()));
//            recyclersRangeService.insertOrUpdate(recyclersRange);
        }

        return "操作成功";
    }

    @Override
    public Object getCompanyRecyclerList(RecyclerBean recyclerBean, Long companyId) {

        PageBean pageBean = recyclerBean.getPageBean();
        if (null == pageBean){
            pageBean = new PageBean();
        }
        Integer pageStart = (pageBean.getPageNum()-1)*pageBean.getPageSize();
        List<Map<String, Object>> companyRecyclerList = companyRecyclerMapper.getCompanyRecyclerList(companyId, recyclerBean.getName(), recyclerBean.getTel(), recyclerBean.getCardType(), pageStart, pageBean.getPageSize());
        Integer count = companyRecyclerMapper.getCompanyRecyclerCount(companyId, recyclerBean.getName(), recyclerBean.getTel(), recyclerBean.getCardType());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("RecyclerList",companyRecyclerList);
        resultMap.put("count",count);
        resultMap.put("pageNum",pageBean.getPageNum());
        return resultMap;
    }

    @Override
    public Object getRecyclerDetailById(String recyclerId) {
        return recyclersService.selectById(recyclerId);
    }
    @Override
    @Transactional
    public Object deleteCompanyRecyclerById(String recyclerId,Long companyId) {
        CompanyRecycler companyRecycler = this.selectOne(new EntityWrapper<CompanyRecycler>().eq("recycler_id", recyclerId).eq("company_id",companyId));
        companyRecycler.setStatus("2");
        this.updateById(companyRecycler);
        recyclersRangeService.delete(new EntityWrapper<RecyclersRange>().eq("recyclers_id", recyclerId).eq("company_id",companyId));
        return "操作成功";
    }
}