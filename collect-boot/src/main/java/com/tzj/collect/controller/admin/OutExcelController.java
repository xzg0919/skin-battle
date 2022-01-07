package com.tzj.collect.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.common.excel.ExcelData;
import com.tzj.collect.common.excel.ExcelUtils;
import com.tzj.collect.core.param.admin.AdminShareCodeBean;
import com.tzj.collect.core.param.ali.*;
import com.tzj.collect.core.param.business.BOrderBean;
import com.tzj.collect.core.param.enterprise.EnterpriseCodeBean;
import com.tzj.collect.core.param.iot.AdminIotErrorBean;
import com.tzj.collect.core.result.admin.RecruitExpressResult;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Company;
import com.tzj.collect.entity.EnterpriseCode;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.OrderComplaint;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private CompanyService companyService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AreaService areaService;
    @Resource
    private JedisPool jedisPool;
    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    private CompanyEquipmentService companyEquipmentService;
    @Autowired
    private OrderComplaintService orderComplaintService;
    @Resource
    private LineQrCodeService lineQrCodeService;

    /**
     * 根据企业导出以旧换新的券的excel列表
     *
     * @param response
     * @param enterpriseCodeBean
     * @throws Exception
     */
    @RequestMapping("/outEnterpriseCodeExcel")
    public void outEnterpriseCodeExcel(HttpServletResponse response, EnterpriseCodeBean enterpriseCodeBean) throws Exception {
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
        for (int i = 0; i < list.size(); i++) {
            row = new ArrayList();
            row.add(list.get(i).get("createDate"));
            row.add(list.get(i).get("customerName"));
            row.add(list.get(i).get("customerTel"));
            row.add(list.get(i).get("productName"));
            row.add(list.get(i).get("terminalName"));
            row.add(new BigDecimal(list.get(i).get("price") + "").setScale(2, BigDecimal.ROUND_DOWN));
            rows.add(row);

        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    /**
     * 根据终端导出以旧换新的券的excel列表
     *
     * @param response
     * @param enterpriseCodeBean
     * @throws Exception
     */
    @RequestMapping("/outEnterpriseTerminalCodeExcel")
    public void outEnterpriseTerminalCodeExcel(HttpServletResponse response, EnterpriseCodeBean enterpriseCodeBean) throws Exception {
        EntityWrapper wrapper = new EntityWrapper<EnterpriseCode>();
        wrapper.eq("terminal_id", enterpriseCodeBean.getId());
        wrapper.eq("del_flag", 0);
        if (!StringUtils.isBlank(enterpriseCodeBean.getStartTime()) && !StringUtils.isBlank(enterpriseCodeBean.getEndTime())) {
            wrapper.le("create_date", enterpriseCodeBean.getEndTime() + " 23:59:59");
            wrapper.ge("create_date", enterpriseCodeBean.getStartTime() + " 00:00:01");
        }
        if (!StringUtils.isBlank(enterpriseCodeBean.getIsUse())) {
            if ("0".equals(enterpriseCodeBean.getIsUse())) {
                wrapper.in("is_use", "0,1");
            } else {
                wrapper.eq("is_use", enterpriseCodeBean.getIsUse());
            }
        }
        wrapper.orderBy("create_date", true);
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
        for (int i = 0; i < list.size(); i++) {
            row = new ArrayList();
            row.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreateDate()));
            row.add(list.get(i).getCustomerName());
            row.add(list.get(i).getCustomerTel());
            row.add(list.get(i).getProductName());
            row.add(list.get(i).getPrice().setScale(2, BigDecimal.ROUND_DOWN));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    /**
     * picc导出用户申请保单的excel列表
     *
     * @param response
     * @param piccOrderBean
     * @throws Exception
     */
    @RequestMapping("/outPiccOrderExcel")
    public void outPiccOrderExcel(HttpServletResponse response, PiccOrderBean piccOrderBean) throws Exception {

        piccOrderService.outPiccOrderExcel(response, piccOrderBean);
    }

    /**
     * 完成订单列表
     *
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/outOrderExcel")
    public void outOrderExcel(HttpServletResponse response, OrderBean orderBean) throws Exception {
        //五废订单需要单独格式导出
        if ("1".equals(orderBean.getType()) || "4".equals(orderBean.getType())) {
            List<Map<String, Object>> list = orderService.outOrderExcel(orderBean.getId(), orderBean.getType(), orderBean.getStartTime(), orderBean.getEndTime(), orderBean.getRecyclerName());
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
            titles.add("平台佣金");
            titles.add("服务商返佣");
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
                row.add(list.get(i).get("commissionsPrice"));
                row.add(list.get(i).get("backCommissionsPrice"));
                rows.add(row);
            }
            data.setRows(rows);
            SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String fileName = fdate.format(new Date()) + ".xlsx";
            ExcelUtils.exportExcel(response, fileName, data);
        } else {
            List<Map<String, Object>> list = orderService.orderDetail4HorseHold(orderBean.getId(), orderBean.getStartTime(), orderBean.getEndTime(), orderBean.getRecyclerName(), orderBean.getType());
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
            titles.add("平台佣金");
            titles.add("服务商返佣");
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
                row.add(list.get(i).get("commissionsPrice"));
                row.add(list.get(i).get("backCommissionsPrice"));
                rows.add(row);
            }
            data.setRows(rows);
            SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String fileName = fdate.format(new Date()) + ".xlsx";
            ExcelUtils.exportExcel(response, fileName, data);
        }
    }

    /**
     * 完成订单列表
     *
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/outNewOrderExcel")
    public void outNewOrderExcel(HttpServletResponse response, OrderBean orderBean) throws Exception {
        List<Order> list1 = orderService.getOrderListss(orderBean.getCompanyId(), orderBean.getOrderNo(), orderBean.getLinkMan(), orderBean.getRecyclerName(), orderBean.getStartTime(), orderBean.getEndTime(), orderBean.getTitle());
        List<String> titles = new ArrayList<>();
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
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
        titles.add("平台佣金");
        titles.add("服务商返佣");
        data.setTitles(titles);
        if (list1.size() > 0) {
            for (Order order : list1) {
                String title = order.getTitle().getValue().toString();
                Long id = order.getId();
                if ("1".equals(title) || "4".equals(title)) {
                    row = new ArrayList();
                    Map<String, Object> map = orderService.select1Or4Map(id);
                    row.add(map.get("orderNo"));
                    row.add(map.get("createDate"));
                    row.add(map.get("name"));
                    row.add(map.get("parentName"));
                    row.add(map.get("categoryName"));
                    row.add(map.get("isCash"));
                    row.add(map.get("amount"));
                    row.add("0.00");
                    row.add(map.get("recyclerName"));
                    row.add(map.get("commissionsPrice"));
                    row.add(map.get("backCommissionsPrice"));
                    rows.add(row);
                    /*data.setRows(rows);*/
                } else {
                    List<Map<String, Object>> list = orderService.select2Or3Map(id);
                    for (int i = 0; i < list.size(); i++) {
                        row = new ArrayList();
                        row.add(list.get(i).get("orderNo"));
                        row.add(list.get(i).get("time"));
                        row.add(list.get(i).get("companyName"));
                        row.add(list.get(i).get("parentName"));
                        row.add(list.get(i).get("categoryName"));
                        row.add(list.get(i).get("cash"));
                        BigDecimal amount = new BigDecimal(String.valueOf(list.get(i).get("amount")));
                        BigDecimal price1 = new BigDecimal(String.valueOf(list.get(i).get("price")));
                        BigDecimal price2 = new BigDecimal(String.valueOf(list.get(i).get("commission")));
                        BigDecimal price3 = new BigDecimal(String.valueOf(list.get(i).get("backCommission")));
                        BigDecimal price11 = amount.multiply(price1).setScale(2, RoundingMode.DOWN);
                        BigDecimal price22 = amount.multiply(price2).setScale(2, RoundingMode.DOWN);
                        BigDecimal price33 = amount.multiply(price3).setScale(2, RoundingMode.DOWN);
                        row.add(String.valueOf(price11));
                        row.add(list.get(i).get("amount"));
                        row.add(list.get(i).get("recyclerName"));
                        row.add(String.valueOf(price22));
                        row.add(String.valueOf(price33));
                        rows.add(row);
                        /*data.setRows(rows);*/
                    }
                }
            }
            data.setRows(rows);
        } else {
            row = new ArrayList();
            rows.add(row);
            data.setRows(rows);
        }
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

