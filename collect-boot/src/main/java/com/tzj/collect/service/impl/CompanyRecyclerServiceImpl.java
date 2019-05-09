package com.tzj.collect.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tzj.collect.api.app.AppTokenApi;
import com.tzj.collect.entity.*;
import com.tzj.collect.service.RecyclersTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.api.app.param.RecyclersBean;
import com.tzj.collect.api.app.result.AppCompany;
import com.tzj.collect.api.business.param.BusinessRecyclerBean;
import com.tzj.collect.mapper.CompanyRecyclerMapper;
import com.tzj.collect.service.CompanyRecyclerService;

@Service
@Transactional
public class CompanyRecyclerServiceImpl extends ServiceImpl<CompanyRecyclerMapper, CompanyRecycler>
		implements CompanyRecyclerService {

	@Resource
	private CompanyRecyclerMapper mapper;
	@Autowired
	private RecyclersTitleService recyclersTitleService;

	@Override
	public List<Company> selectCompanyByRecyclerId(String recyclerId) {
		return mapper.selectCompanyByRecyclerId(recyclerId);
	}

	/**
	 * 根据公司id查询绑定回收人员个数
	 */

	@Override
	public int selectRecycleByCompanyId(long id) {

		return mapper.selectRecycleByCompanyId(id);
	}

	@Override
	public List<CompanyRecycler> selectRecByComId(String id) {
		return selectList(new EntityWrapper<CompanyRecycler>().eq("company_id", id));
	}

	@Override
	public List<AppCompany> getRecyclerCompanyStatus(String recId) {
		return mapper.getRecyclerCompanyStatus(recId);
	}

	/**
	 * 根据公司id和回收人员ida查找关联的回收人员
	 */
	@Override
	public CompanyRecycler getCompanyRecycler(BusinessRecyclerBean recyclerBean) {

		return selectOne(new EntityWrapper<CompanyRecycler>().eq("company_id", recyclerBean.getCompanyId())
				.eq("recycler_id", recyclerBean.getId()));
	}

	/*
	 * 查看该公司下的回收人员的身份证信息
	 * 
	 * @return
	 * 
	 * @see
	 * com.tzj.collect.service.CompanyRecyclerService#findIdCard(com.tzj.collect
	 * .api.business.param.BusinessRecyclerBean)
	 */
	@Override
	public Recyclers findIdCard(BusinessRecyclerBean recyclerBean) {
		return mapper.findIdCard(recyclerBean);
	}

	@Override
	public Map<String,Object> getCurrComList(RecyclersBean recyclersBean) {
		List<AppCompany> appComList = mapper.getCurrComList(recyclersBean, (recyclersBean.getPageBean().getPageNumber()-1)*recyclersBean.getPageBean().getPageSize(), recyclersBean.getPageBean().getPageSize());
		int count = mapper.getCurrComCount(recyclersBean);
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> catagList = null;
		List<String> ids = new ArrayList<>();
		if (appComList.size() > 0) {
			for (AppCompany appCompany : appComList) {
				ids.add(appCompany.getId());
			}
		}
		if (ids.size() > 0) {
			recyclersBean.setComIdsList(ids);
			List<AppCompany> cateCom =  mapper.getComCateList(recyclersBean);
			for (AppCompany appCompany : appComList) {
				catagList = new ArrayList<>();
				for (int i = 0; i < cateCom.size(); i++) {
					if (cateCom.get(i).getId().equals(appCompany.getId())) {
						catagList.add(cateCom.get(i).getCateName());
					}
				}
				appCompany.setCategList(catagList);
			}
		}
		int pageNum = count % recyclersBean.getPageBean().getPageSize() == 0 ? count/recyclersBean.getPageBean().getPageSize() : count / recyclersBean.getPageBean().getPageSize() + 1;
		int currentpage = recyclersBean.getPageBean().getPageNumber();
		map.put("pageNum", pageNum);
		map.put("count", count);
		map.put("listCom", appComList);
		map.put("currentPage", currentpage > pageNum ? pageNum : currentpage);
		return map;
	}

	@Override
	public Map<String,Object> getNotEnterComList(RecyclersBean recyclersBean) {
		int count = mapper.getNotEnterComCount(recyclersBean);
		Map<String,Object> map = new HashMap<String,Object>();
		int pageNum = count % recyclersBean.getPageBean().getPageSize() == 0 ? count/recyclersBean.getPageBean().getPageSize() : count / recyclersBean.getPageBean().getPageSize() + 1;
		int currentpage = recyclersBean.getPageBean().getPageNumber();
		List<AppCompany> appNotEnterComList	= mapper.getNotEnterComList(recyclersBean, (recyclersBean.getPageBean().getPageNumber()-1)*recyclersBean.getPageBean().getPageSize(), recyclersBean.getPageBean().getPageSize());
		map.put("pageNum", pageNum);
		map.put("count", count);
		map.put("listCom", appNotEnterComList);
		map.put("currentPage", currentpage > pageNum ? pageNum : currentpage);
		return map;
	}

	@Override
	public boolean insertComRecByComIds(AppCompany company, long recId) {
		
		String[] comIds = company.getComIds().split(",");
		EntityWrapper<CompanyRecycler> wrapper = null;
		CompanyRecycler companyRecycler = null;
		List<CompanyRecycler> list = new ArrayList<>();
		if (comIds.length >= 0) {
			for (String id : comIds) {
				wrapper = new EntityWrapper<>();
				wrapper.eq("recycler_id", recId);
				wrapper.notIn("status_", "1");
				wrapper.eq("company_id", id);
				wrapper.eq("del_flag", "0");
				list = this.selectList(wrapper);
				if (list.size() > 0) {
					companyRecycler = list.get(0);
					companyRecycler.setStatus("0");
					this.insertOrUpdate(companyRecycler);
				} else {
					companyRecycler = new CompanyRecycler();
					companyRecycler.setRecyclerId(Integer.parseInt(recId + ""));
					companyRecycler.setCompanyId(Integer.parseInt(id));
					this.insert(companyRecycler);
				}
			}
			if("Y".equals((company.getIsBigRecycle()))){
				this.saveRecycle(recId+"");
			}
			return true;
		}
		return false;
	}
	public void saveRecycle(String recyclerId){
		RecyclersTitle recyclersTitle = recyclersTitleService.selectOne(new EntityWrapper<RecyclersTitle>().eq("recycle_id", recyclerId).eq("title_id", 4).eq("del_flag", 0));
		if(recyclersTitle!=null){
			return;
		}else {
			recyclersTitle = new RecyclersTitle();
			recyclersTitle.setRecycleId(Integer.parseInt(recyclerId));
			recyclersTitle.setTitleId(4);
			recyclersTitle.setTitleName("大件垃圾");
			recyclersTitleService.insert(recyclersTitle);
		}
	}
	/**
	 * 修改回收人员的启用状态
	 */
	@Override
	public CompanyRecycler getCompanyRecyclerByRecyclerId(Long recyclerId) {
		EntityWrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>();
		wrapper.eq("recycler_id", recyclerId);
		return this.selectOne(wrapper);
	}
/**
 *  通过回收员姓名,id返回某公司回收人员列表
 */
	@Override
	public List<BusinessRecyclerBean> getgetSearchCompanyRecyclerList(BusinessRecyclerBean recyclerBean) {
		return mapper.getgetSearchCompanyRecyclerList(recyclerBean);
	}
	/**
	 * 获取企业服务范围（例南京市，苏州市等）
	 * @author wangcan
	 * @param
	 * @return
	 */
	@Override
	public Object getCompanyRange(Integer companyId){
		return mapper.getCompanyRange(companyId);
	}

	@Override
	public Object getBigCompanyRange(Integer companyId){
		return mapper.getBigCompanyRange(companyId);
	}

	@Override
	public Object recyclersDel(Integer companyId, String recycleId) {
		EntityWrapper<CompanyRecycler> wrapper = new EntityWrapper<CompanyRecycler>();
		wrapper.eq("recycler_id", recycleId);
		wrapper.eq("company_id", companyId);
		wrapper.eq("del_flag", 0);
		CompanyRecycler companyRecycler = this.selectOne(wrapper);
		companyRecycler.setDelFlag("1");
		return this.updateById(companyRecycler);
	}
}
