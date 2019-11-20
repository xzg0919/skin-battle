package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.jfapp.JfappRecyclerBean;
import com.tzj.collect.entity.JfappRecycler;

public interface JfappRecyclerService extends IService<JfappRecycler> {

    Object getRecycleToken(JfappRecyclerBean jfappRecyclerBean);


}
