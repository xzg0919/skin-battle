package com.tzj.collect.core.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.core.result.business.BusinessRecType;
import com.tzj.collect.entity.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyMapper extends BaseMapper<Company>{

	/**
	 * @author sgmark@aliyun.com
	 * 根据小区Id取得回收企业总数
	 * @param comId 小区Id
	 * @return 企业总数
	 */
	Integer selectCompanyCountByCom(@Param("communityId") String comId);
	/**
	 * @author sgmark@aliyun.com
	 * 根据小区Id取得回收企业回收垃圾类型（去重）
	 * @param comId
	 * @return
	 */
	Integer selectCategoryCountByCom(@Param("communityId") String comId);
	
	/**
	 * 根据小区id取得服务公司列表
	 * @author sgmark@aliyun.com
	 * @return
	 */
	List<Company> selectCompanyListByComm(@Param("communityId") String commId);
	/**
	 * 根据公司id返回公司回收类型
	 * @param comId
	 * @return
	 */
	List<BusinessRecType> getTypeByComId(@Param("companyId") String comId);
	
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业Id
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	Integer getCompanyIdByIds(@Param("CommunityId") Integer CommunityId, @Param("CategoryId") Integer CategoryId);
	/**
	 * 根据分类Id和小区Id查询唯一的所属企业
	 * @author 王灿
	 * @param CommunityId : 小区Id
	 * @param CategoryId : 分类Id
	 * @return
	 */
	Company getCompanyByIds(@Param("CommunityId") Integer CommunityId, @Param("CategoryId") Integer CategoryId);
	/**
	 * 当前小区没有私海公司入驻时返回公海信息
	 * @param communityId
	 * @param categoryId
	 * @return
	 */
	Company getCompanyWithNotFound(@Param("communityId") Integer communityId, @Param("categoryId") Integer categoryId);

    String selectIotUrlByEquipmentCode(@Param("cabinetNo") String cabinetNo);

	List<Map<String,Object>> getCompanyTitleNum();

	List<Map<String,Object>> getAdminCompanyList(@Param("companyName") String companyName, @Param("title") String title, @Param("pageStart") Integer pageStart, @Param("pageSize") Integer pageSize);

	Integer getAdminCompanyCount(@Param("companyName") String companyName, @Param("title") String title);
}
