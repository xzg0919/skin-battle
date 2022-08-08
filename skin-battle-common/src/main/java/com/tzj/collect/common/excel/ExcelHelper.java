package com.tzj.collect.common.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelHelper {





    /**
     * @Title: importExeclFileForPoi
     * @Description 读取导入的EXECL的内容 返回List
     * @param @param importFile
     * @param @param startRow 索引从0开始
     * @param @param fileType
     * @param @return List<Map<String, Object>>
     * @param @throws IOException
     * @return List<Map<String,Object>>    返回类型
     * @date 2015年9月28日 上午10:29:12
     * @author KevinWu
     */
    public static List<Map<String, Object>> importExeclFileForPoi(File importFile, int startRow, String fileType) throws IOException {
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        InputStream stream = new FileInputStream(importFile);
        Workbook wb = null;
        if (("xls".equals(fileType))) {
            wb = new HSSFWorkbook(stream);
        }else if (("xlsx").equals(fileType)) {
            wb = new XSSFWorkbook(stream);
        }else {
            System.out.println("您输入的excel格式不正确");
        }
        Sheet sheet = wb.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(startRow);
        int colNum = row.getPhysicalNumberOfCells();
        for (int i = startRow; i <= rowNum; i++) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            row = sheet.getRow(i);
            if(row==null){
                list.add(map);
                continue;
            }
            int j = 0;
            while (j < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                String str = getCellFormatValue(row.getCell((short) j)).trim();
                map.put("map"+j, str);
                j++;
            }
            list.add(map);
        }
        return list;
    }

    /**
     * @Title: getCellFormatValue
     * @Description 根据HSSFCell类型设置数据
     * @param @param cell 单元格
     * @param @return String
     * @return String    返回类型
     * @date 2015年9月28日 上午10:50:56
     * @author KevinWu
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_NUMERIC:
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        //cellvalue = cell.getDateCellValue().toLocaleString();

                        //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        // 防止数字过长变成科学计数法
                        DecimalFormat df = new DecimalFormat("#");

                        cellvalue = df.format(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case Cell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
}


