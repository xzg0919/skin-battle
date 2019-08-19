package com.tzj.point.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.point.api.app.param.JfappRecyclerBean;
import com.tzj.point.entity.JfappRecycler;

public interface JfappRecyclerService extends IService<JfappRecycler> {

    Object getRecycleToken(JfappRecyclerBean jfappRecyclerBean);


}
