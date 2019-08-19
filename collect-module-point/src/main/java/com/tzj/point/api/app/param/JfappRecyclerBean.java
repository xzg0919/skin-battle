package com.tzj.point.api.app.param;


import lombok.Data;

@Data
public class JfappRecyclerBean {

    //姓名
    private String name;
    //电话
    private String tel;
    //密码
    private  String password;
    //设备终端号
    private String terminalNo;

    private String cardNo;

    private String aliUserId;

    private String point;
    /**
     * 类型0增加  1减少
     */
    private String type;

    //用户姓名
    private String userName;
    //用户手机号
    private String mobile;

    private String recyclerName;

    private PageBean pageBean;

    private String startDate;

    private String endDate;



}
