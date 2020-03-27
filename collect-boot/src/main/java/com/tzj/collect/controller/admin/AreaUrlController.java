package com.tzj.collect.controller.admin;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.tzj.collect.core.service.*;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Community;
import com.tzj.collect.entity.Order;
import com.tzj.collect.entity.RegionCity;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Controller
@RequestMapping("area/url")
public class AreaUrlController {
    @Autowired
    private RegionCityService regionCityService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private OrderService orderService;


    @RequestMapping("/testOrderPush/{id}")
    public Order toXcxIndex(@PathVariable Integer id){
        Order order = orderService.selectById(id);
        asyncService.pushOrder(order);
        return order;
    }
    @RequestMapping("/toXcxIndex")
    public  String toXcxIndex(String outUrl,String areaId, String companyId, String communityId, ModelMap model, String urlParam, String id, String type,String channelId){
        RegionCity regionCity = null;
        if ("Y".equals(outUrl)){
            regionCity = new RegionCity();
            regionCity.setToUrl("http://open.mayishoubei.com/area/url/toXcxIndex?outUrl=Y&type="+type);
            regionCityService.insert(regionCity);
            model.addAttribute("url","https://qr.alipay.com/s6x08110vnu8n4tl0po9w92");
            return "admin/xcxIndex";
        }
        if((StringUtils.isNotBlank(urlParam)&&StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(type))||StringUtils.isNotBlank(urlParam)&&StringUtils.isNotBlank(channelId)){
            String getqRcode = null;
            regionCity = new RegionCity();
            String xcxUri = UtilsController.getXcxUri(urlParam, id, type,channelId);
            try {
                String localPath = System.getProperty("java.io.tmpdir")+ "/local.jpg";
                AreaUrlController.downloadFile(xcxUri, localPath);
                getqRcode = AreaUrlController.getQRcode(localPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isBlank(getqRcode)){
                getqRcode = "https://qr.alipay.com/s6x08110vnu8n4tl0po9w92";
            }
            regionCity.setAreaName(urlParam);
            regionCity.setCityName(id);
            regionCity.setProvinceName(type);
            regionCity.setToUrl(getqRcode);
            model.addAttribute("url",getqRcode);
            System.out.println("参数："+urlParam+"-------"+id+"-------"+type+"  小程序链接："+getqRcode);
            regionCityService.insert(regionCity);
        }else {
            regionCity = new RegionCity();
            regionCity.setCompanyId(Integer.parseInt(companyId));
            String url = "http://open.mayishoubei.com/area/url/toXcxIndex?areaId="+areaId+"&companyId="+companyId;
            Community community = communityService.selectById(communityId);
            Area area = areaService.selectById(areaId);
            regionCity.setAreaId(area.getId().intValue());
            regionCity.setAreaName(area.getAreaName());
            System.out.println("进入城市入口了 : "+area.getAreaName());
            Area city = areaService.selectById(area.getParentId());
            regionCity.setCityId(city.getId().intValue());
            regionCity.setCityName(city.getAreaName());
            Area province = areaService.selectById(city.getParentId());
            if(null!=province){
                regionCity.setProvinceId(province.getId().intValue());
                regionCity.setProvinceName(province.getAreaName());
            }
            if(community!=null){
                url += "&communityId="+communityId;
                regionCity.setCommunityId(community.getId().intValue());
                regionCity.setCommunityName(community.getName());
            }
            regionCity.setToUrl(url);
            regionCityService.insert(regionCity);
            model.addAttribute("url","https://qr.alipay.com/s6x08110vnu8n4tl0po9w92");
        }
        return "admin/xcxIndex";
    }

    public static void main(String[] args) {
        try {
            String localPath = System.getProperty("java.io.tmpdir")+ "/local.jpg";
            System.out.println(localPath);

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
    //解析二维码返回二维码的值
    public static String getQRcode(String fileUrl) throws Exception {
        MultiFormatReader formatReader = new MultiFormatReader();
        File file = new File(fileUrl);
        BufferedImage image = ImageIO.read(file);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        //定义二维码的参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码方式
        Result result = formatReader.decode(binaryBitmap, hints);
       return  result.toString();
    }
    //下载文件到本地
    public static boolean downloadFile(String fileUrl, String fileLocal) throws Exception {
        boolean flag=false;
            URL url = new URL(fileUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                urlCon.setConnectTimeout(6000);
                urlCon.setReadTimeout(6000);
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception("文件读取失败");
                }
            //读文件流
            DataInputStream in = new DataInputStream(urlCon.getInputStream());
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
            }
            try {
                if(out!=null) {
                    out.close();
                }
                if(in!=null) {
                    in.close();
                }
            } catch (Exception e) {
                 e.printStackTrace();
            }
                flag=true;
            return flag;
}

}
