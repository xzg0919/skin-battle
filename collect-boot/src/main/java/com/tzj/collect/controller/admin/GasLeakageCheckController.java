package com.tzj.collect.controller.admin;

import com.tzj.collect.common.excel.ExcelData;
import com.tzj.collect.common.excel.ExcelUtils;
import com.tzj.collect.core.service.GasLeakageCheckService;
import com.tzj.collect.entity.GasLeakageCheck;
import com.tzj.module.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/12/7 09:31
 * @Description:
 */
@Controller
@RequestMapping("/gasLeakageCheck")
public class GasLeakageCheckController {

    @Autowired
    GasLeakageCheckService gasLeakageCheckService;


    @ResponseBody
    @PostMapping("saveInfo")
    public String saveInfo(String tel, String address, String contactName, String province, String city, String distrct, @RequestParam(defaultValue = "",required = false) String from){

        if(StringUtils.isBlank(tel)){
            return "请填写手机号码！";
        }

        if(StringUtils.isBlank(address)){
            return "请填写地址！";
        }

        if(StringUtils.isBlank(contactName)){
            return "请填写姓名！";
        }

        GasLeakageCheck gasLeakageCheck=new GasLeakageCheck();
        gasLeakageCheck.setAddress(address);
        gasLeakageCheck.setContactName(contactName);
        gasLeakageCheck.setTel(tel);
        gasLeakageCheck.setProvince(province);
        gasLeakageCheck.setCity(city);
        gasLeakageCheck.setDistrict(distrct);
        gasLeakageCheck.setFrom(from);
        gasLeakageCheckService.insert(gasLeakageCheck);
        return "提交成功";
    }


    @RequestMapping("/exportExcel")
    public void outEnterpriseCodeExcel(HttpServletResponse response, String startDate,String endDate) throws Exception {
        List<GasLeakageCheck> listByCreateDate = gasLeakageCheckService.getListByCreateDate(startDate, endDate);
        ExcelData data = new ExcelData();
        data.setName("燃气泄露检测数据");
        List<String> titles = new ArrayList<>();
        titles.add("序号");
        titles.add("手机号");
        titles.add("姓名");
        titles.add("省");
        titles.add("市");
        titles.add("区");
        titles.add("地址");
        titles.add("来源");
        titles.add("提交时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        List<Object> row = null;
        for (int i = 0; i < listByCreateDate.size(); i++) {
            row = new ArrayList();
            row.add(listByCreateDate.get(i).getId());
            row.add(listByCreateDate.get(i).getTel());
            row.add(listByCreateDate.get(i).getContactName());
            row.add(listByCreateDate.get(i).getProvince());
            row.add(listByCreateDate.get(i).getCity());
            row.add(listByCreateDate.get(i).getDistrict());
            row.add(listByCreateDate.get(i).getAddress());
            row.add(listByCreateDate.get(i).getFrom());
            row.add(DateUtils.formatDate(listByCreateDate.get(i).getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
            rows.add(row);

        }
        data.setRows(rows);
        String fileName =  "燃气泄露检测.xlsx";
        ExcelUtils.exportExcel(response, fileName, data);
    }
}
