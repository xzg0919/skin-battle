package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.DailyTask;

public interface DailyTaskService extends IService<DailyTask> {


    Page<DailyTask> getPage(Integer pageNo, Integer pageSize);

}
