package com.tzj.collect.core.param.xianyu;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class XyCategory {

    private Long id;

    private String name;

    private String questionType;

    private boolean required;

    private List<Map<String,Object>> answers;


}
