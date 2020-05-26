package com.tzj.green.controller;


import com.tzj.green.common.excel.ExcelData;
import com.tzj.green.common.excel.ExcelUtils;
import com.tzj.green.param.CompanyBean;
import com.tzj.green.service.OrderCountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/excel")
public class OutExcelController {

    @Resource
    private OrderCountService orderCountService;

    @RequestMapping("/pointList")
    public void  getPointListOutExcel(HttpServletResponse response) throws Exception{
        List<Map<String, Object>> list =  orderCountService.getOrderCount();
        ExcelData data = new ExcelData();
        data.setName("积分商户数据统计");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("积分变更时间");
        titles.add("用户姓名");
        titles.add("联系电话");
        titles.add("积分卡号");
        titles.add("回收物重量");
        titles.add("垃圾类型");
        titles.add("可回收类型");
        titles.add("省份");
        titles.add("城市");
        titles.add("行政区");
        titles.add("街道");
        titles.add("居委会/村委会");
        titles.add("社区");
        titles.add("积分员");
        titles.add("积分变更");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<list.size();i++){
            row=new ArrayList();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.add(simpleDateFormat.format(list.get(i).get("createDate")));
            row.add(list.get(i).get("userName"));
            row.add(list.get(i).get("mobile"));
            row.add(list.get(i).get("userNo"));
            row.add(list.get(i).get("amount"));
            row.add(list.get(i).get("garbageType"));
            row.add(list.get(i).get("recycleType"));
            row.add(list.get(i).get("provinceName"));
            row.add(list.get(i).get("cityName"));
            row.add(list.get(i).get("areaName"));
            row.add(list.get(i).get("streetName"));
            row.add(list.get(i).get("communityName"));
            row.add(list.get(i).get("communityHouseName"));
            row.add(list.get(i).get("createBy"));
            row.add(list.get(i).get("points"));
            rows.add(row);
        }
        data.setRows(rows);
        ExcelUtils.exportExcel(response, LocalDate.now() + ".xlsx", data);
    }

    @RequestMapping("/pointListCount")
    public void  getPointListCountOutExcel(HttpServletResponse response ,CompanyBean companyBean) throws Exception{
        List<Map<String, Object>> list =  orderCountService.getPointCount(companyBean);
        ExcelData data = new ExcelData();
        data.setName("积分商户数据统计");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("服务商名称");
        titles.add("积分变更时间");
        titles.add("用户姓名");
        titles.add("联系电话");
        titles.add("积分卡号");
        titles.add("回收物重量");
        titles.add("垃圾类型");
        titles.add("可回收类型");
        titles.add("省份");
        titles.add("城市");
        titles.add("行政区");
        titles.add("街道");
        titles.add("居委会/村委会");
        titles.add("社区");
        titles.add("积分员");
        titles.add("积分(+)");
        titles.add("积分(-)");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<list.size();i++){
            row=new ArrayList();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.add(list.get(i).get("companyName"));
            row.add(simpleDateFormat.format(list.get(i).get("createDate")));
            row.add(list.get(i).get("userName"));
            row.add(list.get(i).get("mobile"));
            row.add(list.get(i).get("userNo"));
            row.add(list.get(i).get("amount"));
            row.add(list.get(i).get("garbageType"));
            row.add(list.get(i).get("recycleType"));
            row.add(list.get(i).get("provinceName"));
            row.add(list.get(i).get("cityName"));
            row.add(list.get(i).get("areaName"));
            row.add(list.get(i).get("streetName"));
            row.add(list.get(i).get("communityName"));
            row.add(list.get(i).get("communityHouseName"));
            row.add(list.get(i).get("createBy"));
            row.add(list.get(i).get("addPoints"));
            row.add(list.get(i).get("sumPoints"));
            rows.add(row);
        }
        data.setRows(rows);
        ExcelUtils.exportExcel(response, LocalDate.now() + ".xlsx", data);
    }

}
