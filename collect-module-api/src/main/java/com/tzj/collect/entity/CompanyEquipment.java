package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

/**
 * 公司所对应设备
 *
 * @author sgmark
 * @create 2019-04-02 14:30
 **/
@TableName("sb_company_equipment")
@Data
public class CompanyEquipment extends  DataEntity<Long> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long companyId;//企业Id

    private String equipmentCode;//设备编号（共有的）

    private String hardwareCode;//app硬件编号(作为mqtt的子topic)

    private Double longitude; // 经度

    private Double latitude; // 纬度
    /**
     * 区域id
     */
    private Integer areaId;
    /**
     * 街道id
     */
    private Integer streetId;
    /**
     * 小区id
     */
    private Integer communityId;

    private String address;
    @TableField(value = "status_")
    private Integer status;//当前状态(满仓与否)

    private String  workHours;//设备工作时间

    private String  workWeek;//设备工作周
    @TableField(exist = false)
    private String  workWeeks;//设备工作周

    private Date enablingTime;//设备启用时间

    private  String isActivated;//是否已激活的（默认未激活0）

    private String activateTel;//激活手机号

    private String captcha;//开箱码（使用过后变更）

    private double setValue;//满溢设定值

    private double currentValue;//满溢当前值

    private String equipmentFrom;//设备来源  0自己的   1奥图的

    private Integer isClose;//柜门是否打开（0关闭 1打开）
    @TableField(exist = false)
    private Integer distance;//距离
    /**
     * 设备动作指令
     * @author: sgmark@aliyun.com
     * @Date: 2019/11/15 0015
     * @Param: 
     * @return: 
     */
    public static class EquipmentAction{
        public enum  EquipmentActionCode implements IEnum {
            UPLOAD_ORDER("10000", "上传订单数据"),
            UPLOAD_SUCCESS("11111", "数据上传成功"),
            EQUIPMENT_OPEN("20000", "打开箱门"),   	 //打开箱门
            EQUIPMENT_CLOSE("20001", "关闭箱门"),   	 //关闭箱门
            IDENTIFYING_PICTURES("20002", "识别图片"),
            RECYCLE_OPEN("30001", "清运开门"),   	 //清运开门
            RECYCLE_CLOSE("30002", "清运关门"),       //清运关门
            UPLOAD_STATUS("40001", "上传设备满溢状态"),
            DISCERN_FINISH("50001", "识别完成"),
            DISCERN_ERROR("50002", "识别错误"),
            CHANGE_ADVENT("60001", "更换广告"),
            FLIP_BAFFLE("70001", "翻转挡板"),
            TRADE_SUCCESS("80001", "交易完成"),
            CHANGE_QRCODE("90001", "更换二维码"),
            UPLOAD_LOCATION("100000", "上传定位"),
            ERROR("00000", "错误");
            private String key;

            private String value;

            EquipmentActionCode(final String key, final String value) {
                this.key = key;
                this.value = value;
            }

            public String getKey() {
                return key;
            }

            @Override
            public String getValue() {
                return value;
            }

            public static String getValueByKey(String key) {
                EquipmentActionCode[] enums = EquipmentActionCode.values();
                for (int i = 0; i < enums.length; i++) {
                    if (enums[i].getKey().equals(key)) {
                        return enums[i].getValue();
                    }
                }
                return "";
            }
        }
    }

    public String getWorkWeeks() {
        if (StringUtils.isBlank(workWeek)){
            return workWeeks;
        }
        final String[] s = {""};
        String[] split = workWeek.split(",");
        Arrays.stream(split).forEach(week -> {
            switch (week){
                case "1": s[0] += "星期一/";
                    break;
                case "2": s[0] += "星期二/";
                    break;
                case "3": s[0] += "星期三/";
                    break;
                case "4": s[0] += "星期四/";
                    break;
                case "5": s[0] += "星期无/";
                    break;
                case "6": s[0] += "星期六/";
                    break;
                case "7": s[0] += "星期天";
                    break;
            }
        });
        return s[0];
    }

    public void setWorkWeeks(String workWeeks) {
        this.workWeeks = workWeeks;
    }
}
