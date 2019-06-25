package com.tzj.collect.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 垃圾分类查询excel处理
 *
 * @author sgmark
 * @create 2019-06-24 14:19
 **/
@Controller
@RequestMapping("flcx")
public class FlcxExcelController {
    @RequestMapping("inportxls")
    @ResponseBody
    public Map<String, Object> flcxExcelInport(final MultipartFile file){

        return null;
    }
}
