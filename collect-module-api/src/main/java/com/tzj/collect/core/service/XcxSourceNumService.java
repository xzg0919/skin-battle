package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.XcxSourceNum;

public interface XcxSourceNumService extends IService<XcxSourceNum> {


Object saveXcxSourceNum(String xcxCode);

}
