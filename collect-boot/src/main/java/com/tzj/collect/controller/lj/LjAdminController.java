package com.tzj.collect.controller.lj;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.commom.redis.RedisUtil;
import com.tzj.collect.core.param.admin.LjAdminBean;
import com.tzj.collect.core.param.admin.RecyclersBean;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.*;
import com.tzj.module.common.utils.DateUtils;
import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("lj/admin")
public class LjAdminController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private RecyclersService recyclersService;
    @Autowired
    RedisUtil redisUtil;


    @RequestMapping("/home")
    public String getHome() {

        return "lj/home";
    }

    @RequestMapping("/login")
    public String getLogin() {
        return "lj/login";
    }

    @RequestMapping("/fwsDate")
    public String getFwsDate(LjAdminBean ljAdminBean, final ModelMap model) {
        List<Area> cityList = areaService.getCityListByLj();
        List<Company> companyList = companyService.selectList(new EntityWrapper<Company>());
        model.addAttribute("cityList", cityList);
        model.addAttribute("companyList", companyList);
        return "lj/fwsDate";
    }

    @RequestMapping("/recyPage")
    public String recyPage(RecyclersBean recyclersBean, final ModelMap model) {
        List<Recyclers> recyclersList = new ArrayList<>();
        if (StringUtils.isNotBlank(recyclersBean.getRecyclerTel()) || StringUtils.isNotBlank(recyclersBean.getRecyclerName())) {
            recyclersList = recyclersService.selectList(new EntityWrapper<Recyclers>().eq("del_flag", 0)
                    .eq(StringUtils.isNotBlank(recyclersBean.getRecyclerName()), "name_", recyclersBean.getRecyclerName())
                    .eq(StringUtils.isNotBlank(recyclersBean.getRecyclerTel()), "tel", recyclersBean.getRecyclerTel()));
            recyclersList.forEach(recycler -> {
                String key = "recyclerDayTimes:" + DateUtils.formatDate(new Date(), "yyyyMMdd") + ":" + recycler.getId();
                //临时使用companyCount为下单 数
                recycler.setCompanyCount(redisUtil.hasKey(key) ? (Integer) redisUtil.get(key) : 0);
            });
        }
        model.addAttribute("recyclersList", recyclersList);
        model.addAttribute("tel", StringUtils.isNotBlank(recyclersBean.getRecyclerTel()) ? recyclersBean.getRecyclerTel() : "");
        model.addAttribute("recyclerName", StringUtils.isNotBlank(recyclersBean.getRecyclerName()) ? recyclersBean.getRecyclerName() : "");
        return "lj/recyPage";
    }

    @RequestMapping("/editRcy")
    @ResponseBody
    public String editRcy(@RequestParam(name = "recyclerId") Long recyclerId, @RequestParam(name = "allowTimes") Integer allowTimes) {
        Recyclers recyclers = recyclersService.selectById(recyclerId);
        recyclers.setAllowTimes(allowTimes);
        recyclersService.updateById(recyclers);
        return "success";
    }

    @RequestMapping("/homeHead")
    public String getHomeHead() {

        return "lj/homeHead";
    }

    @RequestMapping("/leftTree")
    public String getLeftTree() {

        return "lj/leftTree";
    }

    @RequestMapping("/ywDate")
    public String getYwDate(LjAdminBean ljAdminBean, final ModelMap model) {

        List<Area> cityList = areaService.getCityListByLj();
        List<Company> companyList = companyService.selectList(new EntityWrapper<Company>());
        model.addAttribute("cityList", cityList);
        model.addAttribute("companyList", companyList);
        return "lj/ywDate";
    }

    @RequestMapping("getAreaList")
    public @ResponseBody
    List<Area> getAreaList(LjAdminBean ljAdminBean) {
        List<Area> areaList = null;
        if (StringUtils.isNotBlank(ljAdminBean.getCityId())) {
            areaList = areaService.selectList(new EntityWrapper<Area>().eq("parent_id", ljAdminBean.getCityId().replace(",", "")));
        }
        return areaList;
    }

    @RequestMapping("getStreetList")
    public @ResponseBody
    List<Area> getStreetList(LjAdminBean ljAdminBean) {
        List<Area> streetList = null;
        if (StringUtils.isNotBlank(ljAdminBean.getAreaId())) {
            streetList = areaService.selectList(new EntityWrapper<Area>().eq("parent_id", ljAdminBean.getAreaId().replace(",", "")));
        }
        return streetList;
    }

    @RequestMapping("getYwDateDetail")
    public @ResponseBody
    Map<String, Object> getYwDateDetail(LjAdminBean ljAdminBean) {
        //注册总用户数量
        long memberCount = memberService.getMemberCount();
        //平台总订单数
        int orderCount = orderService.getOrderCountByLj(ljAdminBean);
        //家电订单量
        int digitalCount = orderService.selectCount(new EntityWrapper<Order>().eq("title", "1").in("status_", "0,1,2,3"));
        //大件垃圾订单量
        int bigCount = orderService.selectCount(new EntityWrapper<Order>().eq("title", "4").in("status_", "0,1,2,3"));
        //五废订单量
        int houseCount = orderService.selectCount(new EntityWrapper<Order>().eq("title", "2").in("status_", "0,1,2,3"));
        //五公斤订单量
        int fiveKgCount = orderService.selectCount(new EntityWrapper<Order>().eq("title", "3").in("status_", "0,1,2,3"));
        //五废订单下单选择10倍积分的订单总量
        int greenOrderCount = orderService.selectCount(new EntityWrapper<Order>().eq("title", "2").eq("is_cash", "1").in("status_", "0,1,2,3"));
        //当天注册会员数量
        long memberCountToDay = memberService.getMemberCountToDay();
        //开通街道数量
        int communityCount = communityService.getCommunityCountByLj(ljAdminBean);
        //五废服务商回收人员数量
        int recyclersCount = recyclersService.getRecyclersCountByLj(ljAdminBean);
        //未派订单总量
        int initOrderCount = orderService.getInitCountByLj(ljAdminBean);
        //已派订单总量
        int tosendOrderCount = orderService.getTosendCountByLj(ljAdminBean);
        //已接订单总量
        int readyOrderCount = orderService.getReadyCountByLj(ljAdminBean);
        //家电订单量
        int digitalCountByLj = orderService.getOrderCountBytitle(ljAdminBean, "1", null);
        //大件垃圾订单量
        int bigCountByLj = orderService.getOrderCountBytitle(ljAdminBean, "4", null);
        //五废订单量
        int houseCountByLj = orderService.getOrderCountBytitle(ljAdminBean, "2", null);
        //五公斤订单量
        int fiveKgCountByLj = orderService.getOrderCountBytitle(ljAdminBean, "3", null);
        //五废订单下单选择10倍积分的订单总量
        int greenOrderCountByLj = orderService.getOrderCountBytitle(ljAdminBean, "2", "1");
        //大件垃圾平均每单支付金额
        Double greenBigPaymentOrderPrice = orderService.getGreenBigPaymentOrderPrice(ljAdminBean);

        List<Map<String, Object>> orderCategoryByLj = orderService.getOrderCategoryByLj(ljAdminBean);
        //要钱五废
        List<Map<String, Object>> houseOrderCategoryByLjAsCash = orderService.getHouseOrderCategoryByLj(ljAdminBean, "0");
        //不要钱五废
        List<Map<String, Object>> houseOrderCategoryByLjAsGreen = orderService.getHouseOrderCategoryByLj(ljAdminBean, "1");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("memberCount", memberCount);
        resultMap.put("orderCount", orderCount);
        resultMap.put("digitalCount", digitalCount);
        resultMap.put("bigCount", bigCount);
        resultMap.put("houseCount", houseCount);
        resultMap.put("fiveKgCount", fiveKgCount);
        resultMap.put("greenOrderCount", greenOrderCount);
        resultMap.put("memberCountToDay", memberCountToDay);
        resultMap.put("communityCount", communityCount);
        resultMap.put("recyclersCount", recyclersCount);
        resultMap.put("initOrderCount", initOrderCount);
        resultMap.put("tosendOrderCount", tosendOrderCount);
        resultMap.put("readyOrderCount", readyOrderCount);
        resultMap.put("digitalCountByLj", digitalCountByLj);
        resultMap.put("bigCountByLj", bigCountByLj);
        resultMap.put("houseCountByLj", houseCountByLj);
        resultMap.put("fiveKgCountByLj", fiveKgCountByLj);
        resultMap.put("greenOrderCountByLj", greenOrderCountByLj);
        resultMap.put("greenBigPaymentOrderPrice", greenBigPaymentOrderPrice == null ? "0" : greenBigPaymentOrderPrice);
        resultMap.put("orderCategoryByLj", orderCategoryByLj.isEmpty() ? null : orderCategoryByLj);
        resultMap.put("houseOrderCategoryByLjAsCash", houseOrderCategoryByLjAsCash.isEmpty() ? null : houseOrderCategoryByLjAsCash);
        resultMap.put("houseOrderCategoryByLjAsGreen", houseOrderCategoryByLjAsGreen.isEmpty() ? null : houseOrderCategoryByLjAsGreen);
        return resultMap;
    }

    @RequestMapping("getFwsDateDetail")
    public @ResponseBody
    Map<String, Object> getFwsDateDetail(LjAdminBean ljAdminBean) {
        //平均派单时间
        Double avgTosendDate = orderService.avgOrMaxDateByOrder(ljAdminBean, "1", "avg", "2");
        //平均接单时间
        Double avgAlreadyDate = orderService.avgOrMaxDateByOrder(ljAdminBean, "2", "avg", "2");
        //平均完成订单时间
        Double avgCompleteDate = orderService.avgOrMaxDateByOrder(ljAdminBean, "3", "avg", "2");
        //最大派单时间
        Double maxTosendDate = orderService.avgOrMaxDateByOrder(ljAdminBean, "1", "max", "2");
        //最大接单时间
        Double maxAlreadyDate = orderService.avgOrMaxDateByOrder(ljAdminBean, "2", "max", "2");
        //最大完成订单时间
        Double maxCompleteDate = orderService.avgOrMaxDateByOrder(ljAdminBean, "3", "max", "2");
        //平均派单时间
        Double avgTosendDateDq = orderService.avgOrMaxDateByOrder(ljAdminBean, "1", "avg", "1");
        //平均接单时间
        Double avgAlreadyDateDq = orderService.avgOrMaxDateByOrder(ljAdminBean, "2", "avg", "1");
        //平均完成订单时间
        Double avgCompleteDateDq = orderService.avgOrMaxDateByOrder(ljAdminBean, "3", "avg", "1");
        //最大派单时间
        Double maxTosendDateDq = orderService.avgOrMaxDateByOrder(ljAdminBean, "1", "max", "1");
        //最大接单时间
        Double maxAlreadyDateDq = orderService.avgOrMaxDateByOrder(ljAdminBean, "2", "max", "1");
        //最大完成订单时间
        Double maxCompleteDateDq = orderService.avgOrMaxDateByOrder(ljAdminBean, "3", "max", "1");
        //订单总数
        Integer orderCount = orderService.getSumOrderBylj(ljAdminBean);
        //订单取消率
        Integer cancelOrderCount = orderService.getOrderLjByStatus(ljAdminBean, "4");
        //订单驳回率
        Integer rejectedOrderCount = orderService.getOrderLjByStatus(ljAdminBean, "5");
        //订单完成率
        Integer completeOrderCount = orderService.getOrderLjByStatus(ljAdminBean, "3");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("avgTosendDate", avgTosendDate);
        resultMap.put("avgAlreadyDate", avgAlreadyDate);
        resultMap.put("avgCompleteDate", avgCompleteDate);
        resultMap.put("maxTosendDate", maxTosendDate);
        resultMap.put("maxAlreadyDate", maxAlreadyDate);
        resultMap.put("maxCompleteDate", maxCompleteDate);
        resultMap.put("avgTosendDateDq", avgTosendDateDq);
        resultMap.put("avgAlreadyDateDq", avgAlreadyDateDq);
        resultMap.put("avgCompleteDateDq", avgCompleteDateDq);
        resultMap.put("maxTosendDateDq", maxTosendDateDq);
        resultMap.put("maxAlreadyDateDq", maxAlreadyDateDq);
        resultMap.put("maxCompleteDateDq", maxCompleteDateDq);
        resultMap.put("orderCount", orderCount);
        resultMap.put("cancelOrderCount", ((float) cancelOrderCount) / ((float) orderCount));
        resultMap.put("rejectedOrderCount", ((float) rejectedOrderCount) / ((float) orderCount));
        resultMap.put("completeOrderCount", ((float) completeOrderCount) / ((float) orderCount));
        return resultMap;
    }


    @GetMapping("/cancelOrderBatch")
    public String cancelOrderBatch(LjAdminBean ljAdminBean, final ModelMap model) {

        List<Company> companyList = companyService.selectList(new EntityWrapper<Company>());
        model.addAttribute("companyList", companyList);
        return "lj/cancelOrderBatch";
    }


    @PostMapping("cancelOrderBatch")
    public @ResponseBody
    String cancelOrderBatch(LjAdminBean ljAdminBean) {
        try {
            orderService.cancelOrderBatch(ljAdminBean.getCompanyId(), ljAdminBean.getEndDate());
        } catch (Exception e) {
            e.printStackTrace();
            return "订单取消失败";
        }
        return "订单取消成功";
    }


    @PostMapping("cancelOrder")
    public @ResponseBody
    String cancelOrder(LjAdminBean ljAdminBean) {
        try {
            orderService.cancelOrder(   ljAdminBean.getOrderNo());
        } catch (Exception e) {
            e.printStackTrace();
            return "订单取消失败";
        }
        return "订单取消成功";
    }

}
