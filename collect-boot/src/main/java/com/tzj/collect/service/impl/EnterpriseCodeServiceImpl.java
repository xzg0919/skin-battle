package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.enterprise.param.EnterpriseCodeBean;
import com.tzj.collect.entity.EnterpriseCode;
import com.tzj.collect.entity.EnterpriseProduct;
import com.tzj.collect.entity.EnterpriseTerminal;
import com.tzj.collect.mapper.EnterpriseCodeMapper;
import com.tzj.collect.service.AsyncService;
import com.tzj.collect.service.EnterpriseCodeService;
import com.tzj.collect.service.EnterpriseProductService;
import com.tzj.collect.service.EnterpriseTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class EnterpriseCodeServiceImpl extends ServiceImpl<EnterpriseCodeMapper,EnterpriseCode> implements EnterpriseCodeService {

    @Autowired
    private EnterpriseCodeMapper enterpriseCodeMapper;
    @Autowired
    private EnterpriseTerminalService enterpriseTerminalService;
    @Autowired
    private EnterpriseProductService enterpriseProductService;
    @Autowired
    private AsyncService asyncService;

    /**
     * 新增以旧换新的相关券的信息
     * wangcan
     * @param
     * @return
     */
    @Override
    @Transactional
    public Object saveEnterpriseCode(long erminalId,EnterpriseCodeBean enterpriseCodeBean) throws ApiException{

        //随机生成券码
        String code = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(new Random().nextInt(9));
        //根据终端id查询相关信息
        EnterpriseTerminal enterpriseTerminal = enterpriseTerminalService.selectById(erminalId);
        if(enterpriseTerminal==null){
            throw new ApiException("未查询到该用户");
        }
        //根据商品Id查询商品信息
        EnterpriseProduct enterpriseProduct = enterpriseProductService.selectById(enterpriseCodeBean.getProductId());

        EnterpriseCode enterpriseCode = new EnterpriseCode();
        enterpriseCode.setEnterpriseId(enterpriseTerminal.getEnterpriseId());
        enterpriseCode.setTerminalId(enterpriseTerminal.getId().intValue());
        enterpriseCode.setCode(code);
        enterpriseCode.setPrice(enterpriseProduct.getSubsidiesPrice());
        enterpriseCode.setProductId(Integer.parseInt(enterpriseCodeBean.getProductId()));
        enterpriseCode.setProductName(enterpriseProduct.getName());
        enterpriseCode.setCustomerName(enterpriseCodeBean.getCustomerName());
        enterpriseCode.setCustomerTel(enterpriseCodeBean.getCustomerTel());
        enterpriseCode.setCustomerIdcard(enterpriseCodeBean.getCustomerIdcard());
        enterpriseCode.setInvoiceCode(enterpriseCodeBean.getInvoiceCode());
        enterpriseCode.setInvoicePic(enterpriseCodeBean.getInvoicePic());
        enterpriseCode.setIsUse("0");
        enterpriseCode.setCategoryId(null);
        try{
            this.insert(enterpriseCode);
        }catch (Exception e){
            throw  new ApiException("保存失败");
        }
        //发送接单短信
        asyncService.sendEnterprise("垃圾分类回收", enterpriseCodeBean.getCustomerTel(), "SMS_154588941", code,enterpriseProduct.getName() );
        return "操作成功";
    }

    /**
     * 以旧换新的券的列表
     * 王灿
     * @param enterpriseCodeBean
     * @return
     */
    @Override
    public Object enterpriseCodeList(EnterpriseCodeBean enterpriseCodeBean,Integer enterpriseId)throws ApiException{
        List<Map<String,Object>>  list = enterpriseCodeMapper.enterpriseCodeList(enterpriseCodeBean.getStartTime(),enterpriseCodeBean.getEndTime(),enterpriseCodeBean.getIsUse(),enterpriseId,(enterpriseCodeBean.getPageBean().getPageNumber()-1)*enterpriseCodeBean.getPageBean().getPageSize(),enterpriseCodeBean.getPageBean().getPageSize());
        Integer count = enterpriseCodeMapper.enterpriseCodeListCount(enterpriseCodeBean.getStartTime(), enterpriseCodeBean.getEndTime(), enterpriseCodeBean.getIsUse(), enterpriseId);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("enterpriseCodeList",list);
        map.put("count",count);
        map.put("pageNumber",enterpriseCodeBean.getPageBean().getPageNumber());
        return  map;
    }
    /**
     * 旧换新的券的excel列表
     * 王灿
     * @param enterpriseCodeBean
     * @return
     */
    @Override
    public List<Map<String,Object>> outEnterpriseCodeExcel(EnterpriseCodeBean enterpriseCodeBean,Integer enterpriseId)throws ApiException{
        List<Map<String,Object>>  list = enterpriseCodeMapper.outEnterpriseCodeExcel(enterpriseCodeBean.getStartTime(),enterpriseCodeBean.getEndTime(),enterpriseCodeBean.getIsUse(),enterpriseId);
        return  list;
    }

    /**
     * 以旧换新的券的详情
     * 王灿
     * @param enterpriseCodeId
     * @return
     */
    @Override
    public Object enterpriseCodeDetil(String enterpriseCodeId){
       return enterpriseCodeMapper.enterpriseCodeDetil(enterpriseCodeId);
    }
}
