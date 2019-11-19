package com.tzj.collect.controller;


import com.tzj.collect.common.excel.ExcelData;
import com.tzj.collect.common.excel.ExcelUtils;
import com.tzj.collect.service.DailyLexiconService;
import com.tzj.collect.service.impl.DailyWeekRankingServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/last")
public class OutExcelController {

    @Resource
    private DailyLexiconService dailyLexiconService;

    @RequestMapping("/week")
    public void  getRecruitListOutExcel(HttpServletResponse response) throws Exception{
        List<Map<String, Object>> dailyLastWeekRanking =  dailyLexiconService.weekDresserList();
        ExcelData data = new ExcelData();
        data.setName("Ranking");
        //添加表头
        List<String> titles = new ArrayList<>();
        titles.add("排名");
        titles.add("昵称");
        titles.add("电话");
        titles.add("分数");
        titles.add("答题次数");
        titles.add("红包领取次数");
        titles.add("总金额");
        data.setTitles(titles);
        //添加列
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for(int i=0; i<dailyLastWeekRanking.size();i++){
            row=new ArrayList();
            row.add(i);
            row.add(dailyLastWeekRanking.get(i).get("linkName"));
            row.add(dailyLastWeekRanking.get(i).get("mobile"));
            row.add(dailyLastWeekRanking.get(i).get("score"));
            row.add(dailyLastWeekRanking.get(i).get("sum"));
            row.add(dailyLastWeekRanking.get(i).get("count_"));
            row.add(dailyLastWeekRanking.get(i).get("price"));
            rows.add(row);
        }
        data.setRows(rows);
        ExcelUtils.exportExcel(response, LocalDate.now() + ".xlsx", data);
    }

}
