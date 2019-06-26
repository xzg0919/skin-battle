
/**
* @Title: SbRecyclerCommunityServiceImpl.java
* @Package com.tzj.collect.service
* @Description: 【】
* @date 2018年3月5日 下午1:12:56
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.business.param.BusinessRecyclerBean;
import com.tzj.collect.api.business.param.RecyclerServiceBean;
import com.tzj.collect.entity.RecyclerCommunity;
import com.tzj.collect.mapper.RecyclerCommunityMapper;
import com.tzj.collect.service.RecyclerCommunityService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @ClassName: SbRecyclerCommunityServiceImpl
* @Description: 【】
* @date 2018年3月5日 下午1:12:56
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@Service
@Transactional(readOnly = true)
public class RecyclerCommunityServiceImpl extends ServiceImpl<RecyclerCommunityMapper,RecyclerCommunity> implements RecyclerCommunityService{
    @Autowired
	private RecyclerCommunityMapper recyclerCommunityMapper;
	
	@Override
	public Integer selectRecCountByCom(String comId) {
		EntityWrapper<RecyclerCommunity> wrapper = new EntityWrapper<>();
		wrapper.eq("community_id", comId);
		return this.selectCount(wrapper);
	}

	@Override
	public List<RecyclerServiceBean> recyclerServiceList(BusinessRecyclerBean recyclerBean) {
		return recyclerCommunityMapper.recyclerServiceList(recyclerBean);
	}

	@Override
	public int getRecyclerServiceListCount(BusinessRecyclerBean recyclerBean) {
		return recyclerCommunityMapper.getRecyclerServiceListCount(recyclerBean);
	}

	



}
