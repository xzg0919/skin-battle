package com.tzj.green.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tzj.green.entity.MemberCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberCardMapper extends BaseMapper<MemberCard> {


    void addCard(@Param("csvList") List<String> csvList);


}
