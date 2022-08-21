package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.DailyTaskInfoMapper;
import com.skin.core.mapper.DailyTaskMapper;
import com.skin.core.service.DailyTaskService;
import com.skin.core.service.PointListService;
import com.skin.core.service.PointService;
import com.skin.core.service.SysParamsService;
import com.skin.entity.DailyTask;
import com.skin.entity.DailyTaskInfo;
import com.tzj.module.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 15:47
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class DailyTaskServiceImpl extends ServiceImpl<DailyTaskMapper, DailyTask> implements DailyTaskService {

    @Autowired
    DailyTaskInfoMapper dailyTaskInfoMapper;
    @Autowired
    PointListService pointListService;
    @Autowired
    SysParamsService sysParamsService;
    @Autowired
    PointService pointService;

    @Override
    public Page<DailyTask> getPage(Integer pageNo, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageNo, pageSize), new QueryWrapper<DailyTask>().orderByDesc("create_date"));
    }

    @Transactional
    @Override
    public void receive(Long userId, Long taskId) {
        //判断任务是否存在
        DailyTask dailyTask = baseMapper.selectById(taskId);
        if (dailyTask == null || dailyTask.getStatus() == 1) {
            throw new RuntimeException("每日任务不存在");
        }

        //判断任务是否已经领取
        DailyTaskInfo dailyTaskInfo = dailyTaskInfoMapper.selectOne(new QueryWrapper<DailyTaskInfo>().eq("user_id", userId)
                .eq("daily_Task_Id", taskId)
                .between("create_date", DateUtils.getDate() + " 00:00:00", DateUtils.getDate() + " 23:59:59"));
        AssertUtil.isNotNull(dailyTaskInfo, "每日任务已经领取");
        //获取任务类型
        BigDecimal point;
        if (dailyTask.getType() == 1) {
            point = pointListService.getRechargePointToday(userId);
        } else {
            point = pointListService.getConsumePointToday(userId);
        }

        //判断任务是否完成
        if (point.abs().compareTo(dailyTask.getPrice().abs()) >= 0) {

            dailyTaskInfo = new DailyTaskInfo();
            dailyTaskInfo.setUserId(userId);
            dailyTaskInfo.setDailyTaskId(taskId);
            dailyTaskInfo.setRewardPrice(dailyTask.getRewardPrice());
            dailyTaskInfoMapper.insert(dailyTaskInfo);
            int from = Integer.parseInt(sysParamsService.getSysParams("business_type", "任务完成奖励", "9"));
            //完成任务
            pointService.editPoint(userId, dailyTaskInfo.getRewardPrice(), from, "任务完成奖励", dailyTaskInfo.getId().toString(), 1);
        }else{
            throw new RuntimeException("未满足任务条件");
        }
    }

    @Override
    public Page<DailyTask> getPage(Long userId, Integer pageNo, Integer pageSize) {
        Page<DailyTask> dailyTaskPage = baseMapper.selectPage(new Page<>(pageNo, pageSize),
                new QueryWrapper<DailyTask>().orderByDesc("create_date")
                        .select("id,title,price,reward_price,type"));

        List<DailyTaskInfo> dailyTaskInfos = dailyTaskInfoMapper.selectList(new QueryWrapper<DailyTaskInfo>().eq("user_id", userId)
                .between("create_date", DateUtils.getDate() + " 00:00:00", DateUtils.getDate() + " 23:59:59"));

        List<DailyTask> resultPage = new ArrayList<>();
        dailyTaskPage.getRecords().forEach(dailyTask -> {
            dailyTaskInfos.forEach(dailyTaskInfo -> {
                if (dailyTask.getId().equals(dailyTaskInfo.getDailyTaskId())) {
                    dailyTask.setCanReceive(3);
                }
            });
            if(dailyTask.getCanReceive() ==null || dailyTask.getCanReceive() != 3) {
                //判断任务是否完成
                BigDecimal point;
                if (dailyTask.getType() == 1) {
                    point = pointListService.getRechargePointToday(userId);
                } else {
                    point = pointListService.getConsumePointToday(userId);
                }

                //判断任务是否完成
                if (point.abs().compareTo(dailyTask.getPrice().abs()) >= 0) {
                    dailyTask.setCanReceive(2);
                } else {
                    dailyTask.setCanReceive(1);
                }
            }
            resultPage.add(dailyTask);
        });
        dailyTaskPage.setRecords(resultPage);
        return dailyTaskPage;
    }


}
