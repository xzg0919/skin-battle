package com.skin.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("box_battle_info")
@Data
public class BoxBattleInfo extends DataEntity{


	/**
	 * 0:未开始  1:进行中 2:已结束
	 */
	@TableField("status_")
	Integer status;

	Integer boxCount;

	BigDecimal boxPrice;

	Integer userCount;

	@TableField(exist = false)
	List<BoxBattleList> boxBattleLists;

	@TableField(exist = false)
	List<BoxBattleUser> boxBattleUsers;

	/**
	 *  是否已加入 1：是 2：否
	 */
	@TableField(exist = false)
	Integer isJoin;


	Long winner;

}
