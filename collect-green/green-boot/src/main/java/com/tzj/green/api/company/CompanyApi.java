package com.tzj.green.api.company;

import com.tzj.green.GreenApplication;
import com.tzj.green.entity.Logs;
import com.tzj.green.service.LogsService;
import com.tzj.green.service.OrderCountService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@ApiService
public class CompanyApi {
    @Resource
    private OrderCountService orderCountService;
    @Resource
    private LogsService logsService;

    protected final static Logger logger = LoggerFactory.getLogger(GreenApplication.class);

    /**
     * 杭州城管局获取解衣科技数据信息
     *
     */
    @Api(name = "company.list.get", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object getList(){
        Logs logs = null;
        try {
            //插入日志
            logs = new Logs();
            logs.setCompanyId(6);
            logs.setParam("萧山城管局获取信息");
            logsService.insert(logs);
        }catch (Exception e){
            logger.info("萧山城管局请求获取信息");
        }
        Object obj = orderCountService.getOrderCount1();
        try {
            logs.setBody(obj.toString());
            logsService.updateById(logs);
        }catch (Exception e){
            logger.info("返回萧山城管局获取信息"+logs);
        }
        return obj;
    }
}
