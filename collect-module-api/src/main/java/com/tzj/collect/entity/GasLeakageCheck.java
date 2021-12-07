package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 
 * @ClassName: Admin 
 * @author: 向忠国
 * @date: 2018年3月16日 上午11:04:44
 *
 * 燃气泄露检测信息填写
 */
@TableName("gas_leakage_check")
@Data
public class GasLeakageCheck extends DataEntity<Long> {
	 Long id;

     String tel;

     String address;

     String contactName;

}
