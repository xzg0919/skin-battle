package com.tzj.collect.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.commom.excel.ExcelData;
import com.tzj.collect.api.commom.excel.ExcelUtils;
import com.tzj.collect.common.util.SnUtils;
import com.tzj.collect.core.param.ali.OrderBean;
import com.tzj.collect.core.param.ali.PiccOrderBean;
import com.tzj.collect.core.param.ali.RecruitExpressBean;
import com.tzj.collect.core.param.enterprise.EnterpriseCodeBean;
import com.tzj.collect.core.result.admin.RecruitExpressResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.EnterpriseCode;
import com.tzj.collect.entity.OrderItem;
import com.tzj.collect.entity.OrderItemAch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemAchService orderItemAchService;

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
            titles.add("预约时间");
            titles.add("回收类型");
            titles.add("回收明细");
            titles.add("预估价格");
            titles.add("成交金额");
            titles.add("回收人员姓名");
            titles.add("派单时间");
//        titles.add("确认接单时间");
            titles.add("完成时间");
            titles.add("客户姓名");
            titles.add("客户电话");
            titles.add("回收区域");
            data.setTitles(titles);
            //添加列
            List<List<Object>> rows = new ArrayList();
            List<Object> row = null;
            for (int i = 0; i < list.size(); i++) {
                row = new ArrayList();
                row.add(list.get(i).get("orderNo"));
                row.add(list.get(i).get("arrivalTime"));
                row.add(list.get(i).get("titleName"));
                if ("废弃家电".equals(list.get(i).get("titleName")) || "大件物品".equals(list.get(i).get("titleName"))) {
                    OrderItem orderItem = orderItemService.selectOne(new EntityWrapper<OrderItem>().eq("order_id", list.get(i).get("id")));
                    row.add(orderItem.getCategoryName());
                } else if ("生活垃圾".equals(list.get(i).get("titleName"))) {
                    List<OrderItemAch> orderItemAchesList = orderItemAchService.selectList(new EntityWrapper<OrderItemAch>().eq("order_id", list.get(i).get("id")));
                    if (orderItemAchesList != null) {
                        String createName = "";
                        String s = "";
                        for (OrderItemAch orderItemAch : orderItemAchesList) {
                            if (!s.equals(orderItemAch.getParentName())) {
                                createName += orderItemAch.getParentName() + "/";
                            }
                            s = orderItemAch.getParentName();
                        }
                        row.add(createName);
                    } else {
                        row.add("错误订单");
                    }
                }
                row.add(list.get(i).get("price"));
                row.add(list.get(i).get("paymentPrise"));
                row.add(list.get(i).get("recycleName"));
                row.add(list.get(i).get("distributeTime"));
//            row.add(list.get(i).get("receiveTime"));5.9版本干掉接单时间
                row.add(list.get(i).get("completeDate"));
                row.add(SnUtils.leftOne(list.get(i).get("linkMan") + "", 1));
                row.add(SnUtils.telEncry(list.get(i).get("tel") + ""));
                row.add(list.get(i).get("areaName"));
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
            titles.add("下单日期");
            titles.add("服务商");
            titles.add("类别");
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

}
