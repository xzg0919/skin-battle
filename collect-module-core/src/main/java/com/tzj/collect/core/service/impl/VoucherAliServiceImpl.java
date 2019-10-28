package com.tzj.collect.core.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.entity.VoucherMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.VoucherAliMapper;
import com.tzj.collect.core.service.VoucherAliService;
import com.tzj.collect.core.service.VoucherCodeService;
import com.tzj.collect.entity.VoucherAli;
import com.tzj.collect.entity.VoucherCode;
/**
 *
 * <p>Created on2019年10月24日</p>
 * <p>Title:       [收呗]_[]_[]</p>
 * <p>Description: [支付宝券码券service实现类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢]
 * @version        1.0
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class VoucherAliServiceImpl extends ServiceImpl<VoucherAliMapper, VoucherAli> implements VoucherAliService
{
    @Resource
    private VoucherAliMapper VoucherAliMapper;
    @Resource
    private VoucherCodeService voucherCodeService;
    @Autowired
    private VoucherMemberService voucherMemberService;
    /**
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[生成券码]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    @Transactional
    public String makeCode(String id)
    {
        String returnStr = "ok";
        VoucherAli voucherAli = this.selectById(id);
        if(null == voucherAli)
        {
            returnStr = "券不存在!";
            return returnStr;
        }
        int size = voucherAli.getVoucherCount().intValue();
        VoucherCode voucherCode = null;
        String code = null;
        List<VoucherCode> voucherCodeList = new ArrayList<VoucherCode>();
        Date now = new Date();
        for(int i=1;i<=size;i++)
        {
            if(i % 5000 == 0)
            {
                voucherCodeService.insertBatch(voucherCodeList, 5000);
                voucherCodeList.clear();
            }
            voucherCode = new VoucherCode();
            voucherCode.setCreateBy(voucherAli.getCreateBy());
            voucherCode.setCreateDate(now);
            voucherCode.setDelFlag("0");
            voucherCode.setDis(voucherAli.getDis());
            voucherCode.setLowMoney(voucherAli.getLowMoney());
            voucherCode.setMoney(voucherAli.getMoney());
            voucherCode.setPickLimitTotal(voucherAli.getPickLimitTotal());
            voucherCode.setPickupEnd(voucherAli.getPickupEnd());
            voucherCode.setPickupStart(voucherAli.getPickupStart());
            voucherCode.setTopMoney(voucherAli.getTopMoney());
            voucherCode.setUpdateBy(voucherAli.getUpdateBy());
            voucherCode.setUpdateDate(now);
            voucherCode.setValidDay(voucherAli.getValidDay());
            voucherCode.setValidEnd(voucherAli.getValidEnd());
            voucherCode.setValidStart(voucherAli.getValidStart());
            voucherCode.setValidType(voucherAli.getValidType());
            voucherCode.setVersion(voucherAli.getVersion());
            voucherCode.setOrderType(voucherAli.getOrderType());
            voucherCode.setVoucherCount(voucherAli.getVoucherCount());
            voucherCode.setVoucherId(voucherAli.getId());
            voucherCode.setVoucherName(voucherAli.getVoucherName());
            voucherCode.setVoucherType(voucherAli.getVoucherType());
            code = UUID.randomUUID().toString();
            code = code.substring(code.lastIndexOf("-")+1,code.length());
            code = voucherAli.getVoucherType() + code;
            voucherCode.setVoucherCode(code);
            voucherCodeList.add(voucherCode);
        }
        if(size < 5000)
        {
            voucherCodeService.insertBatch(voucherCodeList, size);
        }
        voucherAli.setMaked("1");
        this.updateById(voucherAli);
        return returnStr;
    }
    /**
     * <p>Created on 2019年10月26日</p>
     * <p>Description:[更新领取数]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    @Transactional
    public void updatePickCount(Long voucherId)
    {
        VoucherAliMapper.updatePickCount(voucherId);
        
    }


    /**
     * 根据券的Id和最初的价格计算券优惠后的金额
     * @param price
     * @param voucherId
     * @return
     */
    @Override
    public BigDecimal getDiscountPriceByVoucherId(BigDecimal price, String voucherId){
        //优惠价格
        BigDecimal discountPrice = price;
        VoucherMember voucherMember = voucherMemberService.selectById(voucherId);
        if (null==voucherMember){
            System.out.println("----------------------该券不存在，voucherId："+voucherId);
            return  price;
        }
        Date now = new Date();
        if (now.before(voucherMember.getValidStart())||now.after(voucherMember.getValidEnd())){
            System.out.println("----------------------该券不在使用时间内，voucherId："+voucherId);
            return  price;
        }
        if (price.compareTo(new BigDecimal(voucherMember.getLowMoney())) < 1){
            return price;
        }
        switch (voucherMember.getVoucherType()){
            case "D":
                discountPrice = price.multiply((new BigDecimal(1)).subtract(voucherMember.getDis().multiply(new BigDecimal(0.1))));
                break;
            case "P":
                discountPrice = price.multiply(voucherMember.getDis()).subtract(price);
                break;
            case "E":
                discountPrice = new BigDecimal(voucherMember.getMoney());
                break;
            case "R":
                discountPrice = new BigDecimal(voucherMember.getMoney());
                break;
             default:
        }
        if (new BigDecimal(voucherMember.getTopMoney()).compareTo(discountPrice) < 1){
            discountPrice =  new BigDecimal(voucherMember.getTopMoney());
        }
        if ("D".equals(voucherMember.getVoucherType())||"E".equals(voucherMember.getVoucherType())){
            price = price.subtract(discountPrice);
        }
        if ("P".equals(voucherMember.getVoucherType())||"R".equals(voucherMember.getVoucherType())){
            price = price.add(discountPrice);
        }
        return price;
    }
    /**
     * <p>Created on 2019年10月26日</p>
     * <p>Description:[更新核销数]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    @Transactional
    public void updateUseCount(Long voucherId)
    {
        VoucherAliMapper.updateUseCount(voucherId);
        
    }

}