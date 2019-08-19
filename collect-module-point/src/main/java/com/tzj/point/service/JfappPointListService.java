package com.tzj.point.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.point.api.app.param.JfappRecyclerBean;
import com.tzj.point.api.app.param.PageBean;
import com.tzj.point.entity.JfappPointList;

public interface JfappPointListService extends IService<JfappPointList> {

    Object addOrDeleteUserPoint(JfappRecyclerBean jfappRecyclerBean, long jfappRecyclerId);

    Object getJfappPointList(PageBean pageBean,long jfappRecyclerId);

    Object getJfPointListByAdmin(JfappRecyclerBean jfappRecyclerBean);

}
