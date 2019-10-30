package com.tzj.collect.controller.admin;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.utils.VoucherConst;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.service.VoucherAliService;
import com.tzj.collect.core.service.VoucherCodeService;
import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.entity.VoucherAli;
import com.tzj.collect.entity.VoucherCode;
import com.tzj.collect.entity.VoucherMember;

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
    
    @Resource
    private VoucherMemberService voucherMemberService;

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
        String returnStr = "ok";
        VoucherAli voucherAli = null;
        int size = 0;
        VoucherCode voucherCode = null;
        String code = null;
        List<VoucherCode> voucherCodeList = new ArrayList<VoucherCode>();
        Date now = new Date();
        try
        {
            voucherAli = voucherAliService.selectById(id);
            if(null == voucherAli)
            {
                returnStr = "券不存在!";
                return returnStr;
            }
            if("1".equals(voucherAli.getMaked()))
            {
                returnStr = "券码已生成！";
                return returnStr;
            }
            size = voucherAli.getVoucherCount().intValue();
            for(int i=1;i<=size;i++)
            {
                if(i % 5000 == 0)
                {
                    voucherCodeService.insertBatch(voucherCodeList, 5000);
                    System.out.println("++++++++++++++++++++++++++" + i);
                    voucherCodeList.clear();
                }
                voucherCode = new VoucherCode();
                voucherCode.setCreateBy(voucherAli.getCreateBy());
                voucherCode.setCreateDate(now);
                voucherCode.setDelFlag("0");
                voucherCode.setDis(voucherAli.getDis());
                voucherCode.setLowMoney(voucherAli.getLowMoney());
                voucherCode.setMoney(voucherAli.getMoney());
                voucherCode.setPickLimitTotal(voucherAli.getPickLimitTotal());
                voucherCode.setPickupEnd(voucherAli.getPickupEnd());
                voucherCode.setPickupStart(voucherAli.getPickupStart());
                voucherCode.setTopMoney(voucherAli.getTopMoney());
                voucherCode.setUpdateBy(voucherAli.getUpdateBy());
                voucherCode.setUpdateDate(now);
                voucherCode.setValidDay(voucherAli.getValidDay());
                voucherCode.setValidEnd(voucherAli.getValidEnd());
                voucherCode.setValidStart(voucherAli.getValidStart());
                voucherCode.setValidType(voucherAli.getValidType());
                voucherCode.setVersion(voucherAli.getVersion());
                voucherCode.setOrderType(voucherAli.getOrderType());
                voucherCode.setVoucherCount(voucherAli.getVoucherCount());
                voucherCode.setVoucherId(voucherAli.getId());
                voucherCode.setVoucherName(voucherAli.getVoucherName());
                voucherCode.setVoucherType(voucherAli.getVoucherType());
                code = UUID.randomUUID().toString();
                code = code.substring(code.lastIndexOf("-")+1,code.length());
                code = voucherAli.getVoucherType() + code;
                voucherCode.setVoucherCode(code);
                voucherCodeList.add(voucherCode);
            }
            if(size < 5000)
            {
                voucherCodeService.insertBatch(voucherCodeList, size);
            }
            voucherAli.setMaked("1");
            voucherAliService.updateById(voucherAli);
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
        if (VoucherConst.VOUCHER_VALIDTYPE_ABSOLUTE.equals(voucherAli.getValidType()))
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
        int size =0; 
        List<VoucherCode> voucherCodeList = null;
        try
        {
            response.setCharacterEncoding("gbk");
            response.setContentType("text/csv");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(vname + ".csv", "UTF-8"));
            size =  voucherCodeService.selectCount(wrapper);
            printWriter = response.getWriter();
            if(size < 50000)
            {
                voucherCodeList =  voucherCodeService.selectList(wrapper);
                for (VoucherCode voucherCode : voucherCodeList)
                {
                    //sb = sb.append("\"").append(voucherCode.getVoucherCode()).append("\",");
                    printWriter.print(voucherCode.getVoucherCode());
                    printWriter.write("\r\n");
                }
                printWriter.flush();
            }
            else
            {
                 PageBean pageBean = new PageBean();
                 pageBean.setPageSize(50000);
                 for(int i=0,j = size/50000 + 1;i<j;i++)
                 {
                     pageBean.setPageNumber(1 + i);
                     voucherCodeList =  voucherCodeService.getExpoPageList(pageBean,Integer.valueOf(id));
                     for (VoucherCode voucherCode : voucherCodeList)
                     {
                         printWriter.print(voucherCode.getVoucherCode());
                         printWriter.write("\r\n");
                     }
                 }
                 printWriter.flush();
            }
            
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
    
    
    
    /**
     * 
     * <p>Created on 2019年10月25日</p>
     * <p>Description:[下单选择券]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    @RequestMapping("/getVoucherForOrder")
    public Object getVoucherForOrder(final ModelMap model,Long memberId,String orderType,Integer orderPrice)
    {
        // 所有的
        List<VoucherMember>  voucherMemberList = null;
        // 可用的
        List<VoucherMember>  voucherMemberUseList = null;
        // 不能用的
        List<VoucherMember>  voucherMemberNoUseList = null;
        Map<String,Object> returnMap = new HashMap<String,Object>();
        Date now = new Date();
        try
        {
            voucherMemberList = voucherMemberService.getVoucherForOrder(memberId);
            if(null == voucherMemberList || voucherMemberList.isEmpty())
            {
                // 可用数
                returnMap.put("usefulCount","0");
                return returnMap;
            }
            voucherMemberNoUseList = new ArrayList<VoucherMember>();
            voucherMemberUseList = new ArrayList<VoucherMember>();
            for(VoucherMember vmoucherMember : voucherMemberList)
            {
                if(now.after(vmoucherMember.getValidEnd()))
                {
                    vmoucherMember.setMsg("已过期");
                    voucherMemberNoUseList.add(vmoucherMember);
                    continue;
                }
                if(now.before(vmoucherMember.getValidStart()))
                {
                    vmoucherMember.setMsg("尚未开始");
                    voucherMemberNoUseList.add(vmoucherMember);
                    continue;
                }
                if(orderPrice > vmoucherMember.getLowMoney())
                {
                    vmoucherMember.setMsg("不满足最低金额");
                    voucherMemberNoUseList.add(vmoucherMember);
                    continue;
                }
                if(!"4".equals(orderType))
                {
                    vmoucherMember.setMsg("不支持大件类型订单");
                    voucherMemberNoUseList.add(vmoucherMember);
                    continue;
                }
                voucherMemberUseList.add(vmoucherMember);
            }
            returnMap.put("usefulCount",String.valueOf(voucherMemberUseList.size()));
            returnMap.put("voucherMemberUseList",voucherMemberUseList);
            returnMap.put("voucherMemberNoUseList",voucherMemberNoUseList);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
        
        return null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

}
