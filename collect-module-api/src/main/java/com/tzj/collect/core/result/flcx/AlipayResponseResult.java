package com.tzj.collect.core.result.flcx;

import com.alipay.api.domain.KeyWordDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sgmark
 * @create 2020-01-09 13:12
 **/
@Data
public class AlipayResponseResult implements Serializable {

     private List<KeyWordDTO> keyWords;

     private String traceId;

     private Boolean isSuccess;
}
