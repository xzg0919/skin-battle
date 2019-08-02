package com.tzj.collect.module.task.job;

import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.module.task.job.thread.NewThreadPoorExcutor;
import com.tzj.collect.service.FlcxLexiconService;
import com.tzj.collect.service.FlcxRecordsService;
import com.tzj.module.api.utils.SignUtils;
import com.tzj.module.common.api.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 分类查询定期获取查询记录
 *
 * @author sgmark
 * @create 2019-07-03 19:58
 **/
@Component
public class FlcxJob {

    @Resource
    private FlcxRecordsService flcxRecordsService;
    @Resource
    private FlcxLexiconService flcxLexiconService;
    @Autowired
    private RedisUtil redisUtil;
    protected final static Logger logger = LoggerFactory.getLogger(FlcxJob.class);
    /**
     * 定时任务。（每天一点）
     */
    @Scheduled(cron = "0 45 12 * * ?")
    public void flcxExecute(){
        NewThreadPoorExcutor.getThreadPoor().execute(new Thread (new flcxThread(flcxRecordsService, redisUtil)));
    }

    class flcxThread implements Runnable{
        private FlcxRecordsService flcxRecordsService;
        private RedisUtil redisUtil;
        public flcxThread(FlcxRecordsService flcxRecordsService, RedisUtil redisUtil){
            this.flcxRecordsService = flcxRecordsService;
            this.redisUtil = redisUtil;
        }
        @Override
        public void run() {
            logger.info("我打算更新redis中词库--------------------------------");
            System.out.println("我打算更新redis中词库--------------------------------");
            flcxLexiconService.keySearchInRedis(null);
            //每天查询数据库记录，更新
            logger.info("我打算更新redis中词库使用次数--------------------------------");
            System.out.println("我打算更新redis中词库使用次数--------------------------------");
            List<Map<String, Object>> mapList = flcxRecordsService.selectRecordsCountList();
            //记录昨天查询总数放入redis
            mapList.parallelStream().forEach(map -> {
                redisUtil.set(map.get("name_").toString(), map.get("count_"));
            });
        }
    }

    public static void main(String[] args) {
        System.out.println(SignUtil.md5("app_key=app_id_3&data={\"id\":14271,\"isEvaluated\":\"0\"}&format=json&method=post &name=business.order.getOrderDetail&nonce=bd1ded62-7fca-4585-b39f-42e4759c8b291564645421287.8254&timestamp=1564645421287&token=F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CER664EEGM23BXXS2E6QURQCPKEVM4X3OXL5UQXLGPRFHNND2LZQ5PRXZY5NXK654GKXOUC36QQMBGHQMEVBZ7RJWDWBOKOGZXYEL665O5ICJL46VEFK6OFF475UZWIF2HT6UBX2QM2OH2RJNYJKM3BQ76FKG5BNW3JYN7X22YV7FCSF366SXGTQDA6N6YNIF2NRULCH4V2BXE65AX7BHZQBR6LLA&type=feedback/getOrderdetail&version=1.03bd01cca7f3d3023c784a8429f80808f"));
        System.out.println(SignUtil.md5("app_key=app_id_3&data={\"id\":14271,\"isEvaluated\":\"0\"}&format=json&method=post&name=business.order.getOrderDetail&nonce=bd1ded62-7fca-4585-b39f-42e4759c8b291564645421287.8254&timestamp=1564645421287&token=F7AHNFQOKPRQTKYHDWUKCR2X5IP7P4IQNNCPRN6VQNVN6NHTTULOLHZS5OTDCQQBOOX3LCUSO4NFA2KG3P2LEE7CER664EEGM23BXXS2E6QURQCPKEVM4X3OXL5UQXLGPRFHNND2LZQ5PRXZY5NXK654GKXOUC36QQMBGHQMEVBZ7RJWDWBOKOGZXYEL665O5ICJL46VEFK6OFF475UZWIF2HT6UBX2QM2OH2RJNYJKM3BQ76FKG5BNW3JYN7X22YV7FCSF366SXGTQDA6N6YNIF2NRULCH4V2BXE65AX7BHZQBR6LLA&type=feedback/getOrderdetail&version=1.03bd01cca7f3d3023c784a8429f80808f"));
    }
}
