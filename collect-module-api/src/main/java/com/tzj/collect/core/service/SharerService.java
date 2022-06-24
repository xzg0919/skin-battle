package com.tzj.collect.core.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.Admin;
import com.tzj.collect.entity.Sharer;


public interface SharerService extends IService<Sharer>{


	Sharer getByAliUserId(String aliUserId);


}
