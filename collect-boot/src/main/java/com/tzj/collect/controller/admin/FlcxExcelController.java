package com.tzj.collect.controller.admin;

import com.tzj.collect.api.common.excel.ExcelUtils;
import com.tzj.collect.service.FlcxTypeService;
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
public class FlcxExcelController {

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
}
