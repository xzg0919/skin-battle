package com.skin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.common.util.AssertUtil;
import com.skin.core.mapper.VerifyMessageMapper;
import com.skin.core.service.VerifyMessageService;
import com.skin.entity.VerifyMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/17 13:54
 * @Description:
 */
@Service
@Transactional(readOnly = true)
public class VerifyMessageServiceImpl extends ServiceImpl<VerifyMessageMapper, VerifyMessage> implements VerifyMessageService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VerifyMessage check(String to, String verifyCode) {
        VerifyMessage verifyMessage = baseMapper.selectOne(new QueryWrapper<VerifyMessage>().eq("to_", to).eq("verify_code", verifyCode).eq("is_use", 0));

        AssertUtil.isNull(verifyMessage, "验证码错误");

        if(verifyMessage.getCreateDate().getTime()<(System.currentTimeMillis()-60*5*1000)){
            throw new RuntimeException("验证码已过期");
        }
        return verifyMessage;
    }
}
