package com.tzj.collect.core.param.iot;

import com.tzj.collect.core.param.ali.PageBean;
import lombok.Data;

/**
 * @author sgmark
 * @create 2019-10-16 17:48
 **/
@Data
public class AdminIotErrorBean {

    private String linkName;

    private String mobile;

    private String startTime;

    private String endTime;

    private String equipmentCode;

    private PageBean pageBean;
}
