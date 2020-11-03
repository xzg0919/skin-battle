package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.XcxSourceNum;
import com.tzj.collect.entity.XcxVisitDetail;

public interface XcxVisitDetailService extends IService<XcxVisitDetail> {


  void visit(XcxVisitDetail xcxVisitDetail);

}
