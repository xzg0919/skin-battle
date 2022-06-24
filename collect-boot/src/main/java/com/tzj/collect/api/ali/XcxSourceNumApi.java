package com.tzj.collect.api.ali;


import com.tzj.collect.common.qrcode.QRBarCodeUtil;
import com.tzj.collect.core.param.ali.XcxSourceNumBean;
import com.tzj.collect.core.service.ShareInfoService;
import com.tzj.collect.core.service.SharerService;
import com.tzj.collect.core.service.XcxSourceNumService;
import com.tzj.collect.entity.Sharer;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.AuthIgnore;
import com.tzj.module.api.annotation.SignIgnore;
import com.tzj.module.easyopen.file.FileBase64Param;
import com.tzj.module.easyopen.file.FileBean;
import com.tzj.module.easyopen.file.FileUploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@ApiService
public class XcxSourceNumApi {

    @Autowired
    private XcxSourceNumService xcxSourceNumService;

    @Autowired
    ShareInfoService shareInfoService;

    @Autowired
    SharerService sharerService;
    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 返回小程序首页标题列表
     *
     * @param
     * @author 王灿
     */
    @Api(name = "xcx.saveXcxSourceNum", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object saveXcxSourceNum(XcxSourceNumBean xcxSourceNumBean) {

        return xcxSourceNumService.saveXcxSourceNum(xcxSourceNumBean.getCode(), xcxSourceNumBean.getAliUserId());

    }


   /* @Api(name = "xcx.share", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object share(XcxSourceNumBean xcxSourceNumBean) {
        shareInfoService.share(xcxSourceNumBean.getSharerId(), xcxSourceNumBean.getAliUserId());
        return "success";
    }*/

    @Api(name = "xcx.share.info", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object shareInfo(XcxSourceNumBean xcxSourceNumBean) {
        return shareInfoService.getShareData(xcxSourceNumBean.getAliUserId(), xcxSourceNumBean.getDate());
    }


    @Api(name = "xcx.sharer.info", version = "1.0")
    @SignIgnore
    @AuthIgnore
    public Object sharerInfo(XcxSourceNumBean xcxSourceNumBean) throws UnsupportedEncodingException {
        Sharer byAliUserId = sharerService.getByAliUserId(xcxSourceNumBean.getAliUserId());
        if (byAliUserId != null && StringUtils.isBlank(byAliUserId.getQrCode())) {
            BufferedImage bufferedImage = QRBarCodeUtil.createCodeToBufferedImage(
                    "alipays://platformapi/startapp?appId=2018060660292753&query="+URLEncoder.encode("sharerId="+xcxSourceNumBean.getAliUserId(),"UTF-8")+"&page=pages/view/index/index");
            String base64 = QRBarCodeUtil.getBufferedImageToBase64(bufferedImage, "jpg");
            List<FileBase64Param> list = new ArrayList<>();
            FileBase64Param fileBase64Param = new FileBase64Param();
            fileBase64Param.setFileContentBase64(base64);
            fileBase64Param.setFileName(xcxSourceNumBean.getAliUserId() + ".jpg");
            list.add(fileBase64Param);
            List<FileBean> fileBeans = fileUploadService.uploadImage(list);
            if (fileBeans != null && fileBeans.size() != 0) {
                byAliUserId.setQrCode(fileBeans.get(0).getOriginal());
                sharerService.updateById(byAliUserId);
            }
        }
        return byAliUserId;

    }
}
