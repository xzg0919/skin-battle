package com.tzj.collect.flcx.controller;


import com.tzj.collect.api.commom.excel.ExcelUtils;
import com.tzj.collect.service.FlcxTypeService;
import com.tzj.module.easyopen.ApiConfig;
import com.tzj.module.easyopen.support.ApiController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 垃圾分类查询excel处理
 *
 * @author sgmark
 * @create 2019-06-24 14:19
 **/
@Controller
@RequestMapping("flcx")
public class FlcxExcelController  extends ApiController{

    @Resource
    private FlcxTypeService flcxTypeService;

    @RequestMapping("inportxls")
    @ResponseBody
    public Map<String, Object> flcxExcelInput(final MultipartFile file){
        List<Map<String, String>> mapList = new ArrayList<>();
        List<Map<String, String>> list = null;
        List<String> keyArray = Arrays.asList("name","recover","type");
        Integer startRow = 0;
        do {
            try {
                list = ExcelUtils.getDataListByXLSExcel(file.getInputStream(), keyArray, startRow);
            } catch (Exception e) {
                e.printStackTrace();
            }
            startRow ++;
            mapList.addAll(list);
        }while (list.size() > 0);
        //返回保存结果条数
        mapList.size();
        mapList = mapList.stream().filter(map -> map.size() > 1).collect(Collectors.toList());
        flcxTypeService.inputLinAndType(mapList);
        System.out.println(mapList.size());
        return null;
    }

    @RequestMapping("update")
    @ResponseBody
    public Map<String, Object> flcxExcelUpdate(final MultipartFile file){
        List<Map<String, String>> mapList = new ArrayList<>();
        List<Map<String, String>> list = null;
        List<String> keyArray = Arrays.asList("name","type");
        Integer startRow = 0;
        do {
            try {
                list = ExcelUtils.getDataListByXLSExcel(file.getInputStream(), keyArray, startRow);
            } catch (Exception e) {
                e.printStackTrace();
            }
            startRow ++;
            mapList.addAll(list);
        }while (list.size() > 0);
        //返回保存结果条数
        mapList.size();
        mapList = mapList.stream().filter(map -> map.size() > 1).collect(Collectors.toList());
        flcxTypeService.updateType(mapList);
        System.out.println(mapList.size());
        return null;
    }

    @Override
    protected void initApiConfig(ApiConfig apiConfig) {

    }
}
