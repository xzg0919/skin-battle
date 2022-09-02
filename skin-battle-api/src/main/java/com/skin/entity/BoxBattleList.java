package com.skin.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("box_battle_list")
@Data
public class BoxBattleList extends DataEntity{


	Long boxBattleId;

	Long boxId;

	String boxName;

	BigDecimal boxPrice;

	String boxPicUrl;

}
