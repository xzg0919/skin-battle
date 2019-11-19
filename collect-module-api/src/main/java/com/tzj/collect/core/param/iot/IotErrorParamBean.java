package com.tzj.collect.core.param.iot;

import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.Member;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author sgmark
 * @create 2019-03-30 14:31
 **/
@Data
public class IotErrorParamBean {

    private Member member;//会员

    private String equipmentCode;//设备编号

    private List<String> errorCodeList; //错误编号

    private List<String> errorMessageList;//错误信息

    private String otherMessage;//其他错误信息

}
