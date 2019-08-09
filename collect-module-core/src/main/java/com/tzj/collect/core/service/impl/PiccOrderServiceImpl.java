package com.tzj.collect.core.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.taobao.api.ApiException;
import com.tzj.collect.api.commom.excel.ExcelData;
import com.tzj.collect.api.commom.excel.ExcelUtils;
import com.tzj.collect.common.excel.PiccOrderExcel;
import com.tzj.collect.core.mapper.PiccOrderMapper;
import com.tzj.collect.core.param.ali.PiccOrderBean;
import com.tzj.collect.core.service.PiccInsurancePolicyService;
import com.tzj.collect.core.service.PiccNumService;
import com.tzj.collect.core.service.PiccOrderService;
import com.tzj.collect.core.service.PiccWaterService;
import com.tzj.collect.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PiccOrderServiceImpl extends ServiceImpl<PiccOrderMapper, PiccOrder> implements PiccOrderService {
    @Autowired
    private PiccOrderMapper piccOrderMapper;
    @Autowired
    private PiccInsurancePolicyService piccInsurancePolicyService;
    @Autowired
    private PiccNumService piccNumService;
    @Autowired
    private PiccWaterService piccWaterService;

    @Transactional
    @Override
    public String insertPiccOrder(String aliUserId, PiccOrderBean piccOrderBean) throws ApiException{
        //根据保险单Id查询保单相关信息
        PiccInsurancePolicy piccInsurancePolicy = piccInsurancePolicyService.selectById(piccOrderBean.getInsuranceId());

        PiccOrder piccOrder = new PiccOrder();
        piccOrder.setAliUserId(aliUserId);
        piccOrder.setMemberName(piccOrderBean.getMemberName());
        piccOrder.setMemberTel(piccOrderBean.getMemberTel());
        piccOrder.setMemberAddress(piccOrderBean.getMemberAddress());
        piccOrder.setIdCard(piccOrderBean.getIdCard());
        piccOrder.setInsuranceId(piccOrderBean.getInsuranceId());
        piccOrder.setPiccCompanyId(piccInsurancePolicy.getPiccCompanyId());
        piccOrder.setInitPrice(piccInsurancePolicy.getInitPrice());
        piccOrder.setUnderwritingPrice(piccInsurancePolicy.getUnderwritingPrice());
        piccOrder.setStatus(PiccOrder.PiccOrderType.RECEIVE);
        try{
            this.insert(piccOrder);
        }catch (Exception e){
            throw new ApiException("保存保险订单失败");
        }
        return "操作成功";
    }

    @Transactional
    @Override
    public String deletePiccOrderList(String piccOrderIds) {
        if(!StringUtils.isBlank(piccOrderIds)){
            String[] id = piccOrderIds.split(",");
            for (int i = 0; i < id.length; i++) {
                PiccOrder piccOrder = this.selectById(id[i]);
                piccOrder.setDelFlag("1");
                this.updateById(piccOrder);
            }
            return "操作成功";
        }
        return "请输入Id";
    }

    @Override
    public Object selectPiccErrorOrderList(long piccCompanyId, PiccOrderBean piccOrderBean) {
        EntityWrapper<PiccOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("picc_company_id",piccCompanyId);
        wrapper.eq("del_flag",0);
        wrapper.eq("status_",1);
        if(!StringUtils.isBlank(piccOrderBean.getMemberName())){
            wrapper.like("member_name",piccOrderBean.getMemberName());
        }
        if(!StringUtils.isBlank(piccOrderBean.getStartTime())&&!StringUtils.isBlank(piccOrderBean.getEndTime())){
            wrapper.le("create_date", piccOrderBean.getEndTime()+" 23:59:59");
            wrapper.ge("create_date", piccOrderBean.getStartTime()+" 00:00:01");
        }
        if(!StringUtils.isBlank(piccOrderBean.getAuditingStartTime())&&!StringUtils.isBlank(piccOrderBean.getAuditingEndTime())){
            wrapper.le("auditing_date", piccOrderBean.getAuditingEndTime());
            wrapper.ge("auditing_date", piccOrderBean.getAuditingStartTime());
        }
        int count = this.selectCount(wrapper);
        wrapper.last(" LIMIT "+((piccOrderBean.getPageBean().getPageNumber()-1)*piccOrderBean.getPageBean().getPageSize())+","+piccOrderBean.getPageBean().getPageSize());
        wrapper.orderBy("create_date",false);
        List<PiccOrder> piccOrderList = this.selectList(wrapper);
        for (PiccOrder piccOrder:piccOrderList) {
            //手机号身份证号脱敏处理
            piccOrder.setMemberTel(piccOrder.getMemberTel().substring(0,2)+"*****"+piccOrder.getMemberTel().substring(piccOrder.getMemberTel().length()-3,piccOrder.getMemberTel().length()));
            piccOrder.setIdCard(piccOrder.getIdCard().substring(0,2)+"**********"+piccOrder.getIdCard().substring(piccOrder.getIdCard().length()-2,piccOrder.getIdCard().length()));
        }
        Map<String, Object> resultMap = piccNumService.selectPiccErrorNum(piccCompanyId);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNumber",piccOrderBean.getPageBean().getPageNumber());
        map.put("count",count);
        map.put("piccOrderList",piccOrderList);
        map.put("resultMap",resultMap);
        return map;
    }

    @Override
    public Object selectPiccSuccessOrderList(long piccCompanyId, PiccOrderBean piccOrderBean) {
        EntityWrapper<PiccOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("picc_company_id",piccCompanyId);
        wrapper.eq("del_flag",0);
        wrapper.eq("status_",2);
        if(!StringUtils.isBlank(piccOrderBean.getMemberName())){
            wrapper.like("member_name",piccOrderBean.getMemberName());
        }
        if(!StringUtils.isBlank(piccOrderBean.getStartTime())&&!StringUtils.isBlank(piccOrderBean.getEndTime())){
            wrapper.le("pick_start_time", piccOrderBean.getEndTime());
            wrapper.ge("pick_start_time", piccOrderBean.getStartTime());
        }
        if(!StringUtils.isBlank(piccOrderBean.getAuditingStartTime())&&!StringUtils.isBlank(piccOrderBean.getAuditingEndTime())){
            wrapper.le("pick_end_time", piccOrderBean.getAuditingEndTime());
            wrapper.ge("pick_end_time", piccOrderBean.getAuditingStartTime());
        }
        int count = this.selectCount(wrapper);
        wrapper.last(" LIMIT "+((piccOrderBean.getPageBean().getPageNumber()-1)*piccOrderBean.getPageBean().getPageSize())+","+piccOrderBean.getPageBean().getPageSize());
        wrapper.orderBy("create_date",false);
        List<PiccOrder> piccOrderList = this.selectList(wrapper);
        for (PiccOrder piccOrder:piccOrderList) {
            //手机号身份证号脱敏处理
            piccOrder.setMemberTel(piccOrder.getMemberTel().substring(0,2)+"*****"+piccOrder.getMemberTel().substring(piccOrder.getMemberTel().length()-3,piccOrder.getMemberTel().length()));
            piccOrder.setIdCard(piccOrder.getIdCard().substring(0,2)+"**********"+piccOrder.getIdCard().substring(piccOrder.getIdCard().length()-2,piccOrder.getIdCard().length()));
        }
        Map<String, Object> resultMap = piccNumService.selectPiccSuccessNum(piccCompanyId);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNumber",piccOrderBean.getPageBean().getPageNumber());
        map.put("count",count);
        map.put("piccOrderList",piccOrderList);
        map.put("resultMap",resultMap);
        return map;
    }

    @Override
    public Object selectPiccOrderList(long piccCompanyId, PiccOrderBean piccOrderBean) {
        EntityWrapper<PiccOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("picc_company_id",piccCompanyId);
        wrapper.eq("del_flag",0);
        wrapper.eq("status_",0);
        if(!StringUtils.isBlank(piccOrderBean.getMemberName())){
            wrapper.like("member_name",piccOrderBean.getMemberName());
        }
        if(!StringUtils.isBlank(piccOrderBean.getStartTime())&&!StringUtils.isBlank(piccOrderBean.getEndTime())){
            wrapper.le("create_date", piccOrderBean.getEndTime()+" 23:59:59");
            wrapper.ge("create_date", piccOrderBean.getStartTime()+" 00:00:01");
        }
        int count = this.selectCount(wrapper);
        wrapper.last(" LIMIT "+((piccOrderBean.getPageBean().getPageNumber()-1)*piccOrderBean.getPageBean().getPageSize())+","+piccOrderBean.getPageBean().getPageSize());
        wrapper.orderBy("create_date",true);
        List<PiccOrder> piccOrderList = this.selectList(wrapper);
        for (PiccOrder piccOrder:piccOrderList) {
            //手机号身份证号脱敏处理
            piccOrder.setMemberTel(piccOrder.getMemberTel().substring(0,2)+"*****"+piccOrder.getMemberTel().substring(piccOrder.getMemberTel().length()-3,piccOrder.getMemberTel().length()));
            piccOrder.setIdCard(piccOrder.getIdCard().substring(0,2)+"**********"+piccOrder.getIdCard().substring(piccOrder.getIdCard().length()-2,piccOrder.getIdCard().length()));
        }
        //查询累计导出用户数量，累计删除用户数和上次导出的数量
        Map<String, Object> resultMap = piccNumService.selectPiccNum(piccCompanyId);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("pageNumber",piccOrderBean.getPageBean().getPageNumber());
        map.put("count",count);
        map.put("piccOrderList",piccOrderList);
        map.put("resultMap",resultMap);
        return map;
    }

    @Override
    public Map<String,Object> piccOrderImportExcel(MultipartFile file) {
        //创建处理EXCEL的类
        PiccOrderExcel readExcel=new PiccOrderExcel();
        //解析excel，获取上传的事件单
        Map<String,Object> useList = readExcel.getExcelInfo(file);
        return useList;
    }

    @Transactional
    @Override
    public Object addPiccOrderExcel(PiccCompany piccCompany, List<PiccOrder> piccOrderList) {
        PiccOrder piccOrders = null;
        Integer successNum = 0;
        Integer errorNum = 0;
        for (PiccOrder piccOrder:piccOrderList) {
            piccOrders = this.selectOne(new EntityWrapper<PiccOrder>().eq("member_name", piccOrder.getMemberName()).eq("id_card", piccOrder.getIdCard()).eq("member_tel", piccOrder.getMemberTel()).eq("del_flag", 0));
            if(null==piccOrders){
                continue;
            }
            if(!StringUtils.isBlank(piccOrder.getInsuranceNum())&&null!=piccOrder.getPickEndTime()&&null!=piccOrder.getPickStartTime()){
                piccOrders.setStatus(PiccOrder.PiccOrderType. OPENING);
                piccOrders.setAuditingDate(new Date());
                piccOrders.setPickStartTime(piccOrder.getPickStartTime());
                piccOrders.setPickEndTime(piccOrder.getPickEndTime());
                piccOrders.setInsuranceNum(piccOrder.getInsuranceNum());
                successNum ++;
            }else {
                piccOrders.setStatus(PiccOrder.PiccOrderType. NOOPEN);
                piccOrders.setAuditingDate(new Date());
                piccOrders.setCancelReason(piccOrder.getCancelReason());
                errorNum ++;
            }
            this.updateById(piccOrders);
        }
        PiccNum piccNum = new PiccNum();
        piccNum.setPiccCompanyId(piccCompany.getId().intValue());
        piccNum.setInErrorNum(errorNum);
        piccNum.setInSuccessNum(successNum);
        piccNum.setInSuccessTime(new Date());
        piccNum.setInErrorTime(new Date());
        piccNumService.insert(piccNum);
        return "操作成功";
    }

    @Transactional
    @Override
    public String updatePiccWater(String aliUserId, Integer piccWaterId) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Integer pointCount = 0;
        if (piccWaterId == 0) {
            List<PiccWater> piccWaterList = piccWaterService.selectList(new EntityWrapper<PiccWater>().eq("ali_user_id", aliUserId).eq("del_flag", 0).eq("status_", 0).ge("point_count", 1));
            if (!piccWaterList.isEmpty()){
                for (PiccWater piccWater:piccWaterList){
                    piccWater.setStatus("1");
                    piccWaterService.updateById(piccWater);
                    pointCount += piccWater.getPointCount();
                }
            }
        }else {
            PiccWater piccWater = piccWaterService.selectById(piccWaterId);
            piccWater.setStatus("1");
            piccWaterService.updateById(piccWater);
            pointCount = piccWater.getPointCount();

        }

        PiccOrder piccOrder = this.selectOne(new EntityWrapper<PiccOrder>().eq("status_", 2).eq("del_flag", 0).eq("ali_user_id", aliUserId).ge("pick_end_time", df.format(date)));
        if (null!=piccOrder&&piccOrder.getInitPrice()<piccOrder.getUnderwritingPrice()){
            if (piccOrder.getInitPrice()+pointCount<=piccOrder.getUnderwritingPrice()){
                piccOrder.setInitPrice(piccOrder.getInitPrice()+pointCount);

            }else {
                piccOrder.setInitPrice(piccOrder.getUnderwritingPrice());
            }
            this.updateById(piccOrder);
        }
        return "操作成功";
    }

    @Transactional
    @Override
    public void outPiccOrderExcel(HttpServletResponse response, PiccOrderBean piccOrderBean) throws Exception{
        EntityWrapper<PiccOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("picc_company_id",piccOrderBean.getPiccCompanyId());
        wrapper.eq("del_flag",0);
        wrapper.eq("status_",0);
        if(!StringUtils.isBlank(piccOrderBean.getMemberName())){
            wrapper.like("member_name",piccOrderBean.getMemberName());
        }
        if(!StringUtils.isBlank(piccOrderBean.getStartTime())&&!StringUtils.isBlank(piccOrderBean.getEndTime())){
            wrapper.le("create_date", piccOrderBean.getEndTime()+" 23:59:59");
            wrapper.ge("create_date", piccOrderBean.getStartTime()+" 00:00:01");
        }
        List<PiccOrder> list = this.selectList(wrapper);

        ExcelData data = new ExcelData();
        data.setName("用户申请保单的数据");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("投保人姓名");
        titles.add("申请时间");
        titles.add("身份证号");
        titles.add("手机号");
        titles.add("居住地址");
        titles.add("保单号");
        titles.add("生效日期");
        titles.add("失效日期");
        titles.add("驳回原因");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<list.size();i++){
//            PiccOrder piccOrder = list.get(i);
//            piccOrder.setStatus(PiccOrder.PiccOrderType.WAIT);
//            this.updateById(piccOrder);
            row=new ArrayList();
            row.add(list.get(i).getMemberName());
            row.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreateDate()));
            row.add(list.get(i).getIdCard());
            row.add(list.get(i).getMemberTel());
            row.add(list.get(i).getMemberAddress());
            row.add(null);
            row.add(null);
            row.add(null);
            row.add(null);
            rows.add(row);

        }
        PiccNum piccNum = new PiccNum();
        piccNum.setPiccCompanyId(piccOrderBean.getPiccCompanyId());
        piccNum.setOutNum(list.size());
        piccNumService.insert(piccNum);
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }
}