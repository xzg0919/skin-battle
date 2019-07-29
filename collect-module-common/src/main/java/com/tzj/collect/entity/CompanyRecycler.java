package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 回收人员和企业关联表
 * 
 * @ClassName: CompanyRecycler
 * @Description: 【】
 * @date 2018年3月15日 上午10:27:29
 * @Company: 上海挺之军科技有限公司
 * @Department：研发部
 * @author:[王池][wjc2013481273@163.com]
 */
@TableName("sb_company_recycler")
@Data
public class CompanyRecycler extends DataEntity<Long> {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer recyclerId;// 回收人员id
	private Integer companyId;// 回收企业id
	@TableField(value = "status_")
	private String status = "0";// 状态 0申请 1入住2拒绝
	@TableField(value = "type_")
	private String type = "0";//申请的类型 1家电生活垃圾  4大件

	private String isManager;//是否企业经理 1 经理 0 普通员工

	private Integer city;//回收经理所在城市

	private Integer province;//回收人员所在省份

	private Integer parentsId;//属于哪位业务经理 业务经理为空

}
