package com.tzj.collect.core.service;

import com.tzj.collect.common.amap.AmapResult;

public interface MapService {


    String getLocation(String address) throws Exception;

    AmapResult getAmap(String location) throws Exception;

}
