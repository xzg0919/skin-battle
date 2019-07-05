package com.tzj.collect.controller.admin;


import com.tzj.collect.api.commom.redis.RedisUtil;
import com.tzj.collect.entity.PiccOrder;
import com.tzj.collect.service.PiccOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("add/picc")
public class AddPiccOrderExcelController {

    @Autowired
    private PiccOrderService piccOrderService;
    @Autowired
    private RedisUtil redisUtil;




    /**
     * picc导入用户的信息
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/piccOrderImport", method = RequestMethod.POST)
    public @ResponseBody Object piccOrderImport(final MultipartFile file, final HttpServletRequest request, final HttpServletResponse response,String id)
    {
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> piccOrderMap = piccOrderService.piccOrderImportExcel(file);
        List<PiccOrder> piccOrderList = (List<PiccOrder>)piccOrderMap.get("piccOrderList");
        if (piccOrderMap.get("count")==null){
            resultMap.put("code",500);
            resultMap.put("status","error");
            return resultMap;
        }
        redisUtil.set(id,piccOrderList,300);
        resultMap.put("successNum",piccOrderMap.get("successNum"));
        resultMap.put("errorNum",piccOrderMap.get("errorNum"));
        resultMap.put("count", piccOrderMap.get("count"));
        return resultMap;

    }

}
