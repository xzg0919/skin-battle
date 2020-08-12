
/**
* @Title: SbOrderPicService.java
* @Package com.tzj.collect.service
* @Description: 【】
* @date 2018年3月5日 下午12:53:47
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.entity.OrderOperate;

import java.util.List;

public interface OrderOperateService extends IService<OrderOperate>{

    List<OrderOperate> selectOperate(OrderBean orderbean);

}
