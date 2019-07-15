package com.tzj.collect.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.tzj.collect.core.service.SNService;
import com.tzj.collect.entity.SN;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author 胡方明（12795880@qq.com）
 *
 * 这是一个例子
 *
 * 测试方法
 *
 * http://localhost:8080/ali/api  POST 下面的json
 *
 * {
      "name":"sn.list",
      "version":"1.0",
      "format":"json"
   }
 **/
@ApiService
public class SNApi {

    @Autowired
    SNService snService;

    
    @Api(name = "sn.list", version = "1.0")
    //@AuthIgnore //这个api忽略token验证
    @SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
    public List<SN> listSNs() {
        return snService.selectPage(new Page<SN>(0, 12)).getRecords();
    }
    @Api(name = "sn.update.test", version = "1.0")
    @AuthIgnore //这个api忽略token验证
    @SignIgnore //这个api忽略sign验证以及随机数以及时间戳验证
    public Integer listUpdateTest() {
        SN sn = snService.selectById(4);
        return snService.updateTest(sn);
    }

}
