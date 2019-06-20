package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.XcxSourceNum;
import com.tzj.collect.entity.XcxSourceTitle;
import com.tzj.collect.mapper.XcxSourceNumMapper;
import com.tzj.collect.service.XcxSourceNumService;
import com.tzj.collect.service.XcxSourceTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@Transactional(readOnly=true)
public class XcxSourceNumServiceImpl extends ServiceImpl<XcxSourceNumMapper, XcxSourceNum> implements XcxSourceNumService {

    @Autowired
    private XcxSourceTitleService xcxSourceTitleService;

    @Override
    @Transactional
    public Object saveXcxSourceNum(String xcxCode) {

        XcxSourceTitle xcxSourceTitle = xcxSourceTitleService.selectOne(new EntityWrapper<XcxSourceTitle>().eq("code", xcxCode).eq("del_flag", 0));
        XcxSourceNum xcxSourceNum = this.selectOne(new EntityWrapper<XcxSourceNum>().eq("code", xcxCode).eq("del_flag", 0).eq("times", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        if (null != xcxSourceNum){
            xcxSourceNum.setNum(xcxSourceNum.getNum()+1);
        }else {
            xcxSourceNum = new XcxSourceNum();
            xcxSourceNum.setCode(xcxCode);
            xcxSourceNum.setTimes(new Date());
            xcxSourceNum.setNum(1);
            if(xcxSourceTitle!=null){
                xcxSourceNum.setName(xcxSourceTitle.getName());
            }
        }
        this.insertOrUpdate(xcxSourceNum);
        return "操作成功";
    }
}
