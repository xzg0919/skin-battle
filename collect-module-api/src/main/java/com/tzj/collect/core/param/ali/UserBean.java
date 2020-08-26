package com.tzj.collect.core.param.ali;

import lombok.Data;

@Data
public class UserBean {

    private String userImg ;
    private String userPhone;
    private String userName;
    private String token;
    private String qrCode;
    private String equipmentCode;
    private Integer rubbishId;
    private Double rubbishWeight;
    private Double equipmentLongitude;
    private Double equipmentLatitude;

    public static String getCategoryNameById(Integer categoryId){
        String categoryName = null;
        switch (categoryId){
            case 101:   categoryName = "废纸";
                break;
            case 102:   categoryName = "废塑料";
                break;
            case 103:   categoryName = "废纺织品";
                break;
            case 104:   categoryName = "废金属";
                break;
        }
        return categoryName;
    }
}
