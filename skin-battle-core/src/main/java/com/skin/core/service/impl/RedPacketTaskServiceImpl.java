package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.RedPacketTaskInfoMapper;
import com.skin.core.mapper.RedPacketTaskMapper;
import com.skin.core.service.PointListService;
import com.skin.core.service.PointService;
import com.skin.core.service.RedPacketTaskService;
import com.skin.core.service.SysParamsService;
import com.skin.entity.DailyTask;
import com.skin.entity.DailyTaskInfo;
import com.skin.entity.RedPacketTask;
import com.skin.entity.RedPacketTaskInfo;
import com.tzj.module.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 16:51
 * @Description:
 */
@Service
@Transactional
public class RedPacketTaskServiceImpl extends ServiceImpl<RedPacketTaskMapper, RedPacketTask> implements RedPacketTaskService {

    @Autowired
    PointListService pointListService;
    @Autowired
    SysParamsService sysParamsService;
    @Autowired
    PointService pointService;

    @Autowired
    RedPacketTaskInfoMapper redPacketTaskInfoMapper;


    @Override
    public Page<RedPacketTask> getPage(Integer pageNo, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNo, pageSize),  new QueryWrapper<RedPacketTask>().orderByDesc("create_date"));
    }

    @Override
    public void receive(Long userId, Long taskId) {
        //判断任务是否存在
        RedPacketTask dailyTask = baseMapper.selectOne(new QueryWrapper<RedPacketTask>()
                .le("start_time",  DateUtils.getDate()+" 00:00:00")
                .ge("end_time", DateUtils.getDate()+" 23:59:59").eq("id", taskId));
        if (dailyTask == null || dailyTask.getStatus() == 1) {
            throw new RuntimeException("红包不存在");
        }

        //判断任务是否已经领取
        RedPacketTaskInfo redPacketTaskInfo = redPacketTaskInfoMapper.selectOne(new QueryWrapper<RedPacketTaskInfo>().eq("user_id", userId)
                .eq("red_Packet_Task_Id", taskId)  );
        AssertUtil.isNotNull(redPacketTaskInfo, "每日任务已经领取");
        //获取任务类型
        BigDecimal point= pointListService.getRechargePointByDate(userId,DateUtils.formatDate(dailyTask.getStartTime(),
                "yyyy-MM-dd"),DateUtils.formatDate(dailyTask.getEndTime(),"yyyy-MM-dd"));


        //判断任务是否完成
        if (point.compareTo(dailyTask.getPrice()) >= 0) {

            Random rand = new Random();
            double randNumber = rand.nextDouble()*dailyTask.getRedPacketEnd().doubleValue() + dailyTask.getRedPacketBegin().doubleValue();


            DecimalFormat df = new DecimalFormat( "0.00" );
            String rewardPrice=df.format(randNumber );

            redPacketTaskInfo = new RedPacketTaskInfo();
            redPacketTaskInfo.setUserId(userId);
            redPacketTaskInfo.setRedPacketTaskId(taskId);
            redPacketTaskInfo.setRewardPrice(new BigDecimal(rewardPrice));
            redPacketTaskInfoMapper.insert(redPacketTaskInfo);
            int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "红包领取", "6"));
            //完成任务
            pointService.editPoint(userId, new BigDecimal(rewardPrice), from, "任务完成奖励", redPacketTaskInfo.getId().toString(), 1);
        }else{
            throw new RuntimeException("未满足红包领取条件");
        }
    }

    @Override
    public Page<RedPacketTask> getPage(Long userId, Integer pageNo, Integer pageSize) {
        Page<RedPacketTask> dailyTaskPage = baseMapper.selectPage(new Page<>(pageNo, pageSize), new QueryWrapper<RedPacketTask>()
                        .le("start_time",  DateUtils.getDate()+" 00:00:00")
                        .ge("end_time", DateUtils.getDate()+" 23:59:59")
                        .select("price,red_packet_begin,red_packet_end,id,start_time,end_time,title")
                .orderByDesc("create_date"));

        List<RedPacketTaskInfo> dailyTaskInfos = redPacketTaskInfoMapper.selectList(new QueryWrapper<RedPacketTaskInfo>().eq("user_id", userId));

        List<RedPacketTask> resultPage = new ArrayList<>();
        dailyTaskPage.getRecords().forEach(redPacketTask -> {
            dailyTaskInfos.forEach(redPacketTaskInfo -> {
                if (redPacketTask.getId().equals(redPacketTaskInfo.getRedPacketTaskId())) {
                    redPacketTask.setCanReceive(3);
                }
            });
            if(redPacketTask.getCanReceive() ==null || redPacketTask.getCanReceive() != 3) {
                //是否满足领取条件
                BigDecimal point= pointListService.getRechargePointByDate(userId,DateUtils.formatDate(redPacketTask.getStartTime(),
                        "yyyy-MM-dd"),DateUtils.formatDate(redPacketTask.getEndTime(),"yyyy-MM-dd"));

                //判断任务是否完成
                if (point.compareTo(redPacketTask.getPrice()) >= 0) {
                    redPacketTask.setCanReceive(2);
                } else {
                    redPacketTask.setCanReceive(1);
                }
            }
            resultPage.add(redPacketTask);
        });
        dailyTaskPage.setRecords(resultPage);
        return dailyTaskPage;
    }
}
