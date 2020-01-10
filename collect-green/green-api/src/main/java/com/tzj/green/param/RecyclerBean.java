package com.tzj.green.param;


import lombok.Data;

@Data
public class RecyclerBean {

    private String id;

    private String communityId;

    private String houseId;

    private String status;  //1同意  2拒绝

    private String cardType; //0社区分类元  1社区回收员  2社区管理员

    private String name;

    private String tel;

    private PageBean pageBean;




}
