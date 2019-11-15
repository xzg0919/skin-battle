package com.tzj.collect.entity;

import java.text.DecimalFormat;

public class ApiStringUtils {

/**
  * 将数据保留两位小数
  */
    public static double privatedoublegetTwoDecimal(double num) {
        DecimalFormat dFormat=new DecimalFormat("#.00");
        String yearString=dFormat.format(num);
        Double temp= Double.valueOf(yearString);
        return temp;
    }
    public static String doublegetTwoDecimal(Float num) {
        DecimalFormat dFormat=new DecimalFormat("0.00");
        return dFormat.format(num);
    }
}
