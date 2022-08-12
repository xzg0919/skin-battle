package com.skin.params;

import lombok.Data;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/11 15:24
 * @Description:
 */
@Data
public class NoticeBean {

    PageBean pageBean;

    Long id ;

    private String title ;
    /** 内容 */
    private String content ;
    /** 图片 */
    private String picUrl ;

}
