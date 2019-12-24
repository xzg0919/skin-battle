package com.tzj.collect.core.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenAppQrcodeCreateModel;
import com.alipay.api.request.AlipayOpenAppQrcodeCreateRequest;
import com.alipay.api.response.AlipayOpenAppQrcodeCreateResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.common.constant.AlipayConst;
import com.tzj.collect.core.mapper.LineQrCodeMapper;
import com.tzj.collect.core.param.admin.AdminShareCodeBean;
import com.tzj.collect.core.service.LineQrCodeRangeService;
import com.tzj.collect.core.service.LineQrCodeService;
import com.tzj.collect.entity.LineQrCode;
import com.tzj.collect.entity.LineQrCodeRange;
import com.tzj.module.easyopen.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author 胡方明（12795880@qq.com）
 **/
@Service
@Transactional(readOnly = true)
public class LineQrCodeServiceImpl extends ServiceImpl<LineQrCodeMapper, LineQrCode> implements LineQrCodeService {

    @Resource
    private LineQrCodeRangeService lineQrCodeRangeService;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> createShareCode(AdminShareCodeBean adminShareCodeBean) {
        Map<String, Object> returnMap = new HashMap<>();
        LineQrCode lineQrCode = null;
        if (StringUtils.isEmpty(adminShareCodeBean.getQrName())){
            throw new ApiException("分享码名称不能为空");
        }else {
            lineQrCode = new LineQrCode();
            lineQrCode.setName(adminShareCodeBean.getQrName());
        }
        if (StringUtils.isEmpty(adminShareCodeBean.getQrCodeInfo())){
            throw new ApiException("分享码描述不能为空");
        }else {
            lineQrCode.setQrCodeInfo(adminShareCodeBean.getQrCodeInfo());
        }
        lineQrCode.setShareCode(UUID.randomUUID().toString());
        lineQrCode.setShareNum(0);
        if (null != adminShareCodeBean.getQrType() && LineQrCode.QrType.OFFLINE.name().equals(adminShareCodeBean.getQrType().name())){
            lineQrCode.setQrType(LineQrCode.QrType.OFFLINE);
            lineQrCode.setQrUrl(this.qrUrl(LineQrCode.QrType.OFFLINE, lineQrCode.getShareCode()));
            //线下码
            if (!CollectionUtils.isEmpty(adminShareCodeBean.getAdminCityList())){
                LineQrCode finalLineQrCode = lineQrCode;
                adminShareCodeBean.getAdminCityList().stream().forEach(cityList ->{
                    //保存地址
                    LineQrCodeRange lineQrCodeRange = new LineQrCodeRange();
                    lineQrCodeRange.setAreaId(cityList.getAreaId());
                    lineQrCodeRange.setCityId(cityList.getCityId());
                    lineQrCodeRange.setProvinceId(cityList.getProvinceId());
                    lineQrCodeRange.setStreetId(cityList.getStreetId());
                    lineQrCodeRange.setAreaName(cityList.getAreaName());
                    lineQrCodeRange.setCityName(cityList.getCityName());
                    lineQrCodeRange.setProvinceName(cityList.getProvinceName());
                    lineQrCodeRange.setStreetName(cityList.getStreetName());
                    lineQrCodeRange.setShareCode(finalLineQrCode.getShareCode());
                    lineQrCodeRangeService.insert(lineQrCodeRange);
                });
            }else {
                throw new ApiException("线下码地址不能为空");
            }
        }else if (null != adminShareCodeBean.getQrType() && LineQrCode.QrType.ONLINE.name().equals(adminShareCodeBean.getQrType().name())){
            //线上码
            lineQrCode.setQrType(LineQrCode.QrType.ONLINE);
            lineQrCode.setQrUrl(this.qrUrl(LineQrCode.QrType.ONLINE, lineQrCode.getShareCode()));
        }else {
            throw new ApiException("参数错误");
        }
        System.out.println(lineQrCode.getQrUrl());
        if(this.insert(lineQrCode)){
            returnMap.put("msg", "Y");
        }else {
            returnMap.put("msg", "N");
        }
        return returnMap;
    }
    /**
     * 分享码Page
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/23 0023
     * @Param: 
     * @return: 
     */
    @Override
    public Page<LineQrCode> lineQrCodePage(AdminShareCodeBean adminShareCodeBean) {
        if (StringUtils.isEmpty(adminShareCodeBean.getQrType())){
            throw new ApiException("请选择分享码类型");
        }
        Page<LineQrCode> pages = new Page<LineQrCode>(adminShareCodeBean.getPageBean().getPageNumber(), adminShareCodeBean.getPageBean().getPageSize());
        EntityWrapper<LineQrCode> wrapper = new EntityWrapper<LineQrCode>();
        wrapper.in("qr_type", adminShareCodeBean.getQrType().getValue());
        if (!StringUtils.isEmpty(adminShareCodeBean.getQrName())){
           wrapper.like("name_", adminShareCodeBean.getQrName()+"%");
        }
        wrapper.orderBy("id", false);
        return this.selectPage(pages, wrapper);
    }
    /**
     *
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/23 0023
     * @Param: 
     * @return: 
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> lineQrCodeDel(AdminShareCodeBean adminShareCodeBean) {
        Map<String, Object> returnMap = new HashMap<>();
        LineQrCode lineQrCode = this.selectOne(new EntityWrapper<LineQrCode>().eq("del_flag", 0).eq("share_code", adminShareCodeBean.getQrCode()).last(" limit 1"));
        if (null != lineQrCode){
            lineQrCode.setDelFlag("1");
            if(this.updateById(lineQrCode)){
                returnMap.put("msg", "Y");
            }else {
                returnMap.put("msg", "N");
            }
        }else {
            returnMap.put("msg", "N");
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> lineQrCodeDetail(AdminShareCodeBean adminShareCodeBean) {
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isEmpty(adminShareCodeBean.getQrCode())){
            throw new ApiException("参数错误");
        }
        Wrapper entityWrapper = new EntityWrapper<LineQrCodeRange>().eq("del_flag", 0).eq("share_code", adminShareCodeBean.getQrCode());
        String sqlSelect = "province_id AS provinceId, province_name AS provinceName";
        String sqlLast = " GROUP BY provinceId";
        if (!StringUtils.isEmpty(adminShareCodeBean.getAreaId())){
            sqlSelect = "street_id as streetId, street_name as streetName";
            entityWrapper.eq("area_id", adminShareCodeBean.getAreaId());
            sqlLast = " GROUP BY streetId";
        }else if (!StringUtils.isEmpty(adminShareCodeBean.getCityId())){
            sqlSelect = "area_id as areaId, area_name as areaName";
            entityWrapper.eq("city_id", adminShareCodeBean.getCityId());
            sqlLast = " GROUP BY areaId";
        }else if (!StringUtils.isEmpty(adminShareCodeBean.getProvinceId())){
            sqlSelect = "city_id AS cityId, city_name AS cityName";
            entityWrapper.eq("province_id", adminShareCodeBean.getProvinceId());
            sqlLast = " GROUP BY cityId";
        }
        LineQrCode lineQrCode = this.selectOne(new EntityWrapper<LineQrCode>().eq("del_flag", 0).eq("share_code", adminShareCodeBean.getQrCode()).last(" limit 1"));
        if (null == lineQrCode){
            throw new ApiException("当前数据不存在");
        }
        returnMap.put("qrCodeInfo", lineQrCode.getQrCodeInfo());
        if (LineQrCode.QrType.OFFLINE.name().equals(lineQrCode.getQrType().name())){
            //线下码，增加地址
            List<Map<String, Object>> priList = lineQrCodeRangeService.selectMaps(entityWrapper.setSqlSelect(sqlSelect).last(sqlLast));
            returnMap.put("priList", priList);
        }
        return returnMap;
    }

    /**
     * 获取先上下分享码链接
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/23 0023
     * @Param: 
     * @return: 
     */
    private String qrUrl(LineQrCode.QrType qrType, String shareCode) {
        if (LineQrCode.QrType.OFFLINE.name().equals(qrType.name())){
            String qrUrl = "alipays://platformapi/startapp?appId=2018060660292753&query=sourceId%3D"+shareCode+"&page=pages/view/index/index";
            return qrUrl;
        }else {
            return this.getXcxUri("pages/view/index/index",shareCode, shareCode, shareCode);
//            return "";
        }
    }
    public  String getXcxUri(String urlParam, String id, String type,String channelId){
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConst.serverUrl, AlipayConst.XappId, AlipayConst.private_key, AlipayConst.format, AlipayConst.input_charset, AlipayConst.ali_public_key, AlipayConst.sign_type);
        AlipayOpenAppQrcodeCreateRequest request = new AlipayOpenAppQrcodeCreateRequest();
        AlipayOpenAppQrcodeCreateModel model = new AlipayOpenAppQrcodeCreateModel();
        model.setUrlParam(urlParam);
        String queryParam = "";
        if(org.apache.commons.lang.StringUtils.isNotBlank(id)){
            queryParam += "sourceId="+id +"&";
        }
        model.setQueryParam(queryParam.substring(0,queryParam.length()-1));
        model.setDescribe("二维码的链接");
        request.setBizModel(model);
        AlipayOpenAppQrcodeCreateResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return response.getQrCodeUrl();
    }
}
