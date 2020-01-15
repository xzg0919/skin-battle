package com.tzj.green.param;


import lombok.Data;

@Data
public class CategoryBean {


    private String categoryId;
    /**
     * 增值积分
     */
    private String addPoints;
    /**
     * 减值积分
     */
    private String subtractPoints;
    /**
     * 是否打开此标签0关闭  1打开
     */
    private String isOpen;


}
