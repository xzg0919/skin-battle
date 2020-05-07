package com.tzj.green.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.green.entity.MemberCard;
import com.tzj.green.mapper.MemberCardMapper;
import com.tzj.green.service.MemberCardService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberCardServiceImpl extends ServiceImpl<MemberCardMapper, MemberCard> implements MemberCardService {


    @Override
    public void addCard(List<List<String>> csvList) {
        List<String> lists = new ArrayList<>();
        csvList.stream().forEach(list ->{
            if (!list.isEmpty()&& StringUtils.isNotBlank(list.get(0))){
                lists.add(list.get(0));
            }
        });
        baseMapper.addCard(lists);
    }
}
