package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.RedPacketTask;

public interface RedPacketTaskService extends IService<RedPacketTask> {


    Page<RedPacketTask> getPage(Integer pageNo, Integer pageSize);
}
