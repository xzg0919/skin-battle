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
@TableName("notice")
@Data
public class Notice extends DataEntity<Long>{
	@TableId(type = IdType.AUTO)
	private  Long id ;
	/** 标题 */
	private String title ;
	/** 内容 */
	private String content ;
	/** 图片 */
	private String picUrl ;
	/** 排序 */
	@TableField("sort_")
	private Integer sort ;

}
