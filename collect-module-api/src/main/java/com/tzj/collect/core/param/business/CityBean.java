package com.tzj.collect.core.param.business;

import lombok.Data;

import java.util.List;

@Data
public class CityBean {

    private String citycode;

    private String adcode;

    private String level;

    private String center;

    private String name;

    protected List<CityBean> districts;

    public static void main(String[] args) {
        System.out.println(String.valueOf(1));
    }


}