//    public static void main(String[] args) {
//        BigDecimal amount = new BigDecimal(Double.valueOf(String.valueOf("10")));
//        BigDecimal price = new BigDecimal(String.valueOf("0.3"));
//        System.out.println(""+amount+" "+price+" "+ amount.multiply(price).setScale(2, RoundingMode.DOWN));
//    }
    @RequestMapping("/getRecruitListOutExcel")
    public void getRecruitListOutExcel(HttpServletResponse response, RecruitExpressBean recruitExpressBean) throws Exception {
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
        for (int i = 0; i < recruitList.size(); i++) {
            row = new ArrayList();
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
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    @RequestMapping("/getRecyclerOrderList")
    public void getRecyclerOrderList(HttpServletResponse response, OrderBean orderBean) throws Exception {

        List<Map<String, Object>> list = orderService.getRecyclerOrderList(orderBean);
        ExcelData data = new ExcelData();
        data.setName("正常订单数据");
        if ("1".equals(orderBean.getIsOverTime())) {
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
        for (int i = 0; i < list.size(); i++) {
            row = new ArrayList();
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
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    @RequestMapping("/getOdrerCancleExamineList")
    public void getOdrerCancleExamineList(HttpServletResponse response, OrderBean orderBean) throws Exception {
        Map<String, Object> resultMap = (Map<String, Object>) orderService.getOrderCancleExamineList(orderBean);
        List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get("overTimeOrderList");
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
        for (int i = 0; i < list.size(); i++) {
            row = new ArrayList();
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
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    @RequestMapping("/getCompanyServiceOutList")
    public void getCompanyServiceOutList(HttpServletResponse response, AreaBean areaBean) throws Exception {
        List<Map<String, Object>> list = areaService.getCompanyServiceOutList(areaBean);
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
        for (int i = 0; i < list.size(); i++) {
            row = new ArrayList();
            row.add(list.get(i).get("companyName"));
            row.add(list.get(i).get("title"));
            row.add(list.get(i).get("province"));
            row.add(list.get(i).get("cityName"));
            row.add(list.get(i).get("areaName"));
            row.add(list.get(i).get("streetName"));
            rows.add(row);

        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    public String getOrderStatus(String status) {
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

    /**
     * 【业务数据总览】 已完成和其他状态订单分sheet导出
     *
     * @param
     * @return
     * @author sgmark@aliyun.com
     * @date 2019/9/10 0010
     */
    @RequestMapping("/out/order/for/overview")
    public void outAllOrderMapOverview(HttpServletResponse response, BOrderBean bOrderBean) throws Exception {
        if (StringUtils.isEmpty(bOrderBean.getStartTime()) || StringUtils.isEmpty(bOrderBean.getEndTime())) {
            throw new ApiException("缺少开始或结束时间");
        } else if (null == bOrderBean.getCompanyId()) {
            throw new ApiException("缺少公司id");
        }
        String type = null;
        if (StringUtils.isEmpty(bOrderBean.getCategoryType())) {
            type = "0";
        } else {
            type = bOrderBean.getCategoryType();
        }
        //限制各个类型每天导出一次
        String redisKeyName = LocalDate.now().getYear() + ":" + LocalDate.now().getDayOfYear() + ":" + bOrderBean.getCompanyId() + ":" + type;
        RedisUtil.SaveOrGetFromRedis saveOrGetFromRedis = new RedisUtil.SaveOrGetFromRedis();
        if (null == saveOrGetFromRedis.getFromRedis(redisKeyName, jedisPool)) {
            try {
                saveOrGetFromRedis.saveInRedis(redisKeyName, System.currentTimeMillis(), 24 * 3600, jedisPool);
            } catch (Exception e) {
                throw new ApiException("今日已经导出过，不能再执行此操作");
            }
        } else {
            throw new ApiException("今日已经导出过，不能再执行此操作");
        }
        List<Map<String, Object>> achList = orderService.outOtherOrderListOverview(bOrderBean);
        List<Map<String, Object>> otherList = orderService.outAchOrderListOverview(bOrderBean);
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
        titles.add("回收人员");
        otherData.setTitles(titles);
        achData.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for (int i = 0; i < otherList.size(); i++) {
            Map<String, Object> complaint = orderService.getOrderComplaint(otherList.get(i).get("order_no") + "");
            String complaints = "";
            row = new ArrayList();
            row.add(otherList.get(i).get("order_no"));
            row.add(otherList.get(i).get("caName"));
            row.add(otherList.get(i).get("cityName"));
            row.add(otherList.get(i).get("areaName"));
            row.add(otherList.get(i).get("streetName"));
            row.add(otherList.get(i).get("category_name"));
            row.add(otherList.get(i).get("status_"));
            row.add(otherList.get(i).get("ach_price"));
            row.add(otherList.get(i).get("amount"));
            row.add("0".equals(otherList.get(i).get("isComplaint") + "") ? "不是" : "是");
            if ("3".equals(complaint.get("complaintType"))) {
                complaints = "催促两次";
            }
            if (null != complaint.get("overTime") && 2880 < Integer.parseInt(complaint.get("overTime") + "")) {
                complaints += "超时两天";
            }
            row.add(complaints);
            row.add(otherList.get(i).get("recyclerName"));
            rows.add(row);
        }
        otherData.setRows(rows);
        rows = new ArrayList();
        for (int i = 0; i < achList.size(); i++) {
            Map<String, Object> complaint = orderService.getOrderComplaint(achList.get(i).get("order_no") + "");
            String complaints = "";
            row = new ArrayList();
            row.add(achList.get(i).get("order_no"));
            row.add(achList.get(i).get("caName"));
            row.add(achList.get(i).get("cityName"));
            row.add(achList.get(i).get("areaName"));
            row.add(achList.get(i).get("streetName"));
            row.add(achList.get(i).get("category_name"));
            row.add(achList.get(i).get("status_"));
            row.add(achList.get(i).get("ach_price"));
            row.add(achList.get(i).get("amount"));
            row.add("0".equals(achList.get(i).get("isComplaint") + "") ? "不是" : "是");
            if ("3".equals(complaint.get("complaintType"))) {
                complaints = "催促两次";
            }
            if (null != complaint.get("overTime") && 2880 < Integer.parseInt(complaint.get("overTime") + "")) {
                complaints += "超时两天";
            }
            row.add(complaints);
            row.add(achList.get(i).get("recyclerName"));
            rows.add(row);
        }
        achData.setRows(rows);
        allExcelData.add(achData);
        allExcelData.add(otherData);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, allExcelData);
    }

    @RequestMapping("/getOutComplaintOrderList")
    public void getOutComplaintOrderList(HttpServletResponse response, OrderBean orderBean) throws Exception {
        List<Map<String, Object>> list = orderService.getOutComplaintOrderList(orderBean);

        ExcelData data = new ExcelData();
        data.setName("订单表");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("订单号");
        titles.add("省");
        titles.add("市");
        titles.add("区");
        titles.add("街道");
        titles.add("下单时间");
        titles.add("预约上门时间");
        titles.add("用户手机号");
        titles.add("回收商名称");
        titles.add("回收员姓名");
        titles.add("完成时间");
        titles.add("订单状态");
        titles.add("催派");
        titles.add("催接");
        titles.add("催收");
        titles.add("系统自定义客诉超时2天");
        titles.add("投诉（点选的客诉）");
        titles.add("客诉数量");
        titles.add("回收类型");
        titles.add("平台佣金（定价回收）");
        titles.add("平台佣金（环保回收）");
        titles.add("服务商返佣");
        titles.add("投诉理由");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        Map<String, Object> outComplaintOrderMap = null;
        for (int i = 0, j = list.size(); i < j; i++) {
            row = new ArrayList();
            outComplaintOrderMap = list.get(i);
            row.add(outComplaintOrderMap.get("orderNo"));
            row.add(outComplaintOrderMap.get("provinceName"));
            row.add(outComplaintOrderMap.get("cityName"));
            row.add(outComplaintOrderMap.get("areaName"));
            row.add(outComplaintOrderMap.get("streetName"));
            row.add(outComplaintOrderMap.get("createDate"));
            row.add(outComplaintOrderMap.get("arrivalTime") + " " + outComplaintOrderMap.get("arrivalPeriod"));
            row.add(outComplaintOrderMap.get("tel"));
            row.add(outComplaintOrderMap.get("companyName"));
            row.add(outComplaintOrderMap.get("recycleerName"));
            row.add(outComplaintOrderMap.get("completeDate"));
            row.add(this.getOrderStatus(outComplaintOrderMap.get("status").toString()));
            row.add(outComplaintOrderMap.get("initCount"));
            row.add(outComplaintOrderMap.get("sendCount"));
            row.add(outComplaintOrderMap.get("completeCount"));
            row.add(outComplaintOrderMap.get("isComplaint"));
            row.add(outComplaintOrderMap.get("complaintCount"));
            row.add(outComplaintOrderMap.get("count"));
            row.add(outComplaintOrderMap.get("categoryName"));
            row.add(outComplaintOrderMap.get("commissionsPrice"));
            row.add(outComplaintOrderMap.get("freeCommissionsPrice"));
            row.add(outComplaintOrderMap.get("backCommissionsPrice"));
            List<OrderComplaint> orderComplaints = orderComplaintService.selectList(new EntityWrapper<OrderComplaint>().eq("order_no", outComplaintOrderMap.get("orderNo")).eq("type_", "4"));
            List<Object> finalRow = row;
            orderComplaints.stream().forEach(orderComplaint -> {
                finalRow.add(orderComplaint.getReason());
            });
            rows.add(row);

        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    /**
     * 各品类订单完成情况导出
     *
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/gary/outTitleOrderCount")
    public void outTitleOrderCount(HttpServletResponse response, OrderBean orderBean) throws Exception {
        //服务商列表
        List<Company> companyNameList = companyService.getCompanyNameList();
        //订单总数
        List<Long> orderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), null, null);
        //完成数量
        List<Long> orderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), null, "3");
        //家电覆盖街道到数
        List<Long> applianceStreetNum = companyService.getStreetNumByTableName("sb_company_street_appliance");
        //家电订单总数
        List<Long> applianceOrderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "1", null);
        //家电进行中订单总数
        List<Long> applianceOrderIngList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "1", "0,1,2");
        //家电用户取消订单总数
        List<Long> applianceOrderCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "1", "4");
        //家电平台取消订单总数
        List<Long> applianceOrderRejectList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "1", "5");
        //家电完成订单总数
        List<Long> applianceOrderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "1", "3");
        //生活垃圾覆盖街道到数
        List<Long> houseStreetNum = companyService.getStreetNumByTableName("sb_company_street_house");
        //生活垃圾订单总数
        List<Long> houseOrderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "2", null);
        //生活垃圾进行中订单总数
        List<Long> houseOrderIngList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "2", "0,1,2");
        //生活垃圾用户取消订单总数
        List<Long> houseOrderCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "2", "4");
        //生活垃圾平台取消订单总数
        List<Long> houseOrderRejectList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "2", "5");
        //生活垃圾完成订单总数
        List<Long> houseOrderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "2", "3");
        //五公斤覆盖街道到数
        List<Long> fiveStreetNum = companyService.getStreetNumByTableName("sb_company_stree");
        //五公斤订单总数
        List<Long> fiveOrderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "3", null);
        //五公斤进行中订单总数
        List<Long> fiveOrderIngList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "3", "0,1,2");
        //五公斤用户取消订单总数
        List<Long> fiveOrderCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "3", "4");
        //五公斤平台取消订单总数
        List<Long> fiveOrderRejectList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "3", "5");
        //五公斤完成订单总数
        List<Long> fiveOrderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "3", "3");
        //大件覆盖街道到数
        List<Long> bigStreetNum = companyService.getStreetNumByTableName("sb_company_street_big");
        //大件订单总数
        List<Long> bigOrderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "4", null);
        //大件进行中订单总数
        List<Long> bigOrderIngList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "4", "0,1,2");
        //大件用户取消订单总数
        List<Long> bigOrderCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "4", "4");
        //大件平台取消订单总数
        List<Long> bigOrderRejectList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "4", "5");
        //大件完成订单总数
        List<Long> bigOrderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "4", "3");
        //小家电覆盖街道到数
        List<Long> smallAppStreetNum = companyService.getStreetNumByTableName("sb_company_street_app_small");
        //小家电订单总数
        List<Long> smallAppOrderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "8", null);
        //小家电进行中订单总数
        List<Long> smallAppOrderIngList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "8", "0,1,2");
        //小家电用户取消订单总数
        List<Long> smallAppOrderCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "8", "4");
        //小家电平台取消订单总数
        List<Long> smallAppOrderRejectList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "8", "5");
        //小家电完成订单总数
        List<Long> smallAppOrderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "8", "3");
        //IOT订单总数
        List<Long> IOTOrderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "5", null);
        //IOT完成订单总数
        List<Long> IOTOrderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "5", "3");

        //电瓶车覆盖街道到数
        List<Long> electroStreetNum = companyService.getStreetNumByTableName("sb_company_street_electro_mobile");
        //电瓶车订单总数
        List<Long> electroOrderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "9", null);
        //电瓶车进行中订单总数
        List<Long> electroOrderIngList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "9", "0,1,2");
        //电瓶车用户取消订单总数
        List<Long> electroOrderCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "9", "4");
        //电瓶车平台取消订单总数
        List<Long> electroOrderRejectList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "9", "5");
        //电瓶车完成订单总数
        List<Long> electroOrderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), "9", "3");


        ExcelData data = new ExcelData();
        data.setName("各个品类订单完成情况");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("服务商");
        titles.add("订单总数");
        titles.add("完成数量");
        titles.add("家电覆盖街道数量");
        titles.add("家电订单总数");
        titles.add("家电进行中的数量");
        titles.add("家电用户取消");
        titles.add("家电平台取消");
        titles.add("家电完成数量");
        titles.add("生活垃圾覆盖街道数量");
        titles.add("生活垃圾订单总数");
        titles.add("生活垃圾进行中的数量");
        titles.add("生活垃圾用户取消");
        titles.add("生活垃圾平台取消");
        titles.add("生活垃圾完成数量");
        titles.add("五公斤覆盖街道数量");
        titles.add("五公斤订单总数");
        titles.add("五公斤进行中的数量");
        titles.add("五公斤用户取消");
        titles.add("五公斤平台取消");
        titles.add("五公斤完成数量");
        titles.add("大件覆盖街道数量");
        titles.add("大件订单总数");
        titles.add("大件进行中的数量");
        titles.add("大件用户取消");
        titles.add("大件平台取消");
        titles.add("大件完成数量");
        titles.add("小家电覆盖街道数量");
        titles.add("小家电订单总数");
        titles.add("小家电进行中的数量");
        titles.add("小家电用户取消");
        titles.add("小家电平台取消");
        titles.add("小家电完成数量");
        titles.add("IOT订单总数");
        titles.add("IOT完成数量");
        titles.add("电瓶车覆盖街道数量");
        titles.add("电瓶车订单总数");
        titles.add("电瓶车进行中的数量");
        titles.add("电瓶车用户取消");
        titles.add("电瓶车平台取消");
        titles.add("电瓶车完成数量");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        Map<String, Object> outComplaintOrderMap = null;
        for (int i = 0, j = companyNameList.size(); i < j; i++) {
            row = new ArrayList();
            row.add(companyNameList.get(i).getName());
            row.add(orderList.get(i));
            row.add(orderCompleteList.get(i));
            row.add(applianceStreetNum.get(i));
            row.add(applianceOrderList.get(i));
            row.add(applianceOrderIngList.get(i));
            row.add(applianceOrderCancelList.get(i));
            row.add(applianceOrderRejectList.get(i));
            row.add(applianceOrderCompleteList.get(i));
            row.add(houseStreetNum.get(i));
            row.add(houseOrderList.get(i));
            row.add(houseOrderIngList.get(i));
            row.add(houseOrderCancelList.get(i));
            row.add(houseOrderRejectList.get(i));
            row.add(houseOrderCompleteList.get(i));
            row.add(fiveStreetNum.get(i));
            row.add(fiveOrderList.get(i));
            row.add(fiveOrderIngList.get(i));
            row.add(fiveOrderCancelList.get(i));
            row.add(fiveOrderRejectList.get(i));
            row.add(fiveOrderCompleteList.get(i));
            row.add(bigStreetNum.get(i));
            row.add(bigOrderList.get(i));
            row.add(bigOrderIngList.get(i));
            row.add(bigOrderCancelList.get(i));
            row.add(bigOrderRejectList.get(i));
            row.add(bigOrderCompleteList.get(i));
            row.add(smallAppStreetNum.get(i));
            row.add(smallAppOrderList.get(i));
            row.add(smallAppOrderIngList.get(i));
            row.add(smallAppOrderCancelList.get(i));
            row.add(smallAppOrderRejectList.get(i));
            row.add(smallAppOrderCompleteList.get(i));
            row.add(IOTOrderList.get(i));
            row.add(IOTOrderCompleteList.get(i));
            row.add(electroStreetNum.get(i));
            row.add(electroOrderList.get(i));
            row.add(electroOrderIngList.get(i));
            row.add(electroOrderCancelList.get(i));
            row.add(electroOrderRejectList.get(i));
            row.add(electroOrderCompleteList.get(i));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    /**
     * 各品类订单完成情况导出
     *
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/gary/outOrderCountByCity")
    public void outOrderCountByCity(HttpServletResponse response, OrderBean orderBean) throws Exception {
        //查询所有城市
        List<Map<String, Object>> cityList = areaService.getCityListByGary();
        //查询家电订单数量
        List<Long> applianceOrderList = orderService.getOrderListByCity(orderBean.getStartTime(), orderBean.getEndTime(), "1");
        //查询生活垃圾订单数量
        List<Long> houseOrderList = orderService.getOrderListByCity(orderBean.getStartTime(), orderBean.getEndTime(), "2");
        //查询大件订单数量
        List<Long> bigOrderList = orderService.getOrderListByCity(orderBean.getStartTime(), orderBean.getEndTime(), "4");
        //查询大件订单数量
        List<Long> fiveOrderList = orderService.getOrderListByCity(orderBean.getStartTime(), orderBean.getEndTime(), "3");
        //查询电瓶车订单数量
        List<Long> electroOrderList = orderService.getOrderListByCity(orderBean.getStartTime(), orderBean.getEndTime(), "9");
        ExcelData data = new ExcelData();
        data.setName("各个品类订单完成情况");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("省");
        titles.add("城市");
        titles.add("家电订单数量");
        titles.add("生活垃圾订单数量");
        titles.add("大件订单数量");
        titles.add("五公斤订单数量");
        titles.add("电瓶车订单数量");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        Map<String, Object> outComplaintOrderMap = null;
        for (int i = 0, j = cityList.size(); i < j; i++) {
            row = new ArrayList();
            row.add(cityList.get(i).get("province"));
            row.add(cityList.get(i).get("city"));
            row.add(applianceOrderList.get(i));
            row.add(houseOrderList.get(i));
            row.add(bigOrderList.get(i));
            row.add(fiveOrderList.get(i));
            row.add(electroOrderList.get(i));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    /**
     * 订单取消原因汇总导出
     *
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/gary/outOrderCancelByCompany")
    public void outOrderCancelByCompany(HttpServletResponse response, OrderBean orderBean) throws Exception {
        List<Map<String, Object>> cancelOrderList = orderService.getOrderCancelByCompany(orderBean.getStartTime(), orderBean.getEndTime());
        ExcelData data = new ExcelData();
        data.setName("订单取消原因汇总导出");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("服务商");
        titles.add("订单号");
        titles.add("回收类型");
        titles.add("下单时间");
        titles.add("取消原因");
        titles.add("取消类型");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        Map<String, Object> outComplaintOrderMap = null;
        for (int i = 0, j = cancelOrderList.size(); i < j; i++) {
            row = new ArrayList();
            Map<String, Object> objectMap = cancelOrderList.get(i);
            row.add(objectMap.get("name"));
            row.add(objectMap.get("orderNo"));
            row.add(objectMap.get("title"));
            row.add(objectMap.get("createDate"));
            row.add(objectMap.get("cancelReason"));
            row.add(objectMap.get("status"));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);

    }

    /**
     * 回收人员订单完成情况
     *
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/gary/outOrderListByRecycler")
    public void outOrderListByRecycler(HttpServletResponse response, OrderBean orderBean) throws Exception {
        //完成总订单
        List<Long> completeOrderList = orderService.outOrderListByRecycler(orderBean.getStartTime(), orderBean.getEndTime(), null);
        //超时订单
        List<Long> overTimeOrderList = orderService.outOrderListByRecycler(orderBean.getStartTime(), orderBean.getEndTime(), "1");
        //不超时完成订单
        List<Long> notOverTimeOrderList = orderService.outOrderListByRecycler(orderBean.getStartTime(), orderBean.getEndTime(), "0");

        List<Map<String, Object>> recyclerCityList = recyclersService.getRecyclerCityList();

        ExcelData data = new ExcelData();
        data.setName("回收人员完成订单情况");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("回收人员姓名");
        titles.add("所属服务商");
        titles.add("管辖省");
        titles.add("管辖市");
        titles.add("完成订单数量");
        titles.add("预约时间内完成");
        titles.add("超时完成");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        Map<String, Object> outComplaintOrderMap = null;
        for (int i = 0, j = recyclerCityList.size(); i < j; i++) {
            row = new ArrayList();
            Map<String, Object> objectMap = recyclerCityList.get(i);
            row.add(objectMap.get("name"));
            row.add(objectMap.get("companyName"));
            row.add(objectMap.get("province"));
            row.add(objectMap.get("city"));
            row.add(completeOrderList.get(i));
            row.add(notOverTimeOrderList.get(i));
            row.add(overTimeOrderList.get(i));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);

    }

    /**
     * 某个时间段订单完成情况
     *
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/gary/outOrderListByGary")
    public void outOrderListByGary(HttpServletResponse response, OrderBean orderBean) throws Exception {
        //服务商列表
        List<Company> companyNameList = companyService.getCompanyNameList();
        //订单总数
        List<Long> orderList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), null, null);
        //完成数量
        List<Long> orderCompleteList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), null, "3");
        //总取消数量
        List<Long> orderCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), null, "4,5");
        //用户取消数量
        List<Long> orderUserCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), null, "4");
        //平台数量
        List<Long> orderCompanyCancelList = orderService.getOrderListByTitleStatus(orderBean.getStartTime(), orderBean.getEndTime(), null, "5");
        //回收人员数量
        List<Long> recyclersList = recyclersService.getRecyclerListGroupCompany();
        //获取街道覆盖数量
        List<Long> streetCountList = recyclersService.getStreetListGroupCompany();
        //超时订单数
        List<Long> orderOverTimeList = orderService.outOrderListGroupCompany(orderBean.getStartTime(), orderBean.getEndTime());

        ExcelData data = new ExcelData();
        data.setName("回收人员完成订单情况");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("服务商");
        titles.add("回收人员人数");
        titles.add("街道覆盖");
        titles.add("用户总下订单");
        titles.add("总完成");
        titles.add("总取消");
        titles.add("用户取消");
        titles.add("服务商取消");
        titles.add("超时完成订单数");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for (int i = 0, j = companyNameList.size(); i < j; i++) {
            row = new ArrayList();
            row.add(companyNameList.get(i).getName());
            row.add(recyclersList.get(i));
            row.add(streetCountList.get(i));
            row.add(orderList.get(i));
            row.add(orderCompleteList.get(i));
            row.add(orderCancelList.get(i));
            row.add(orderUserCancelList.get(i));
            row.add(orderCompanyCancelList.get(i));
            row.add(orderOverTimeList.get(i));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    /**
     * 某个时间段多次下单统计
     *
     * @param response
     * @param orderBean
     * @throws Exception
     */
    @RequestMapping("/gary/outManyOrderListByGary")
    public void outManyOrderListByGary(HttpServletResponse response, OrderBean orderBean) throws Exception {
        Integer applianceCompleteNum = orderService.getOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "1", "3");
        Integer houseCompleteNum = orderService.getOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "2", "3");
        Integer fiveCompleteNum = orderService.getOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "3", "3");
        Integer bigCompleteNum = orderService.getOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "4", "3");
        Integer electroCompleteNum = orderService.getOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "9", "3");

        Integer applianceManyNum = orderService.getManyOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "1", null);
        Integer houseManyNum = orderService.getManyOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "2", null);
        Integer fiveManyNum = orderService.getManyOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "3", null);
        Integer bigManyNum = orderService.getManyOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "4", null);
        Integer electroManyNum = orderService.getManyOrderListByDate(orderBean.getStartTime(), orderBean.getEndTime(), "9", null);

        ExcelData data = new ExcelData();
        data.setName("某个时间段多次下单统计");
        //添加表头
        List<String> titles = new ArrayList<>();
        //for(String title: excelInfo.getNames())
        titles.add("回收品类");
        titles.add("大家电");
        titles.add("生活五废");
        titles.add("五公斤");
        titles.add("大家具");
        titles.add("电瓶车");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = new ArrayList();
        row.add("选择时间段完成订单数量");
        row.add(applianceCompleteNum);
        row.add(houseCompleteNum);
        row.add(fiveCompleteNum);
        row.add(bigCompleteNum);
        row.add(electroCompleteNum);
        rows.add(row);
        List<Object> row1 = new ArrayList();
        row1.add("选择时间段完多次下单数量");
        row1.add(applianceManyNum);
        row1.add(houseManyNum);
        row1.add(fiveManyNum);
        row1.add(bigManyNum);
        row1.add(electroManyNum);
        rows.add(row1);
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }

    /**
     * 服务区域总表---未覆盖区域
     *
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/9 0009
     * @Param:
     * @return:
     */
    @RequestMapping("/admin/other/areas")
    public void adminOtherAreas(HttpServletResponse response, OrderBean orderBean) {
        String title = orderBean.getTitle();
        String areaId = orderBean.getAreaId() + "";
        String tableName = null;
        String type = null;
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(areaId)) {
            throw new ApiException("参数错误");
        } else {
            if (Order.TitleType.HOUSEHOLD.getValue().toString().equals(title)) {
                type = "生活垃圾";
                tableName = "sb_company_street_house";
            } else if (Order.TitleType.BIGTHING.getValue().toString().equals(title)) {
                type = "大件垃圾";
                tableName = "sb_company_street_big";
            } else if (Order.TitleType.DIGITAL.getValue().toString().equals(title)) {
                type = "废弃家电";
                tableName = "sb_company_street_appliance";
            } else if (Order.TitleType.FIVEKG.getValue().toString().equals(title)) {
                type = "五公斤";
                /**
                 * 匹配数据库
                 * @author: sgmark@aliyun.com
                 * @Date: 2019/12/12 0012
                 */
                tableName = "sb_company_stree";
            }else if (Order.TitleType.ELECTROMOBILE.getValue().toString().equals(title)) {
                type = "电瓶车";
                tableName = "sb_company_street_electro_mobile";
            } else {
                throw new ApiException("参数错误");
            }
        }
        ExcelData data = new ExcelData();
        data.setName("未覆盖区域");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("回收类型");
        titles.add("城市");
        titles.add("行政区");
        titles.add("街道");
        data.setTitles(titles);
        List<Map<String, Object>> otherAreaLists = companyService.otherAreaLists(tableName, areaId);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for (int i = 0, j = otherAreaLists.size(); i < j; i++) {
            row = new ArrayList();
            row.add(type);
            row.add(otherAreaLists.get(i).get("cityName"));
            row.add(otherAreaLists.get(i).get("areaName"));
            row.add(otherAreaLists.get(i).get("streetName"));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        try {
            ExcelUtils.exportExcel(response, fileName, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * iot导出订单信息（gary后台）
     *
     * @author: sgmark@aliyun.com
     * @Date: 2019/12/13 0013
     * @Param:
     * @return:
     */
    @RequestMapping("/admin/iot/order")
    public void adminIotOrder(HttpServletResponse response, AdminIotErrorBean adminIotBean) {
        ExcelData data = new ExcelData();
        data.setName("iot订单信息");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("设备编号");
        titles.add("下单时间");
        titles.add("订单编号");
        titles.add("用户id");
        titles.add("用户手机号");
        data.setTitles(titles);
        if (StringUtils.isEmpty(adminIotBean.getStartTime()) || StringUtils.isEmpty(adminIotBean.getEndTime())) {
            throw new ApiException("请选择时间范围");
        }
        List<Map<String, Object>> adminIotOrderList = companyEquipmentService.adminIotOrderList(adminIotBean);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for (int i = 0, j = adminIotOrderList.size(); i < j; i++) {
            row = new ArrayList();
            row.add(adminIotOrderList.get(i).get("iot_equipment_code"));
            row.add(adminIotOrderList.get(i).get("create_date"));
            row.add(adminIotOrderList.get(i).get("order_no"));
            row.add(adminIotOrderList.get(i).get("ali_user_id"));
            row.add(adminIotOrderList.get(i).get("tel"));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        try {
            ExcelUtils.exportExcel(response, fileName, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/admin/share_code/report")
    public void adminShareCodeReport(HttpServletResponse response, AdminShareCodeBean adminShareCodeBean) {
        ExcelData data = new ExcelData();
        data.setName("分享码报表");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("码名称");
        titles.add("活动详情");
        titles.add("配置时间");
        titles.add("跳转数量");
        titles.add("用户下单数量");
        titles.add("转化率");
        data.setTitles(titles);
        if (StringUtils.isEmpty(adminShareCodeBean.getStartTime()) || StringUtils.isEmpty(adminShareCodeBean.getEndTime())) {
            throw new ApiException("请选择时间范围");
        }
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(500000);
        pageBean.setPageNumber(1);
        adminShareCodeBean.setPageBean(pageBean);
        Map<String, Object> adminIotOrderListMap = lineQrCodeService.lineQrCodeReport(adminShareCodeBean);
        List<Map<String, Object>> adminIotOrderList = (ArrayList) adminIotOrderListMap.get("lineQrCodeReportList");
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for (int i = 0, j = adminIotOrderList.size(); i < j; i++) {
            row = new ArrayList();
            row.add(adminIotOrderList.get(i).get("name_"));
            row.add(adminIotOrderList.get(i).get("qr_code_info"));
            row.add(adminIotOrderList.get(i).get("create_date"));
            row.add(adminIotOrderList.get(i).get("share_num"));
            row.add(adminIotOrderList.get(i).get("count_"));
            row.add(adminIotOrderList.get(i).get("level_"));
            rows.add(row);
        }
        data.setRows(rows);
        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xlsx";
        try {
            ExcelUtils.exportExcel(response, fileName, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
