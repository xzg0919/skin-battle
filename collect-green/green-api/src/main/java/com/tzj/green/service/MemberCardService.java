package com.tzj.green.service;

import com.baomidou.mybatisplus.service.IService;
import com.tzj.green.entity.MemberCard;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface MemberCardService extends IService<MemberCard> {



    void addCard(List<List<String>> csvList);



}
