//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tzj.module.common.file.upload;

import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

public class FileUploadFactoryBean implements FactoryBean<FileUpload>, InitializingBean, DisposableBean, ServletContextAware {
    private FileUpload fileUpload;
    private ServletContext servletContext;
    private String uploadType;
    private boolean useRealPath = false;
    private String accessId;
    private String accessKey;
    private String bucketName;
    private String ossEndPointer;
    private String imageDomain;

    public void setImageDomain(String imageDomain) {
        this.imageDomain = imageDomain;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public void setUseRealPath(boolean useRealPath) {
        this.useRealPath = useRealPath;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setOssEndPointer(String ossEndPointer) {
        this.ossEndPointer = ossEndPointer;
    }

    public FileUploadFactoryBean() {
    }

    public void destroy() throws Exception {
    }

    public FileUpload getObject() throws Exception {
        return this.fileUpload;
    }

    public Class<?> getObjectType() {
        return this.fileUpload != null ? this.fileUpload.getClass() : FileUpload.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.uploadType, "Property 'uploadType' is required");
        if (StringUtils.isNotEmpty(this.uploadType) && this.uploadType.equals("oss")) {
            Assert.notNull(this.ossEndPointer, "Property 'ossEndPointerossEndPointerossEndPointer' is required");
            Assert.notNull(this.accessId, "Property 'accessId' is required");
            Assert.notNull(this.accessKey, "Property 'accessKey' is required");
            Assert.notNull(this.bucketName, "Property 'bucketName' is required");
            this.fileUpload = new OssFileUpload(this.ossEndPointer, this.accessId, this.accessKey, this.bucketName);
        } else if (StringUtils.isNotEmpty(this.uploadType) && this.uploadType.equals("local")) {
            this.fileUpload = new LocalFileUpload(this.useRealPath, this.servletContext);
        } else {
            this.fileUpload = new LocalFileUpload(this.useRealPath, this.servletContext);
        }

        this.fileUpload.setImageDomain(this.imageDomain);
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
