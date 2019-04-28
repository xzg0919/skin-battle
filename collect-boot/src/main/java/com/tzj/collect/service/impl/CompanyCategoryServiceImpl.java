package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.result.ComCatePrice;
import com.tzj.collect.api.business.param.ComIdAndCateOptIdBean;
import com.tzj.collect.api.business.result.BusinessCategoryResult;
import com.tzj.collect.entity.*;
import com.tzj.collect.mapper.CompanyCategoryMapper;
import com.tzj.collect.mapper.CompanyMapper;
import com.tzj.collect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回收企业关联ServiceImpl
 * @Author 王灿
 **/
@Service
@Transactional(readOnly = true)
public class CompanyCategoryServiceImpl extends ServiceImpl<CompanyCategoryMapper, CompanyCategory> implements CompanyCategoryService{
   
	
	@Override
	public int selectCategoryByCompanyId(long id) {
		
		return this.selectCategoryByCompanyId(id);
	}
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CompanyCategoryMapper comCateMapper;
	@Autowired
	private CompanyServiceService companyServiceService;
	@Autowired
	private CompanyCategoryMapper companyCategoryMapper;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private CompanyStreeService companyStreeService;
	@Autowired
	private CompanyStreetBigService companyStreetBigService;


	/**
	 * 小程序取得所有二级分类
	 * @param
	 * @return
	 */
	@Override
	public Map<String, Object> categoryTwoList(CategoryBean categoryBean) {
		Map<String, Object> map = new HashMap<>();
		List<ComCatePrice> priceList = null;
		List<ComCatePrice> noPriceList = null;
		priceList = this.getAvgPrice(categoryBean);
		noPriceList = this.getAvgNoPrice(categoryBean);
		map.put("ComCatePriceList", priceList);
		map.put("ComCateNoPriceList", noPriceList);
		map.put("category",categoryService.selectById(categoryBean.getId()));
		return  map;
	}
	/**
	 * 小程序取得所有二级分类
	 * @param
	 * @return
	 */
	@Override
	public Map<String, Object> categoryHouseTwoList(CategoryBean categoryBean) {
		Category category = categoryService.selectById(categoryBean.getId());
		Map<String, Object> map = new HashMap<>();
		List<ComCatePrice> priceList = null;
		List<ComCatePrice> noPriceList = null;
			//根据分类Id和小区Id查询所属企业
			Company company = this.selectCompany(categoryBean.getId(),categoryBean.getCommunityId());
			if("bigFurniture".equals(categoryBean.getType())){
				//判断该地址是否回收5公斤废纺衣物
				Integer streeCompanyId = companyStreetBigService.selectStreetBigCompanyId(categoryBean.getId(), categoryBean.getStreeId());
				if(null != streeCompanyId){
					priceList = comCateMapper.getBigThingCategoryList(categoryBean.getId(),streeCompanyId);
				}else {
					priceList = this.getAvgPrice(categoryBean);
					noPriceList = this.getAvgNoPrice(categoryBean);
				}
			}else if("appliance".equals(categoryBean.getType())){
				if(null!=company){
					//获取当前公司下的回收列表
					priceList = this.getOwnnerPrice(categoryBean, Integer.parseInt(company.getId().toString()));
					noPriceList = this.getOwnnerNoPrice(categoryBean,Integer.parseInt(company.getId().toString()));
				}else {
					priceList = this.getAvgPrice(categoryBean);
					noPriceList = this.getAvgNoPrice(categoryBean);
				}
			}else if("rubbish".equals(categoryBean.getType())){
				if(null!=company){
					//根据小区Id查询小区信息
					Community community  = communityService.selectById(categoryBean.getCommunityId());
					//判断该小区是否免费
					if("1".equals(community.getIsFree())) {
						priceList = new ArrayList<ComCatePrice>();
						noPriceList = this.getNoPrice(categoryBean, Integer.parseInt(company.getId().toString()));
					}else {
						priceList = this.getOwnnerPrice(categoryBean, Integer.parseInt(company.getId().toString()));
						noPriceList = this.getOwnnerNoPrice(categoryBean,Integer.parseInt(company.getId().toString()));
					}
				}else {
					//判断该地址是否回收5公斤废纺衣物
					Integer streeCompanyId = companyStreeService.selectStreeCompanyIds(categoryBean.getId(), categoryBean.getStreeId());
					if ("45".equals(categoryBean.getId().toString())&&null != streeCompanyId){
							category.setIcon("http://images.sqmall.top/collect/20190412/original_2d560942-7a1f-4c95-9721-b19cb892ac4f.jpg");
							category.setRecNotes("1.废旧衣物指无污染、无霉变的各类纺织品，包含衣服、鞋子、纺织包袋、床上用品和织物类家具用品；," +
									"2.衣服包含毛衣、大衣、T恤和衬衫等；," +
									"3.鞋子包含运动鞋、皮鞋和帆布鞋等；," +
									"4.纺织包袋包含手提包、皮包和电脑包等；," +
									"5.床上用品包含床单、被套和枕套等；," +
									"6.织物类家居用品包含窗帘、地毯、毛巾、浴巾和桌布等;," +
									"7.当日晚8点后预约，可预约时间最早为次日上午；," +
									"8.预约完成后，回收人员会在预约时间段电话联系您，确认具体的上门时间，免费上门收取废旧衣物.," +
									"提示：," +
									"1.邮管局规定各快递公司全面推进实名制寄件，回收人员上门收件时可能需要您配合出示身份证件，望您理解;," +
									"2.废旧衣物积累到5kg及以上可进行预约，需要您亲自打包好，这样可以减少上门回收的时间，方便回收人员称重.");
						priceList = this.getOwnnerPrice(categoryBean,streeCompanyId);
						noPriceList = this.getOwnnerNoPrice(categoryBean,streeCompanyId);
					}else{
						priceList = this.getAvgPrice(categoryBean);
						noPriceList = this.getAvgNoPrice(categoryBean);
					}
				}
			}
			map.put("ComCatePriceList", priceList);
			map.put("ComCateNoPriceList", noPriceList);
			map.put("category",category);
		return  map;
	}
	/**
	 * 获取价格分页(不分页)
	 */
	@Override
	public Map<String, Object> getPrice(CategoryBean categoryBean) {
		Map<String, Object> map = new HashMap<>();
		
		List<ComCatePrice> priceList = null;
		List<ComCatePrice> noPriceList = null;
		//如果不为空获取当前公司回收价格
		if (categoryBean.getCommunityId() != null&&categoryBean.getCommunityId()!=0) {
			//根据分类Id和小区Id查询所属企业
	    	Company company = this.selectCompany(categoryBean.getId(),categoryBean.getCommunityId());
			//根据小区Id查询唯一的所属企业
	    	//CompanyServiceRange companyServiceRange = companyServiceService.selectOne(new EntityWrapper<CompanyServiceRange>().eq("community_id", categoryBean.getCommunityId()));
	    	//Integer companyId = companyService.getCompanyIdByIds(orderbean.getCommunityId(),orderbean.getCategoryParentId());
	    	if(company != null) {
	    		//根据小区Id查询小区信息
	    		Community community  = communityService.selectById(categoryBean.getCommunityId());
	    		//判断该小区是否免费
	    		if("1".equals(community.getIsFree())) {
	    			//免费时
	    			//生活垃圾时
	    			if("HOUSEHOLD".equals(categoryBean.getTitle())) {
	    				priceList = new ArrayList<ComCatePrice>();
	    				noPriceList = this.getNoPrice(categoryBean, Integer.parseInt(company.getId().toString()));
	    			}else {
	    				priceList = this.getOwnnerPrice(categoryBean, Integer.parseInt(company.getId().toString()));
	    			}
	    			map.put("comIsNull", "2");
	    			map.put("isCash", "1");
	    			map.put("companyId", company.getId());
	    		}else {
	    			//获取当前公司下的回收列表
	    			map.put("comIsNull", "2");
	    			map.put("isCash", "0");
	    			map.put("companyId", company.getId());
	    			priceList = this.getOwnnerPrice(categoryBean, Integer.parseInt(company.getId().toString()));
	    			noPriceList = this.getOwnnerNoPrice(categoryBean,Integer.parseInt(company.getId().toString()));
	    		}
	    	}else{
	    		//获取平均价格
	    		map.put("comIsNull", "1");
	    		map.put("isCash", "0");
	    		priceList = this.getAvgPrice(categoryBean);
	    		noPriceList = this.getAvgNoPrice(categoryBean);
	    	}
		}else{
			//获取平均价格
    		map.put("comIsNull", "0");
    		map.put("isCash", "0");
    		priceList = this.getAvgPrice(categoryBean);
    		noPriceList = this.getAvgNoPrice(categoryBean);
		}
		map.put("ComCatePriceList", priceList);
		map.put("ComCateNoPriceList", noPriceList);

		return  map;
	}
	/**
	 * @author wangcan
	 * @param categoryBean:传入小区CommunityId，id(分类ID，大的分类)
	 * 获取价格分页(不分页)
	 */
	@Override
	public Map<String, Object> getTowCategoryList(CategoryBean categoryBean) {
		Map<String, Object> map = new HashMap<>();

		List<ComCatePrice> priceList = null;
		//判断是否免费
		String isCash = categoryBean.getIsCash();
//		if ("1".equals(isCash)){
//			map.put("comIsNull", false);
//			priceList = comCateMapper.getAppCategoryList();
//			map.put("ComCatePriceList", priceList);
//			return  map;
//		}
		//如果不为空获取当前公司回收价格
		if (categoryBean.getCommunityId() != null) {
			//根据小区Id查询唯一的所属企业
	    	CompanyServiceRange companyServiceRange = companyServiceService.selectOne(new EntityWrapper<CompanyServiceRange>().eq("community_id", categoryBean.getCommunityId()));
	    	//Integer companyId = companyService.getCompanyIdByIds(orderbean.getCommunityId(),orderbean.getCategoryParentId());
	    	if(companyServiceRange != null) {
	    		//获取当前公司下的回收列表
				map.put("comIsNull", true);
				map.put("companyId", companyServiceRange.getCompanyId());
				priceList = this.getOwnnerPriceApp(categoryBean, Integer.parseInt(companyServiceRange.getCompanyId()));
	    	}else{
	    		//获取平均价格
	    		map.put("comIsNull", false);
	    		priceList = this.getAvgPriceApp(categoryBean);
	    	}
		}else{
			//获取平均价格
    		map.put("comIsNull", false);
    		priceList = this.getAvgPriceApp(categoryBean);
		}
		map.put("ComCatePriceList", priceList);

		return  map;
	}
	@Override
	public List<ComCatePrice> getOwnnerPrice(CategoryBean categoryBean, Integer companyId) {
		return comCateMapper.getOwnnerPrice(categoryBean, companyId);
	}
	public List<ComCatePrice> getOwnnerNoPrice(CategoryBean categoryBean, Integer companyId) {
		return comCateMapper.getOwnnerNoPrice(categoryBean, companyId);
	}
	public List<ComCatePrice> getNoPrice(CategoryBean categoryBean, Integer companyId) {
		return comCateMapper.getNoPrice(categoryBean, companyId);
	}
	@Override
	public List<ComCatePrice> getOwnnerPriceApp(CategoryBean categoryBean, Integer companyId) {
		return comCateMapper.getOwnnerPriceApp(categoryBean, companyId);
	}
	@Override
	public List<ComCatePrice> getAvgPrice(CategoryBean categoryBean) {
		return comCateMapper.getAvgPrice(categoryBean);
	}
	public List<ComCatePrice> getAvgNoPrice(CategoryBean categoryBean) {
		return comCateMapper.getAvgNoPrice(categoryBean);
	}
	public List<ComCatePrice> getAvgPriceApp(CategoryBean categoryBean) {
		return comCateMapper.getAvgPriceApp(categoryBean);
	}
	@Override
	public List<BusinessCategoryResult> selectComCateAttOptPrice(ComIdAndCateOptIdBean comIdAndCateOptIdBean) {
		String cateOptId = comIdAndCateOptIdBean.getCateOptId();
				
		String companyId = comIdAndCateOptIdBean.getCompanyId();
		try {
			if (cateOptId == null || "".equals(cateOptId)) {
				throw new ApiException("cateOptId不能为空");
			}
			if (companyId == null || "".equals(companyId)) {
				throw new ApiException("请先登录");
			}
		} catch (ApiException e) {
			e.getCause();
		}
		List<BusinessCategoryResult> list = comCateMapper.selectComCateAttOptPrice(comIdAndCateOptIdBean);
		String price = null;
		for (BusinessCategoryResult businessCategoryResult : list) {
			if (businessCategoryResult.getComOptPrice() != null) {
				price = businessCategoryResult.getComOptPrice();
			}
			if (price != null) {
				if (new BigDecimal(price).compareTo(BigDecimal.ZERO) >= 0) {
					businessCategoryResult.setComOptAddPrice(price);
				}else{
					businessCategoryResult.setComOptDecprice(new BigDecimal(price).abs().toString());
				}
			}
		}
		return list;
	}
	
	@Override
	public CompanyCategory selectByCategoryId(int categoryId) {
		return comCateMapper.selectByCategoryId(categoryId);
	}
	

	/**
	 * 更新生活垃圾价格
	 * @param
	 * @return
	 */
	@Override
	public int updatePrice(CompanyCategory companyCategory) {
		return companyCategoryMapper.updateHousePrice(companyCategory);
	}
	
	/**
	 * 添加关联表
	 */
	@Override
	public int insertPrice(CompanyCategory companyCategory) {
		return companyCategoryMapper.insertPrice(companyCategory);
	}
	@Override
	public CompanyCategory selectPriceByAttrId(String id, String companyId) {
		return companyCategoryMapper.selectPriceByAttrId(id, companyId);
	}
	@Override
	public Company selectCompany(Integer categoryId, Integer communityId) {
		return companyCategoryMapper.selectCompany(categoryId,communityId);
	}  

}
