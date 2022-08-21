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
@TableName("daily_task_info")
@Data
public class DailyTaskInfo extends DataEntity{


	Long userId;

	Long dailyTaskId;

	BigDecimal rewardPrice;
}
