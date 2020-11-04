package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.XcxSourceNumMapper;
import com.tzj.collect.core.mapper.XcxVisitDetailMapper;
import com.tzj.collect.core.service.LineQrCodeService;
import com.tzj.collect.core.service.XcxSourceNumService;
import com.tzj.collect.core.service.XcxSourceTitleService;
import com.tzj.collect.core.service.XcxVisitDetailService;
import com.tzj.collect.entity.LineQrCode;
import com.tzj.collect.entity.XcxSourceNum;
import com.tzj.collect.entity.XcxSourceTitle;
import com.tzj.collect.entity.XcxVisitDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@Transactional(readOnly=true)
public class XcxSourceNumServiceImpl extends ServiceImpl<XcxSourceNumMapper, XcxSourceNum> implements XcxSourceNumService {

    @Autowired
    private XcxSourceTitleService xcxSourceTitleService;
    @Resource
    private LineQrCodeService lineQrCodeService;
    @Autowired
    XcxVisitDetailService xcxVisitDetailService;

    @Override
    @Transactional
    public Object saveXcxSourceNum(String xcxCode,String aliUserId ) {
        XcxSourceTitle xcxSourceTitle = xcxSourceTitleService.selectOne(new EntityWrapper<XcxSourceTitle>().eq("code", xcxCode).eq("del_flag", 0));
        XcxSourceNum xcxSourceNum = this.selectOne(new EntityWrapper<XcxSourceNum>().eq("code", xcxCode).eq("del_flag", 0).eq("times", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        //存入明细
        if(!StringUtils.isEmpty(aliUserId)){
            XcxVisitDetail xcxVisitDetail =new XcxVisitDetail();
            xcxVisitDetail.setAliUserId(aliUserId);
            xcxVisitDetail.setCode(xcxCode);
            if(xcxSourceTitle!=null) {
                xcxVisitDetail.setName(xcxSourceTitle.getName());
            }
            xcxVisitDetailService.visit(xcxVisitDetail);
        }else{
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
        }

        LineQrCode lineQrCode = lineQrCodeService.selectOne(new EntityWrapper<LineQrCode>().eq("del_flag", 0).eq("share_code", xcxCode));
        try {
            if (null != lineQrCode){
                lineQrCode.setShareNum(lineQrCode.getShareNum()+1);
                lineQrCode.setUpdateDate(new Date());
                lineQrCodeService.updateById(lineQrCode);
            }
        }catch (Exception e){
        }
        return "操作成功";
    }
}
