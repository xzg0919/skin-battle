package com.tzj.collect.core.param.ali;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class VoucherBean {

    private String voucherType;//1待使用  2已使用  3已过期

    private String orderType;//bigFurniture大件 appliance家电  rubbish生活垃圾


}
