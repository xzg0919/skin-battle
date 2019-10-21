package com.tzj.collect.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.commom.excel.ExcelData;
import com.tzj.collect.api.commom.excel.ExcelUtils;
import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.common.util.SnUtils;
import com.tzj.collect.core.param.ali.AreaBean;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.PiccOrderBean;
import com.tzj.collect.core.param.ali.RecruitExpressBean;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.collect.core.param.enterprise.EnterpriseCodeBean;
import com.tzj.collect.core.result.admin.RecruitExpressResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.EnterpriseCode;
import com.tzj.collect.entity.OrderComplaint;
import com.tzj.collect.entity.OrderItem;
import com.tzj.collect.entity.OrderItemAch;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("out/excel")
public class OutExcelController {

    @Autowired
    private RecruitExpressService recruitExpressService;
    @Autowired
    private EnterpriseCodeService enterpriseCodeService;
    @Autowired
    private PiccOrderService piccOrderService;
    @Autowired
    private PiccNumService piccNumService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderComplaintService orderComplaintService;
    @Autowired
    private OrderItemAchService orderItemAchService;
    @Autowired
    private AreaService areaService;
    @Resource
    private JedisPool jedisPool;
    /**
     * 根据企业导出以旧换新的券的excel列表
     * @param response
     * @param enterpriseCodeBean
     * @throws Exception
     */
    @RequestMapping("/outEnterpriseCodeExcel")
    public void  outEnterpriseCodeExcel(HttpServletResponse response, EnterpriseCodeBean enterpriseCodeBean) throws Exception{
        List<Map<String, Object>> list = enterpriseCodeService.outEnterpriseCodeExcel(enterpriseCodeBean, Integer.parseInt(enterpriseCodeBean.getId()));
        ExcelData data = new ExcelData();
        data.setName("以旧换新信息数据");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("购买时间");
        titles.add("购买者姓名");
        titles.add("购买者手机号");
        titles.add("购买产品名称");
        titles.add("销售终端名称");
        titles.add("补贴金额");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<list.size();i++){
            row=new ArrayList();
            row.add(list.get(i).get("createDate"));
            row.add(list.get(i).get("customerName"));
            row.add(list.get(i).get("customerTel"));
            row.add(list.get(i).get("productName"));
            row.add(list.get(i).get("terminalName"));
            row.add(new BigDecimal(list.get(i).get("price")+"").setScale(2, BigDecimal.ROUND_DOWN));
            rows.add(row);

        }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    /**
     * 根据终端导出以旧换新的券的excel列表
     * @param response
     * @param enterpriseCodeBean
     * @throws Exception
     */
    @RequestMapping("/outEnterpriseTerminalCodeExcel")
    public void outEnterpriseTerminalCodeExcel(HttpServletResponse response,EnterpriseCodeBean enterpriseCodeBean)throws Exception {
        EntityWrapper wrapper = new EntityWrapper<EnterpriseCode>();
        wrapper.eq("terminal_id",enterpriseCodeBean.getId());
        wrapper.eq("del_flag",0);
        if(!StringUtils.isBlank(enterpriseCodeBean.getStartTime())&&!StringUtils.isBlank(enterpriseCodeBean.getEndTime())){
            wrapper.le("create_date", enterpriseCodeBean.getEndTime()+" 23:59:59");
            wrapper.ge("create_date", enterpriseCodeBean.getStartTime()+" 00:00:01");
        }
        if (!StringUtils.isBlank(enterpriseCodeBean.getIsUse())) {
            if("0".equals(enterpriseCodeBean.getIsUse())){
                wrapper.in("is_use","0,1");
            }else{
                wrapper.eq("is_use",enterpriseCodeBean.getIsUse());
            }
        }
        wrapper.orderBy("create_date",true);
        List<EnterpriseCode> list = enterpriseCodeService.selectList(wrapper);

        ExcelData data = new ExcelData();
        data.setName("以旧换新信息数据");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("购买时间");
        titles.add("购买者姓名");
        titles.add("购买者手机号");
        titles.add("购买产品名称");
        titles.add("补贴金额");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<list.size();i++){
            row=new ArrayList();
            row.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreateDate()));
            row.add(list.get(i).getCustomerName());
            row.add(list.get(i).getCustomerTel());
            row.add(list.get(i).getProductName());
            row.add(list.get(i).getPrice().setScale(2, BigDecimal.ROUND_DOWN));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }
    /**
     * picc导出用户申请保单的excel列表
     * @param response
     * @param piccOrderBean
     * @throws Exception
     */
    @RequestMapping("/outPiccOrderExcel")
    public void outPiccOrderExcel(HttpServletResponse response, PiccOrderBean piccOrderBean)throws Exception {

        piccOrderService.outPiccOrderExcel(response,piccOrderBean);
    }

    /**
     * 完成订单列表
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/outOrderExcel")
    public void outOrderExcel(HttpServletResponse response, OrderBean orderBean)throws Exception {
        //五废订单需要单独格式导出
        if ( !"2".equals(orderBean.getType())) {
            List<Map<String, Object>> list = orderService.outOrderExcel(orderBean.getId(), orderBean.getType(), orderBean.getStartTime(), orderBean.getEndTime());
            //添加表头
            List<String> titles = new ArrayList<>();
            //for(String title: excelInfo.getNames())
            ExcelData data = new ExcelData();
            data.setName("完成订单");
            titles.add("订单号");
            titles.add("下单日期");
            titles.add("服务商公司");
            titles.add("大分类");
            titles.add("小分类");
            titles.add("付款方式");
            titles.add("成交金额/重量");
            titles.add("回收人员");
            data.setTitles(titles);
            //添加列
            List<List<Object>> rows = new ArrayList();
            List<Object> row = null;
            for (int i = 0; i < list.size(); i++) {
                row = new ArrayList();
                row.add(list.get(i).get("orderNo"));
                row.add(list.get(i).get("createDate"));
                row.add(list.get(i).get("name"));
                row.add(list.get(i).get("parentName"));
                row.add(list.get(i).get("categoryName"));
                row.add(list.get(i).get("isCash"));
                row.add(list.get(i).get("amount"));
                row.add(list.get(i).get("recyclerName"));
                rows.add(row);
            }
            data.setRows(rows);
            SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String fileName = fdate.format(new Date()) + ".xlsx";
            ExcelUtils.exportExcel(response, fileName, data);
        }else {
            List<Map<String, Object>> list = orderService.
                    orderDetail4HorseHold(orderBean.getId(),orderBean.getStartTime(), orderBean.getEndTime());
            //添加表头
            List<String> titles = new ArrayList<>();
            ExcelData data = new ExcelData();
            data.setName("完成订单");
            titles.add("订单号");
            titles.add("下单时间");
            titles.add("公司");
            titles.add("一级类目");
            titles.add("二级类目");
            titles.add("计费方式");
            titles.add("金额");
            titles.add("重量");
            titles.add("回收人员");
            data.setTitles(titles);

            List<List<Object>> rows = new ArrayList();
            List<Object> row = null;
            for (int i = 0; i < list.size(); i++) {
                row = new ArrayList();
                row.add(list.get(i).get("orderNo"));
                row.add(list.get(i).get("createDate"));
                row.add(list.get(i).get("company"));
                row.add(list.get(i).get("firstCate"));
                row.add(list.get(i).get("secondCate"));
                row.add(list.get(i).get("cash"));
                row.add(list.get(i).get("price"));
                row.add(list.get(i).get("weight"));
                row.add(list.get(i).get("recycleName"));
                rows.add(row);
            }
            data.setRows(rows);
            SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String fileName = fdate.format(new Date()) + ".xlsx";
            ExcelUtils.exportExcel(response, fileName, data);
        }
    }

    @RequestMapping("/getRecruitListOutExcel")
    public void  getRecruitListOutExcel(HttpServletResponse response, RecruitExpressBean recruitExpressBean) throws Exception{
        List<RecruitExpressResult> recruitList = recruitExpressService.getRecruitListOutExcel(recruitExpressBean);
        ExcelData data = new ExcelData();
        data.setName("以旧换新信息数据");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("加入方式");
        titles.add("合作方式");
        titles.add("提交时间");
        titles.add("企业名称");
        titles.add("联系人姓名");
        titles.add("联系人电话");
        titles.add("回收类型");
        titles.add("意向城市");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<recruitList.size();i++){
            row=new ArrayList();
            row.add(recruitList.get(i).getType());
            row.add(recruitList.get(i).getCooperationType());
            row.add(recruitList.get(i).getCreateDate());
            row.add(recruitList.get(i).getEnterprise());
            row.add(recruitList.get(i).getName());
            row.add(recruitList.get(i).getMobile());
            row.add(recruitList.get(i).getCategoryType());
            row.add(recruitList.get(i).getCity());
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }
    @RequestMapping("/getRecyclerOrderList")
    public void  getRecyclerOrderList(HttpServletResponse response, OrderBean orderBean) throws Exception{

        List<Map<String,Object>> list = orderService.getRecyclerOrderList(orderBean);
        ExcelData data = new ExcelData();
        data.setName("正常订单数据");
        if ("1".equals(orderBean.getIsOverTime())){
            data.setName("超时订单数据");
        }
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("回收人员姓名");
        titles.add("回收人员电话");
        titles.add("订单号");
        titles.add("下单时间");
        titles.add("预约时间");
        titles.add("订单状态");
        titles.add("回收品类");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<list.size();i++){
            row=new ArrayList();
            row.add(list.get(i).get("recyclerName"));
            row.add(list.get(i).get("recyclerTel"));
            row.add(list.get(i).get("orderNo"));
            row.add(list.get(i).get("createDate"));
            row.add(list.get(i).get("arrivalTime"));
            row.add(this.getOrderStatus(list.get(i).get("status").toString()));
            row.add(list.get(i).get("categoryName"));
            rows.add(row);

        }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }
    @RequestMapping("/getOdrerCancleExamineList")
    public void  getOdrerCancleExamineList(HttpServletResponse response, OrderBean orderBean) throws Exception{
        Map<String,Object> resultMap = (Map<String,Object>)orderService.getOrderCancleExamineList(orderBean);
        List<Map<String,Object>> list = (List<Map<String,Object>>)resultMap.get("overTimeOrderList");
        ExcelData data = new ExcelData();
        data.setName("取消申请订单列表");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("申请时间");
        titles.add("订单编号");
        titles.add("回收物类型");
        titles.add("客户姓名");
        titles.add("客户电话");
        titles.add("服务商");
        titles.add("取消理由");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<list.size();i++){
            row=new ArrayList();
            row.add(list.get(i).get("createDate"));
            row.add(list.get(i).get("orderNo"));
            row.add(list.get(i).get("title"));
            row.add(list.get(i).get("linkMan"));
            row.add(list.get(i).get("tel"));
            row.add(list.get(i).get("name"));
            row.add(list.get(i).get("cancleReason"));
            rows.add(row);

        }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }
    @RequestMapping("/getCompanyServiceOutList")
    public void  getCompanyServiceOutList(HttpServletResponse response, AreaBean areaBean) throws Exception{
        List<Map<String,Object>> list = areaService.getCompanyServiceOutList(areaBean);
        ExcelData data = new ExcelData();
        data.setName("服务商服务范围表");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("服务商名称");
        titles.add("回收类型");
        titles.add("省");
        titles.add("市");
        titles.add("区");
        titles.add("街道");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<list.size();i++){
            row=new ArrayList();
            row.add(list.get(i).get("companyName"));
            row.add(list.get(i).get("title"));
            row.add(list.get(i).get("province"));
            row.add(list.get(i).get("cityName"));
            row.add(list.get(i).get("areaName"));
            row.add(list.get(i).get("streetName"));
            rows.add(row);

        }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }


















    public String getOrderStatus(String status){
        String statusPage = null;
        switch (status) {
            case "1":
                statusPage = "待接单";
                break;
            case "3":
                statusPage = "已完成";
                break;
            case "4":
                statusPage = "已取消";
                break;
            case "2":
                statusPage = "进行中";
                break;
            case "5":
                statusPage = "平台已取消";
                break;
            case "0":
                statusPage = "待处理";
                break;
            default:
                break;
        }
        return statusPage;
    }

    /** 【业务数据总览】 已完成和其他状态订单分sheet导出
      * @author sgmark@aliyun.com
      * @date 2019/9/10 0010
      * @param
      * @return
      */
    @RequestMapping("/out/order/for/overview")
    public void  outAllOrderMapOverview(HttpServletResponse response, BOrderBean bOrderBean) throws Exception{
        if (StringUtils.isEmpty(bOrderBean.getStartTime()) || StringUtils.isEmpty(bOrderBean.getEndTime())){
            throw new ApiException("缺少开始或结束时间");
        }else if (null == bOrderBean.getCompanyId()){
            throw new ApiException("缺少公司id");
        }
        String type = null;
        if (StringUtils.isEmpty(bOrderBean.getCategoryType())){
            type = "0";
        }else {
            type = bOrderBean.getCategoryType();
        }
        //限制各个类型每天导出一次
        String redisKeyName = LocalDate.now().getYear()+":"+LocalDate.now().getDayOfYear()+":"+bOrderBean.getCompanyId()+":"+ type;
        RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
        if (null == saveOrGetFromRedis.getFromRedis(redisKeyName, jedisPool)){
            saveOrGetFromRedis.saveInRedis(redisKeyName,System.currentTimeMillis(), 24*3600, jedisPool);
        }else {
            throw new ApiException("今日已经导出过，不能再执行此操作");
        }
        List<Map<String, Object>> achList = orderService.outOtherOrderListOverview(bOrderBean);
        List<Map<String, Object>> otherList  = orderService.outAchOrderListOverview(bOrderBean);
        List<ExcelData> allExcelData = new ArrayList<>();
        ExcelData otherData = new ExcelData();
        ExcelData achData = new ExcelData();
        achData.setName("sheet1");
        otherData.setName("sheet2");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("订单号");
        titles.add("省");
        titles.add("市");
        titles.add("区");
        titles.add("街道");
        titles.add("类目");
        titles.add("订单状态");
        titles.add("成交金额");
        titles.add("数量");
        titles.add("是否为客诉");
        titles.add("客诉原因");
        otherData.setTitles(titles);
        achData.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<otherList.size();i++){
            Map<String, Object> complaint = orderService.getOrderComplaint(otherList.get(i).get("order_no") + "");
            String complaints = "";
            row=new ArrayList();
            row.add(otherList.get(i).get("order_no"));
            row.add(otherList.get(i).get("caName"));
            row.add(otherList.get(i).get("cityName"));
            row.add(otherList.get(i).get("areaName"));
            row.add(otherList.get(i).get("streetName"));
            row.add(otherList.get(i).get("category_name"));
            row.add(otherList.get(i).get("status_"));
            row.add(otherList.get(i).get("ach_price"));
            row.add(otherList.get(i).get("amount"));
            row.add("0".equals(otherList.get(i).get("isComplaint")+"")?"不是":"是");
            if("3".equals(complaint.get("complaintType"))){
                complaints = "催促两次";
            }
            if (2880<Integer.parseInt(complaint.get("overTime")+"")){
                complaints += "超时两天";
            }
            row.add(complaints);
            rows.add(row);
        }
        otherData.setRows(rows);
        rows = new ArrayList();
        for(int i=0; i<achList.size();i++){
            Map<String, Object> complaint = orderService.getOrderComplaint(achList.get(i).get("order_no") + "");
            String complaints = "";
            row=new ArrayList();
            row.add(achList.get(i).get("order_no"));
            row.add(achList.get(i).get("caName"));
            row.add(achList.get(i).get("cityName"));
            row.add(achList.get(i).get("areaName"));
            row.add(achList.get(i).get("streetName"));
            row.add(achList.get(i).get("category_name"));
            row.add(achList.get(i).get("status_"));
            row.add(achList.get(i).get("ach_price"));
            row.add(achList.get(i).get("amount"));
            row.add("0".equals(achList.get(i).get("isComplaint")+"")?"不是":"是");
            if("3".equals(complaint.get("complaintType"))){
                complaints = "催促两次";
            }
            if (2880<Integer.parseInt(complaint.get("overTime")+"")){
                complaints += "超时两天";
            }
            row.add(complaints);
            rows.add(row);
        }
        achData.setRows(rows);
        allExcelData.add(achData);
        allExcelData.add(otherData);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, allExcelData);
    }

    @RequestMapping("/getOutComplaintOrderList")
    public void  getOutComplaintOrderList(HttpServletResponse response, OrderBean orderBean) throws Exception{
        List<Map<String, Object>> list = orderService.getOutComplaintOrderList(orderBean);

        ExcelData data = new ExcelData();
        data.setName("订单表");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("订单号");
        titles.add("下单时间");
        titles.add("用户手机号");
        titles.add("回收商名称");
        titles.add("回收员姓名");
        titles.add("完成时间");
        titles.add("订单状态");
        titles.add("客诉数量");
        titles.add("客诉是否解决");
        titles.add("催促状态");
        titles.add("操作时间");
        titles.add("回收类型");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        Map<String,Object> outComplaintOrderMap  = null;
        for(int i=0,j = list.size(); i<j;i++){
            row= new ArrayList();
            outComplaintOrderMap = list.get(i);
            row.add(outComplaintOrderMap.get("orderNo"));
            row.add(outComplaintOrderMap.get("createDate"));
            row.add(outComplaintOrderMap.get("tel"));
            row.add(outComplaintOrderMap.get("companyName"));
            row.add(outComplaintOrderMap.get("recycleerName"));
            row.add(outComplaintOrderMap.get("completeDate"));
            row.add(this.getOrderStatus(outComplaintOrderMap.get("status").toString()));
            row.add(outComplaintOrderMap.get("count"));
            row.add("");
            row.add(outComplaintOrderMap.get("reason"));
            row.add(outComplaintOrderMap.get("updateDate"));
            row.add(outComplaintOrderMap.get("categoryName"));
            rows.add(row);

        }
        data.setRows(rows);
        SimpleDateFormat fdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName=fdate.format(new Date())+".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }
}
