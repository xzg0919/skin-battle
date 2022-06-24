package com.tzj.collect.core.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.collect.entity.ShareInfo;
import com.tzj.collect.entity.Sharer;

import java.util.Map;


public interface ShareInfoService extends IService<ShareInfo>{


    void share(String shareId, String aliUserId,String name,String tel);

    Map<String,Object> getShareData(String shareId,String date);
}
