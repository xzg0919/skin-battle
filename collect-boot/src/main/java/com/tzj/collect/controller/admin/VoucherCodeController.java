package com.tzj.collect.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.common.utils.VoucherConst;
import com.tzj.collect.core.param.ali.PageBean;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.VoucherAliService;
import com.tzj.collect.core.service.VoucherCodeService;
import com.tzj.collect.core.service.VoucherMemberService;
import com.tzj.collect.entity.Area;
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
    
    @Resource
    private AreaService areaService;

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
        
       //lp();
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
    
    
    
    
    
    public void lp()
    {
    	
    	List<String> pList = getPlist(); 
    	
    	EntityWrapper<Area> wrapper = new EntityWrapper<Area>();
        
    	
    	AreaVO areaVO = null;
    	List<AreaVO> list = new ArrayList<AreaVO>();
    	  try {
			File file = new File("D:\\a.xlsx");
			Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            int index = 0;
            Row row = null;
            String reg = "[\\u4e00-\\u9fa5]+";
            String add = null;
            String provice = null;
            String city = null;
            for (int i = 1, j = sheet.getLastRowNum(); i <= j; i++)
            {
                index = i + 1;
                row = sheet.getRow(i);
                areaVO = new AreaVO();
                areaVO.setCells(null);
                areaVO.setJoin(row.getCell(0).getStringCellValue().trim().replaceAll(" ", ""));
                areaVO.setCorp(row.getCell(1).getStringCellValue().trim().replaceAll(" ", ""));
                areaVO.setTime(row.getCell(2).getStringCellValue());
                areaVO.setComp(row.getCell(3).getStringCellValue().trim().replaceAll(" ", ""));
                areaVO.setName(row.getCell(4).getStringCellValue().trim().replaceAll(" ", ""));
                if(row.getCell(5).getCellType() == 0)
                {
                	areaVO.setPhone(String.valueOf(row.getCell(5).getNumericCellValue()));
                }
                if(row.getCell(5).getCellType() == 1)
                {
                	areaVO.setPhone(row.getCell(5).getStringCellValue().trim().replaceAll(" ", ""));
                }
                areaVO.setAdd(row.getCell(6).getStringCellValue().trim().replaceAll(" ", ""));
                
                add = areaVO.getAdd().trim().replaceAll(" ", "");
                
                if (row.getCell(8) != null && !"".equals(row.getCell(8).getStringCellValue().trim()))
                {
                	areaVO.setProvi(row.getCell(7).getStringCellValue().trim().replaceAll(" ", ""));
                	areaVO.setCity(row.getCell(8).getStringCellValue().trim().replaceAll(" ", ""));
                	list.add(areaVO);
                    continue;
                }
                if(areaVO.getAdd().contains("国"))
                {
                	continue;
                }
                if(!areaVO.getAdd().matches(reg))
                {
                	continue;
                }
                if(add.contains("，") ||add.contains(")") || add.contains("、"))
                {
                	areaVO.setProvi(row.getCell(7).getStringCellValue());
                	areaVO.setCity(row.getCell(8).getStringCellValue());
                	list.add(areaVO);
                    continue;
                }
                
                if("抚顺".equals(areaVO.getAdd()))
                {
                	areaVO.getAdd();
                }
                
                // 1. 有7
                if (row.getCell(7) != null && !"".equals(row.getCell(7).getStringCellValue().trim()))
                {
                	areaVO.setCells("8");
                	areaVO.setProvi(row.getCell(7).getStringCellValue().trim().replaceAll(" ", ""));
                	city = add;
                	city = add.replaceAll(areaVO.getProvi(),"").replaceAll("省","");
                	areaVO.setCity(city);
                	list.add(areaVO);
                }
                else
                {
                	// 2 无7
                	areaVO.setCells("7,8");
                	if(add.contains("省"))
                	{
                		// 有省
                		areaVO.setProvi(add.substring(0,add.indexOf("省")+1));
                		areaVO.setCity(add.replaceAll(areaVO.getProvi(), ""));
                		list.add(areaVO);
                        continue;
                	}
                	else
                	{
                		// 无省
                		if(add.length() >= 4)
                		{
                			// 字数大于4 取两位为省，不是省的话，按市算
                			provice = add.substring(0,2);
                			if(pList.contains(provice))
                			{
                				// 前两位是省
                				city = add.replaceAll(provice, "");
                				areaVO.setProvi(provice);
                				areaVO.setCity(city);
                				list.add(areaVO);
                                continue;
                			}
                			else
                			{
                				// 没招
                				list.add(areaVO);
                                continue;
                			}
                		}
                		else
                		{
                			// 字数小于4 
                			if(add.contains("市"))
                			{
                				// 有市，截取到市 去查省//TODO
                				city = add.substring(0,add.indexOf("市")+1);
                				areaVO.setCity(city);
                				wrapper= new EntityWrapper<Area>();
                				wrapper.eq("area_name", city);
                				List<Area> areaList = areaService.selectList(wrapper);
                				if(null != areaList && !areaList.isEmpty())
                				{
                					areaVO.setProvi(areaService.selectById(areaList.get(0).getParentId()).getAreaName());
                				}
                				else
                				{
                					areaVO.setProvi("");
                				}
                				list.add(areaVO);
                                continue;
                			}
                			else
                			{
                				// 没有，按市算//TODO
                				areaVO.setCity(add);
                				wrapper= new EntityWrapper<Area>();
                				wrapper.eq("area_name", add.concat("市"));
                				List<Area> areaList = areaService.selectList(wrapper);
                				if(null != areaList && !areaList.isEmpty())
                				{
                					areaVO.setProvi(areaService.selectById(areaList.get(0).getParentId()).getAreaName());
                				}
                				else
                				{
                					areaVO.setProvi("");
                				}
                				list.add(areaVO);
                                continue;
                			}
                		}
                	}
                }
                
                
                
                
                
                
                
                
                
                
                
                
                
            }
			System.out.println("行数"+index);
			System.out.println("数据数："+list.size());
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	export(list);
    	
    	
    	
    	
    	
    	
    	
    	
    }

	private void export(List<AreaVO> list) 
	{
		 String[] titles = new String[]{"加入方式","合作方式","提交时间","企业名称","联系人姓名","联系人电话","意向城市","省份","城市"};
		 AreaVO areaVO = null;
		 //HSSFWorkbook hwb = new HSSFWorkbook();
		 
		 XSSFWorkbook hwb = new XSSFWorkbook();
		 Sheet sheet = hwb.createSheet("sheet0"); 
		 
        sheet.setDisplayGridlines(false);
        sheet.createFreezePane(0, 1);
        // 标题的字体
        Font titleFont = hwb.createFont();
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	     //标题单元格格式
        XSSFCellStyle cellTitleStyle = hwb.createCellStyle();
        cellTitleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellTitleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellTitleStyle.setFillForegroundColor((short) 24);
        cellTitleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cellTitleStyle.setFont(titleFont);
        cellTitleStyle.setBorderBottom((short) 1);
        cellTitleStyle.setBorderLeft((short) 1);
        cellTitleStyle.setBorderTop((short) 1);
        cellTitleStyle.setBorderRight((short) 1);
        
        
        // 内容单元格 居中
        XSSFCellStyle cellStyleCenter = hwb.createCellStyle();
        cellStyleCenter.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyleCenter.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyleCenter.setBorderBottom((short) 1);
        cellStyleCenter.setBorderLeft((short) 1);
        cellStyleCenter.setBorderTop((short) 1);
        cellStyleCenter.setBorderRight((short) 1);
        // 内容单元格 居←
        
        Font myFont = hwb.createFont();
        myFont.setFontHeightInPoints((short) 10);
        myFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        myFont.setColor((short)44);
        XSSFCellStyle cellStyLeft = hwb.createCellStyle();
        cellStyLeft.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyLeft.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyLeft.setFillForegroundColor((short) 24);
        cellStyLeft.setBorderBottom((short) 1);
        cellStyLeft.setBorderLeft((short) 1);
        cellStyLeft.setBorderTop((short) 1);
        cellStyLeft.setBorderRight((short) 1);
        cellStyLeft.setFont(myFont);
	        
        sheet.setColumnWidth(0, 2800);
        sheet.setColumnWidth(1, 2700);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 7000);
        sheet.setColumnWidth(4, 2800);
        sheet.setColumnWidth(5, 3100);
        sheet.setColumnWidth(6, 5600);
        sheet.setColumnWidth(7, 3500);
        sheet.setColumnWidth(8, 3500);
		
        // 设置标题
        Row  firstrow = sheet.createRow(0);
        Cell[] firstcell = new Cell[titles.length];
        for (int i = 0, j = titles.length; i < j; i++) 
        {
            firstcell[i] = firstrow.createCell(i);
            firstcell[i].setCellValue((titles[i]));
            firstcell[i].setCellStyle(cellTitleStyle);
            firstcell[i].setCellType(Cell.CELL_TYPE_STRING);
        }
		
        try 
        {
       	 Row row = null;
       	 Cell md = null;
       	 Cell sf = null;
       	 Cell yh = null;
       	 Cell hy = null;
       	 Cell xm = null;
       	 Cell rq = null;
       	 Cell zt = null;
       	 Cell gm = null;
       	 Cell lp = null;
            int index = 0;
       	 
            for(int i=0,j=list.size();i<j;i++)
            {
           	 areaVO = list.get(i);
           	 row = sheet.createRow(index + 1);
                index++;
                md = row.createCell(0);
                sf = row.createCell(1);
                yh = row.createCell(2);
                hy = row.createCell(3);
                xm = row.createCell(4);
                rq = row.createCell(5);
                zt = row.createCell(6);
                gm = row.createCell(7);
                lp = row.createCell(8);
                
                if(i==17)
                {
                	areaVO.getAdd();
                }
                md.setCellStyle(cellStyleCenter);
                sf.setCellStyle(cellStyleCenter);
                yh.setCellStyle(cellStyleCenter);
                hy.setCellStyle(cellStyleCenter);
                xm.setCellStyle(cellStyleCenter);
                rq.setCellStyle(cellStyleCenter);
                zt.setCellStyle(cellStyleCenter);
                
           	 if(null != areaVO.getCells() && !"".equals(areaVO.getCells()))
           	 {
           		 if(areaVO.getCells().contains("7"))
           		 {
           			 gm.setCellStyle(cellStyLeft);
           		 }
           		 else
           		 {
           			gm.setCellStyle(cellStyleCenter);
           		 }
           		 if(areaVO.getCells().contains("8"))
           		 {
           			 lp.setCellStyle(cellStyLeft);
           		 }
           		else
          		 {
           			lp.setCellStyle(cellStyleCenter);
          		 }
           	 }
           	 else
           	 {
           		gm.setCellStyle(cellStyleCenter);
                lp.setCellStyle(cellStyleCenter);
           	 }
           	 md.setCellValue(areaVO.getJoin());
           	 sf.setCellValue(areaVO.getCorp());
           	 yh.setCellValue(areaVO.getTime());
           	 hy.setCellValue(areaVO.getComp());
           	 xm.setCellValue(areaVO.getName());
           	 rq.setCellValue(areaVO.getPhone());
           	 zt.setCellValue(areaVO.getAdd());
           	 gm.setCellValue(areaVO.getProvi());
           	 lp.setCellValue(areaVO.getCity());
            }
            hwb.write(new FileOutputStream("D:\\a1.xlsx"));
            
            System.out.println("ok");
            
        } catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private List<String> getPlist() 
	{
		List<String> pList = new ArrayList<String>();
		
		pList.add("北京市");
		pList.add("天津市");
		pList.add("河北省");
		pList.add("山西省");
		pList.add("内蒙古自治区");
		pList.add("辽宁省");
		pList.add("吉林省");
		pList.add("黑龙江省");
		pList.add("上海市");
		pList.add("江苏省");
		pList.add("浙江省");
		pList.add("安徽省");
		pList.add("福建省");
		pList.add("江西省");
		pList.add("山东省");
		pList.add("河南省");
		pList.add("湖北省");
		pList.add("湖南省");
		pList.add("广东省");
		pList.add("广西壮族自治区");
		pList.add("海南省");
		pList.add("重庆市");
		pList.add("四川省");
		pList.add("贵州省");
		pList.add("云南省");
		pList.add("西藏自治区");
		pList.add("陕西省");
		pList.add("甘肃省");
		pList.add("青海省");
		pList.add("宁夏回族自治区");
		pList.add("新疆维吾尔自治区");

		
		return pList;
	}
    
    
	 @RequestMapping("/address")
	    public String address(final ModelMap model)
	    {
	        return "admin/address";
	    }

	 @RequestMapping("/getAddress")
	    public @ResponseBody String getAddress(String id, final ModelMap model)
	    {
		 EntityWrapper<Area> wrapper = new EntityWrapper<Area>();
		 if(!id.contains("市"))
		 {
			 id = id + "市";
		 }
			wrapper.eq("area_name", id);
			List<Area> areaList = areaService.selectList(wrapper);
			if(null != areaList && !areaList.isEmpty())
			{
				return areaService.selectById(areaList.get(0).getParentId()).getAreaName();
			}
			else
			{
				return "有弄第五";
			}
	    }
    
    

}
