package com.skin.controller;

import com.skin.common.utils.SteamLoginUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/30 18:32
 * @Description:
 */

@RestController
@RequestMapping("test")
public class TestController {



    @RequestMapping
    public void test(@RequestParam(name = "openid.ns",required = false) String ns,
                     @RequestParam(name = "openid.mode",required = false) String mode,
                     @RequestParam(name = "openid.op_endpoint",required = false) String endpoint,
                     @RequestParam(name = "openid.claimed_id",required = false) String claimedId,
                     @RequestParam(name = "openid.identity",required = false) String identity,
                     @RequestParam(name = "openid.return_to",required = false) String returnTo,
                     @RequestParam(name = "openid.response_nonce",required = false) String responseNonce,
                     @RequestParam(name = "openid.assoc_handle",required = false) String handle,
                     @RequestParam(name = "openid.signed",required = false) String signed,
                     @RequestParam(name = "openid.sig",required = false) String sig
                     ){
        System.out.println("进来了");
        HashMap<String,String> map =new HashMap<>();
        map.put("openid.ns",ns);
        map.put("openid.mode",mode);
        map.put("openid.op_endpoint",endpoint);
        map.put("openid.claimed_id",claimedId);
        map.put("openid.identity",identity);
        map.put("openid.return_to",returnTo);
        map.put("openid.response_nonce",responseNonce);
        map.put("openid.assoc_handle",handle);
        map.put("openid.signed",signed);
        map.put("openid.sig",sig);

        System.out.println(map+"map--------");
        System.out.println("steamId:"+ SteamLoginUtil.validate(map) );
    }
}
