package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.ImprisonMember;
import com.tzj.collect.mapper.ImprisonMemberMapper;
import com.tzj.collect.service.ImprisonMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class ImprisonMemberServiceImpl extends ServiceImpl<ImprisonMemberMapper, ImprisonMember> implements ImprisonMemberService {

    @Autowired
    private  ImprisonMemberMapper imprisonMemberMapper;

    public Boolean isImprisonMember(String aliUserId,String title){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = sdf.format(new Date());
        Boolean isImprison = false;
        ImprisonMember imprisonMember = this.selectOne(new EntityWrapper<ImprisonMember>().eq("ali_user_id", aliUserId).eq("title",title).ge("end_date", nowDate).le("start_date", nowDate));
        if(null != imprisonMember){
            isImprison = true;
        }
        return isImprison;
    }


}
