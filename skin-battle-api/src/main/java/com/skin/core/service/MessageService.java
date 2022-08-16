package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.Message;

public interface MessageService extends IService<Message> {


    Page<Message> getPage(Integer pageNo, Integer pageSize, Long userId);



}
