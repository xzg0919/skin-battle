package com.tzj.collect.api.common.excel;

import com.tzj.collect.entity.PiccOrder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * Created on 2017-11-20
 * <p>Title:       [垃圾分类回收系统]_[]_[]</p>
 * <p>Description: [保险单导入]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿][@aliyun.com]
 * @version        1.0
 */

public class PiccOrderExcel {
	 //总行数  
    private int totalRows = 0;    
    //总条数  
    private int totalCells = 0;   
    //错误信息接收器  
    private String errorMsg;  
    //构造方法  
    public PiccOrderExcel(){}
    //获取总行数  
    public int getTotalRows()  { return totalRows;}   
    //获取总列数  
    public int getTotalCells() {  return totalCells;}   
    //获取错误信息  
    public String getErrorInfo() { return errorMsg; }    
      
  /** 
   * 读EXCEL文件，获取信息集合 
   * @param mFile
   * @return 
   */  
    public Map<String,Object> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();//获取文件名  
        Map<String,Object> userMap = null;
        try {  
            if (!validateExcel(fileName)) {// 验证文件名是否合格  
                return null;  
            }  
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本  
            if (isExcel2007(fileName)) {  
                isExcel2003 = false;  
            }  
            userMap = createExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return userMap;
    }  
    
  /** 
   * 根据excel里面的内容读取客户信息 
   * @param is 输入流 
   * @param isExcel2003 excel是2003还是2007版本 
   * @return 
   * @throws IOException 
   */  
    public Map<String,Object> createExcel(InputStream is, boolean isExcel2003) {
        Map<String,Object> userMap = null;
        try{  
            Workbook wb = null;  
            if (isExcel2003) {// 当excel是2003时,创建excel2003  
                wb = new HSSFWorkbook(is);  
            } else {// 当excel是2007时,创建excel2007  
                wb = new XSSFWorkbook(is);  
            }  
            userMap = readExcelValue(wb);// 读取Excel里面客户的信息
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return userMap;
    }  
    
  /** 
   * 读取Excel里面客户的信息 
   * @param wb 
   * @return 
   */  
    private Map<String,Object> readExcelValue(Workbook wb) {
        // 得到第一个shell  
        Sheet sheet = wb.getSheetAt(0);  
        // 得到Excel的行数  
        this.totalRows = sheet.getPhysicalNumberOfRows();  
        // 得到Excel的列数(前提是有行数)  
        if (totalRows > 1 && sheet.getRow(0) != null) {  
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();  
        }
        Map<String,Object> resultMap = new HashMap<>();
        List<PiccOrder> userList = new ArrayList<PiccOrder>();
        Integer count=0;
        Integer errorNum=0;
        Integer successNum=0;
        // 循环Excel行数  
        for (int r = 1; r < totalRows; r++) {  
            Row row = sheet.getRow(r);  
            if (row == null){  
                continue;  
            }
            PiccOrder piccOrder = new PiccOrder();
            // 循环Excel的列  
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {  
                    if (c == 0) {  
                        //如果是纯数字,比如你写的是25,cell.getNumericCellValue()获得是25.0,通过截取字符串去掉.0获得25  
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){  
                            String memberName = String.valueOf(cell.getNumericCellValue());
                            piccOrder.setMemberName(memberName.substring(0, memberName.length()-2>0?memberName.length()-2:1));//投保人姓名
                        }else{
                            piccOrder.setMemberName(cell.getStringCellValue());//投保人姓名
                        }  
                    } else if (c == 1) {  
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){  
                            String cateDate = String.valueOf(cell.getNumericCellValue());
                            piccOrder.setCreateDate(this.getDate(cateDate.substring(0, cateDate.length()-2>0?cateDate.length()-2:1)));//申请时间
                        }else{
                            piccOrder.setCreateDate(this.getDate(cell.getStringCellValue()));//申请时间
                        }  
                    }else if (c == 2) { 
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){  
                        	String idCard = String.valueOf(cell.getNumericCellValue());
                            piccOrder.setIdCard(idCard.substring(0, idCard.length()-2>0?idCard.length()-2:1));//身份证号
                        }else{
                            piccOrder.setIdCard(cell.getStringCellValue());//身份证号
                        }  
                    }else if (c == 3) { 
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){  
                        	String memberTel = String.valueOf(cell.getNumericCellValue());
                            piccOrder.setMemberTel(memberTel.substring(0, memberTel.length()-2>0?memberTel.length()-2:1));//手机号
                        }else{
                            piccOrder.setMemberTel(cell.getStringCellValue());//手机号
                        }  
                    }else if (c == 4) { 
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){  
                        	String address = String.valueOf(cell.getNumericCellValue());
                            piccOrder.setMemberAddress(address.substring(0, address.length()-2>0?address.length()-2:1));//居住地址
                        }else{
                            piccOrder.setMemberAddress(cell.getStringCellValue());//居住地址
                        }  
                    }else if (c == 5) { 
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                            piccOrder.setInsuranceNum(((long)cell.getNumericCellValue())+"");//保单号
                        }else{
                            piccOrder.setInsuranceNum(cell.getStringCellValue());//保单号
                        }
                    }else if (c == 6) { 
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){  
                        	String pickStartTime = String.valueOf(cell.getNumericCellValue());
                            piccOrder.setPickStartTime(getYMD(pickStartTime.substring(0, pickStartTime.length()-2>0?pickStartTime.length()-2:1)));//生效日期
                        }else{
                            piccOrder.setPickStartTime(getYMD(cell.getStringCellValue()));//生效日期
                        }  
                    }else if (c == 7) { 
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){  
                        	String pickEndtTime = String.valueOf(cell.getNumericCellValue());
                            piccOrder.setPickEndTime(getYMD(pickEndtTime.substring(0, pickEndtTime.length()-2>0?pickEndtTime.length()-2:1)));//失效日期
                        }else{
                            piccOrder.setPickEndTime(getYMD(cell.getStringCellValue()));//失效日期
                        }  
                    }else if (c == 8) {
                        if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                            String cancleReason = String.valueOf(cell.getNumericCellValue());
                            piccOrder.setCancelReason(cancleReason.substring(0, cancleReason.length()-2>0?cancleReason.length()-2:1));//驳回原因
                        }else{
                            piccOrder.setCancelReason(cell.getStringCellValue());//驳回原因
                        }
                    }
                }  
            }
            if(!StringUtils.isBlank(piccOrder.getInsuranceNum())&&piccOrder.getPickStartTime()!=null&&piccOrder.getPickEndTime()!=null&&StringUtils.isBlank(piccOrder.getCancelReason())){
                successNum ++;
            }else if(StringUtils.isBlank(piccOrder.getInsuranceNum())&&piccOrder.getPickStartTime()==null&&piccOrder.getPickEndTime()==null&&!StringUtils.isBlank(piccOrder.getCancelReason())){
                errorNum ++;
            }else{
                    return resultMap;
            }
            // 添加到list
            userList.add(piccOrder);
            count ++;
        }
        resultMap.put("piccOrderList",userList);
        resultMap.put("successNum",successNum);
        resultMap.put("errorNum",errorNum);
        resultMap.put("count",count);
        return resultMap;
    }  
      
    /** 
     * 验证EXCEL文件 
     *  
     * @param filePath 
     * @return 
     */  
    public boolean validateExcel(String filePath) {  
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {  
            errorMsg = "文件名不是excel格式";  
            return false;  
        }  
        return true;  
    }  
      
    // @描述：是否是2003的excel，返回true是2003   
    public static boolean isExcel2003(String filePath)  {    
         return filePath.matches("^.+\\.(?i)(xls)$");    
     }    
     
    //@描述：是否是2007的excel，返回true是2007   
    public static boolean isExcel2007(String filePath)  {    
         return filePath.matches("^.+\\.(?i)(xlsx)$");
     }

    public Date getDate(String date) {
        Date time = null;
        if(StringUtils.isBlank(date)){
            return null;
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                time = sdf.parse(date);
            }catch (Exception e){
                e.printStackTrace();
            }
            return time;
        }
    }

    private Date getYMD(String date) {
        Date time = null;
        if(StringUtils.isBlank(date)){
            return null;
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                time = sdf.parse(date);
            }catch (Exception e){
                e.printStackTrace();
            }
            return time;
        }
    }


}
