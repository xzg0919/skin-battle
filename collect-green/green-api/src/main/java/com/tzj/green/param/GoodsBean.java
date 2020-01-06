package com.tzj.green.param;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

@Data
public class GoodsBean {


    /**
     * 主键
     */
    private String id;
    /**
     * 所属的企业Id
     */
    private String companyId;
    /**
     * 商品类型  0实物  1虚拟券
     */
    private String type;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品编号
     */
    private String goodsNo;
    /**
     * 市场价
     */
    private String marketPrice;
    /**
     * 商品数量
     */
    private String goodsNum;
    /**
     * 商品冻结数量
     */
    private String goodsFrozenNum;
    /**
     * 兑换所需积分
     */
    private String points;
    /**
     * 商品说明
     */
    private String detail;
    /**
     * 商品图片
     */
    private String icon;
    /**
     * 商品大图片
     */
    private String bigIcon;

    private PageBean pageBean;



}
