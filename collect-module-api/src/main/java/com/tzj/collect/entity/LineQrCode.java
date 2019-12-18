package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;

import java.io.Serializable;

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
    private String code;
    /**
     * url的code
     */
    @TableField(value = "name_")
    private String name;
    /**
     * 所属公司
     */
    private String companyId;

    private QrType qrType;//码类型：线下线上

    private String qrCodeInfo;//码详情

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
}
