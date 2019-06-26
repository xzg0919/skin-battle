package com.tzj.collect.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.collect.entity.CompanyServiceRange;

import java.util.List;
import java.util.Map;

public interface CompanyServiceMapper extends BaseMapper<CompanyServiceRange>{

	
	/**根据公司ID获取获取服务范围小区个数
	* @Title: selectCompanyServiceByCompanyId
	* @Description: 【】
	* @date 2018年3月20日 下午12:18:14
	* @author:[王池][wjc2013481273@163.com]
	* @param @param id
	* @param @return    参数
	* @return int    返回类型
	* @throws
	*/
	
	int selectCompanyServiceByCompanyId(@Param("companyId") long id);

	Map<String,Object> companyAreaRanges(String companyId);

}
