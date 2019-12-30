package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 线上线下分享码
 * @author: sgmark@aliyun.com
 * @Date: 2019/12/19 0019
 * @Param: 
 * @return: 
 */
@TableName("sb_line_qr_code")
@Data
public class LineQrCode extends DataEntity<Long>
{
    /**
     *  id
     */
    private Long id;
    /**
     * url的code
     */
    private String shareCode;//唯一code
    /**
     * url的code
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 所属公司
     */
    private Long companyId;

    private String qrUrl;//分享码链接

    private QrType qrType;//码类型：线下线上

    private String qrCodeInfo;//码详情

    private Integer shareNum;//扫码进入次数

    public enum QrType implements IEnum {
        ONLINE(0),	//  线上码
        OFFLINE(1); //线下码

        public Integer value;

        QrType(final Integer value) {
            this.value = value;
        }

        @Override
        public Serializable getValue() {
            return this.value;
        }
    }

    public String getCreateDate4Page() {
        if (null != super.getCreateDate()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(super.getCreateDate());
        }else {
            return "";
        }
    }
}
