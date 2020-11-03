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

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@Transactional(readOnly=true)
public class XcxVisitDetailServiceImpl extends ServiceImpl<XcxVisitDetailMapper, XcxVisitDetail> implements XcxVisitDetailService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void visit(XcxVisitDetail xcxVisitDetail) {
        this.baseMapper.insert(xcxVisitDetail);
    }
}
