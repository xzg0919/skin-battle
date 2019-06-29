package com.tzj.collect.controller.admin;

import com.taobao.api.ApiException;
import com.tzj.collect.api.common.excel.ExcelUtils;
import com.tzj.collect.service.AreaService;
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
@RequestMapping("area")
public class AreaExcelController {

    @Resource
    private AreaService areaService;

    @RequestMapping("inportxls")
    @ResponseBody
    public Map<String, Object> flcxExcelInput(final MultipartFile file)throws ApiException {
        List<Map<String, String>> mapList = new ArrayList<>();
        List<Map<String, String>> list = null;
        List<String> keyArray = Arrays.asList("id","name","parentCode","parent","code");
        try {
            list = ExcelUtils.getDataListByXLSExcel(file.getInputStream(), keyArray, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapList.addAll(list);
        //返回保存结果条数
        mapList.size();
        mapList = mapList.stream().filter(map -> map.size() > 1).collect(Collectors.toList());
        areaService.inputAreaCode(mapList);
        System.out.println(mapList.size());
        return null;
    }

    @RequestMapping("add/inportxls")
    @ResponseBody
    public Map<String, Object> addAreaExcelInput(final MultipartFile file)throws ApiException {
        List<Map<String, String>> mapList = new ArrayList<>();
        List<Map<String, String>> list = null;
        List<String> keyArray = Arrays.asList("id","pri","city","area","address","all","code");
        try {
            list = ExcelUtils.getDataListByXLSExcel(file.getInputStream(), keyArray, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapList.addAll(list);
        //返回保存结果条数
        mapList.size();
        mapList = mapList.stream().filter(map -> map.size() > 1).collect(Collectors.toList());
        areaService.addInputAreaCode(mapList);
        System.out.println(mapList.size());
        return null;
    }
}
