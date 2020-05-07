package com.tzj.green.controller.admin;


import com.tzj.green.common.utils.CsvUtils;
import com.tzj.green.service.MemberCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("add/csv")
public class AddCsvController {
    @Autowired
    private MemberCardService memberCardService;

    /**
     * 批量导入券
     * @param response
     * @return
     */
    @RequestMapping(value = "/imports")
    public Object imports(final MultipartFile file, final HttpServletRequest request, final HttpServletResponse response, String id)
    {
        return "addExcel";
    }


    /**
     * 批量导入券
     * @param response
     * @return
     */
    @RequestMapping(value = "/addCard", method = RequestMethod.POST)
    public @ResponseBody
    Object addCard(final MultipartFile file, final HttpServletRequest request, final HttpServletResponse response, String id)
    {
        List<List<String>> csvList = CsvUtils.getCsvList(file, String.class);
        memberCardService.addCard(csvList);
        return csvList;

    }

}
