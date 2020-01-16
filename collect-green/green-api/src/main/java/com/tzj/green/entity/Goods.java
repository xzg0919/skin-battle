package com.tzj.green.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * <p>Created on2019年12月30日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: [商品表实体类]</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [王灿]
 * @version        1.0
 */
@Data
@TableName("sb_goods")
public class Goods extends DataEntity<Long>
{
    /**
     * 主键
     */
    private Long id;
    /**
     * 所属的企业Id
     */
    private Long companyId;
    /**
     * 商品类型  0实物  1虚拟券
     */
    @TableField(value = "type_")
    private String type;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品编号
     */
    private String goodsNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999) + 100000);
    /**
     * 市场价
     */
    private java.math.BigDecimal marketPrice;
    /**
     * 商品数量
     */
    private Long goodsNum;
    /**
     * 商品冻结数量
     */
    private Long goodsFrozenNum;
    @TableField(exist = false)
    private Long goodsUsableNum;
    /**
     * 兑换所需积分
     */
    private Long points;
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

    public Long getGoodsUsableNum() {
        return goodsNum-goodsFrozenNum;
    }

    public void setGoodsUsableNum(Long goodsUsableNum) {
        this.goodsUsableNum = goodsUsableNum;
    }
}
