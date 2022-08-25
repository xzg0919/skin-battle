package com.skin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("pull_box_log")
@Data
public class PullBoxLog extends DataEntity  {

	Long userId;
	String pullBoxSkinName;
	String pullBoxAttritionRate;
	String pullBoxPicUrl;
	Double probability;
	int isSuccess;
	String awardSkinName;
	String awardAttritionRate;
	String awardBoxPicUrl;
	BigDecimal price;
}
