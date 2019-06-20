package com.tzj.collect.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.PiccNum;
import com.tzj.collect.entity.PiccOrder;
import com.tzj.collect.mapper.PiccNumMapper;
import com.tzj.collect.service.PiccNumService;
import com.tzj.collect.service.PiccOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PiccNumServiceImpl extends ServiceImpl<PiccNumMapper,PiccNum> implements PiccNumService {

    @Autowired
    private PiccNumMapper piccNumMapper;
    @Autowired
    private PiccOrderService piccOrderService;

    @Override
    @DS("slave")
    public Map<String, Object> selectPiccNum(long piccConpanyId) {
            Integer sumOutNum = 0;
            Integer sumDeleteNum = 0;
            Integer outNum = 0;
        //查询总导出数量
        sumOutNum = piccNumMapper.selectSumOutNum(piccConpanyId);
        //查询上次导出数量
        outNum = piccNumMapper.selectOutNum(piccConpanyId);
        //查询总删除数量
        sumDeleteNum = piccOrderService.selectCount(new EntityWrapper<PiccOrder>().eq("picc_company_id",piccConpanyId).eq("del_flag",1));

        Map<String, Object> map = new HashMap<>();
        map.put("sumOutNum",sumOutNum==null?0:sumOutNum);
        map.put("sumDeleteNum",sumDeleteNum);
        map.put("outNum",outNum==null?0:outNum);
        return map;
    }
    @Override
    @DS("slave")
    public Map<String, Object> selectPiccErrorNum(long piccConpanyId) {
        Integer sumErrorNum = 0;
        Integer errorNum = 0;
        //查询总未通过审核的人数
        sumErrorNum = piccNumMapper.selectSumErrorNum(piccConpanyId);
        //查询上一次未通过审核的人数
        errorNum = piccNumMapper.selectErrorNum(piccConpanyId);

        Map<String, Object> map = new HashMap<>();
        map.put("sumErrorNum",sumErrorNum==null?0:sumErrorNum);
        map.put("errorNum",errorNum==null?0:errorNum);
        return map;
    }

    @Override
    @DS("slave")
    public Map<String, Object> selectPiccSuccessNum(long piccConpanyId) {
        Integer sumSuccessNum = 0;
        Integer successNum = 0;
        Integer invalidNum = 0;

        //查询总通过审核的人数
        sumSuccessNum = piccNumMapper.selectSumSuccessNum(piccConpanyId);
        //查询上一次通过审核的人数
        successNum = piccNumMapper.selectSuccessNum(piccConpanyId);
        //查询近七天失效的用户数
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        Date today = calendar.getTime();
        invalidNum = piccNumMapper.selectInvalidNum(piccConpanyId,df.format(date),df.format(today));

        Map<String, Object> map = new HashMap<>();
        map.put("sumSuccessNum",sumSuccessNum==null?0:sumSuccessNum);
        map.put("successNum",successNum==null?0:successNum);
        map.put("invalidNum",invalidNum==null?0:invalidNum);
        return map;
    }

}
