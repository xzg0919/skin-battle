package com.tzj.collect.core.param.sync;

import lombok.Data;

@Data
  public  class IndustryInfo{
        ServiceProductInfo service_product_info;
        ServicePoviderInfo  service_provider_info =new ServicePoviderInfo();
        ServicePerformanceInfo service_performance_info ;
        ServiceStaffInfo service_staff_info =new ServiceStaffInfo();
    }



    @Data
    class ServicePoviderInfo{
    String platform_name  = "易代扔";
    String platform_phone = "400-686-1575";


}

@Data
class ServiceStaffInfo {

    String company_name = "易代扔";

    String phone  = "400-686-1575";
}