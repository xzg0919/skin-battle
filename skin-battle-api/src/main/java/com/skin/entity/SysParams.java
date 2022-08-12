package com.skin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("sys_params")
@Data
public class SysParams extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;
	private String param ;
	@TableField("desc_")
	private String desc;
	private String val ;

}
