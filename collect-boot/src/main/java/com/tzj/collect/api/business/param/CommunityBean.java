package com.tzj.collect.api.business.param;

import lombok.Data;

@Data
public class CommunityBean {

    /**
     * 0保存，1删除
     */
    private String saveOrDelete;

    private String communityId;

}
