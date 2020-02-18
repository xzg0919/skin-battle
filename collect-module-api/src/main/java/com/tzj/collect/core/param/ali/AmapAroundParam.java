package com.tzj.collect.core.param.ali;


import lombok.Data;

import java.io.Serializable;

@Data
public class AmapAroundParam implements Serializable {

    private static final long serialVersionUID = 6981800187882832233L;


    private String location;


    private String searchKey;


    private String offset;


    private String page;
}
