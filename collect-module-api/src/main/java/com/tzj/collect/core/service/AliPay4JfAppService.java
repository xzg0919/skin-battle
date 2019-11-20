package com.tzj.collect.core.service;

import java.util.Date;

public interface AliPay4JfAppService {

    public String updatePoint(String targetCardNo, Date openDate, String point, String vip);
}
