package com.tzj.green.param;


import lombok.Data;

import java.io.Serializable;

@Data
public class CompanyBean implements Serializable {


    /**
     * 联系电话
     */
    private String tel;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 服务商id
     */
    private Long companyId;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;





}
