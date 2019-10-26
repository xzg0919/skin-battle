package com.tzj.collect.controller.admin;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.core.service.VoucherAliService;
import com.tzj.collect.core.service.VoucherCodeService;
import com.tzj.collect.entity.VoucherAli;
import com.tzj.collect.entity.VoucherCode;

/**
*
* <p>Created on2019年10月24日</p>
* <p>Title:       [杭州绿账]_[]_[]</p>
* <p>Description: [支付宝券码]</p>
* <p>Copyright:   Copyright (c) 2017</p>
* <p>Company:     上海挺之军信息科技有限公司 </p>
* <p>Department:  研发部</p>
* @author         [杨欢]
* @version        1.0
*/
@Controller
@RequestMapping("/ali")
public class VoucherCodeController
{
    @Autowired
    private VoucherAliService voucherAliService;

    @Resource
    private VoucherCodeService voucherCodeService;

    /**
     * 
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[券列表]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    @RequestMapping("/list")
    public String getVoucherCodeList(final ModelMap model)
    {
        EntityWrapper<VoucherAli> wrapper = new EntityWrapper<VoucherAli>();
        wrapper.orderBy("create_date", false);
        wrapper.eq("del_flag", 0);
        List<VoucherAli> voucherAliList = voucherAliService.selectList(wrapper);
        model.addAttribute("page", getPageCode(voucherAliList));
        return "admin/voucherCodeList";
    }

    /**
     * 
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[生成券码]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    @RequestMapping("/makeCode")
    public @ResponseBody String makeCode(String id, final ModelMap model)
    {
        String returnStr = "群里找杨欢";
        try
        {
            returnStr = voucherAliService.makeCode(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            returnStr = "出错了" + e.getMessage();
        }
        return returnStr;
    }

    /**
     * 
     * <p>Created on 2019年10月24日</p>
     * <p>Description:[转为页面代码]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return Object
     */
    private String getPageCode(List<VoucherAli> voucherAliList)
    {
        StringBuffer sb = new StringBuffer("");
        VoucherAli voucherAli = null;
        if (null != voucherAliList && !voucherAliList.isEmpty())
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0, j = voucherAliList.size(); i < j; i++)
            {
                voucherAli = voucherAliList.get(i);
                sb.append("<td class=tac>");
                sb.append(i);
                sb.append("</td>");
                sb.append("<td class=tac id='n_");
                sb.append(voucherAli.getId());
                sb.append("'>");
                sb.append(voucherAli.getVoucherName());
                sb.append("</td>");

                sb.append("<td class=tac>");
                sb.append(getDis(voucherAli));
                sb.append("</td>");

                sb.append("<td class=tac>");
                sb.append(voucherAli.getLowMoney());
                sb.append("</td>");
                sb.append("<td class=tac>");
                sb.append(voucherAli.getVoucherCount());
                sb.append("</td>");
                sb.append("<td class=tac>");
                sb.append(dateFormat.format(voucherAli.getPickupStart()));
                sb.append("<br>-<br>");
                sb.append(dateFormat.format(voucherAli.getPickupEnd()));
                sb.append("</td>");

                sb.append("<td class=tac>");
                sb.append(getValidTime(voucherAli, dateFormat));
                sb.append("</td>");

                sb.append("<td class=tac>");
                sb.append(voucherAli.getVoucherCount());
                sb.append("</td>");
                sb.append("<td class=tac>");
                sb.append(voucherAli.getPickCount());
                sb.append("</td>");
                sb.append("<td class=tac>");
                sb.append(voucherAli.getUseCount());
                sb.append("</td>");
                sb.append("<td class=tac>");
                sb.append(" 【查看】   ");
                if ("0".equals(voucherAli.getMaked()))
                {
                    sb.append("<span  style='cursor:pointer' id='");
                    sb.append("make_");
                    sb.append(voucherAli.getId());
                    sb.append("' target='");
                    sb.append(voucherAli.getId());
                    sb.append("'>");
                    sb.append(" 【生成券码】 </span>");
                }

                sb.append("<span style='cursor:pointer' id='");
                sb.append("expo_");
                sb.append(voucherAli.getId());
                sb.append("' target='");
                sb.append(voucherAli.getId());
                sb.append("'>");
                sb.append(" 【导出券码】 </span>");

                sb.append("</td>");
            }
        }
        return sb.toString();
    }

    /**
     * <p>Created on 2019年10月24日</p>
     * <p>Description:[获取折扣信息]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return Object
     */
    private String getDis(VoucherAli voucherAli)
    {
        String money = null;
        switch (voucherAli.getVoucherType())
        {
        case "BD":
            money = voucherAli.getDis() + "折";
            break;
        case "BE":
            money = voucherAli.getMoney() + "元";
            break;
        case "HP":
            money = voucherAli.getDis() + "%";
            break;
        case "HR":
            money = voucherAli.getMoney() + "元";
            break;
        }
        return money;
    }

    /**
     * <p>Created on 2019年10月24日</p>
     * <p>Description:[券有效期]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return Object
     */
    private String getValidTime(VoucherAli voucherAli, DateFormat dateFormat)
    {
        String validTime = null;
        if ("absolute".equals(voucherAli.getValidType()))
        {
            validTime = dateFormat.format(voucherAli.getValidStart()).concat("<br>-<br>")
                    .concat(dateFormat.format(voucherAli.getValidEnd()));
        }
        else
        {
            validTime = "领取后 " + voucherAli.getValidDay() + " 天有效";
        }
        return validTime;
    }
    /**
     * 
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[导出券码]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    @RequestMapping("/expoCode")
    public String expoCode(String id, String vname, final ModelMap model, HttpServletResponse response)
    {
        EntityWrapper<VoucherCode> wrapper = new EntityWrapper<VoucherCode>();
        wrapper.eq("voucher_id", id);
        wrapper.eq("del_flag", 0);
        PrintWriter printWriter = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            List<VoucherCode> voucherCodeList = voucherCodeService.selectList(wrapper);
            response.setCharacterEncoding("gbk");
            response.setContentType("text/csv");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(vname + ".csv", "UTF-8"));
            printWriter = response.getWriter();
            // 写入文件头部
            for (VoucherCode voucherCode : voucherCodeList)
            {
                sb = sb.append("\"").append(voucherCode.getVoucherCode()).append("\",");
                printWriter.print(sb.toString());
            }
            printWriter.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(null != printWriter)
            {
                printWriter.close();
            }
        }
        return null;
    }

}
