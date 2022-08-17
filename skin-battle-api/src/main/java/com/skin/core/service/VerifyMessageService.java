package com.skin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.VerifyMessage;

public interface VerifyMessageService extends IService<VerifyMessage> {

    VerifyMessage check(String to, String verifyCode);


}
