package com.tzj.collect.core.param.picc;

public class PiccCompanyBean {
    private Long id ;

    /**
     * 企业名
     */
    private String  name;

    /**
     * 登录名
     */
    private String  userName;

    /**
     * 密码
     */
    private String  password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
