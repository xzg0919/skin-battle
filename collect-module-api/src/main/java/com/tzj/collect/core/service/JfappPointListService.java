package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.core.param.jfapp.JfappRecyclerBean;
import com.tzj.collect.core.param.jfapp.PageBean;
import com.tzj.collect.entity.JfappPointList;

public interface JfappPointListService extends IService<JfappPointList> {

    Object addOrDeleteUserPoint(JfappRecyclerBean jfappRecyclerBean, long jfappRecyclerId);

    Object getJfappPointList(PageBean pageBean,long jfappRecyclerId);

    Object getJfPointListByAdmin(JfappRecyclerBean jfappRecyclerBean);

}
