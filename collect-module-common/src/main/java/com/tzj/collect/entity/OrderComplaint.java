package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@TableName("sb_order_complaint")
@Data
public class OrderComplaint extends DataEntity<Long> {
    private Long id;

    private  String  orderNo; //订单号',
    @TableField(value="type_")
    private  String   type;//催促类型,
    private  String  types;//催促内容',
    private  String  reason;//客诉内容',
    private  String  isComplaint;//是否是客诉',
    @TableField(exist = false)
    private String createTime;

    public String getCreateTime() {
        if (this.createDate != null) {
            return this.getDate(this.createDate);
        }
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
