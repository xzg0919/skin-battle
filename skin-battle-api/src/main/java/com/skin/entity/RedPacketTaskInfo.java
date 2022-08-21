package com.skin.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


/**
 * admin;
 * @author : http://www.chiner.pro
 * @date : 2022-8-9
 */
@TableName("red_packet_task_info")
@Data
public class RedPacketTaskInfo extends DataEntity{


	Long userId;

	Long redPacketTaskId;

	BigDecimal rewardPrice;
}
