//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tzj.module.common.file.upload;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.tzj.module.common.file.FileInfo;
import com.tzj.module.common.file.FileLists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OssFileUpload extends FileUpload {
    private String accessId;
    private String accessKey;
    private String bucketName;
    private String ossEndPointer;

    public OssFileUpload(String ossEndPointer, String accessId, String accessKey, String bucketName) {
        this.setOssEndPointer(ossEndPointer);
        this.setAccessId(accessId);
        this.setAccessKey(accessKey);
        this.setBucketName(bucketName);
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

    public void upload(String path, File file, String contentType) {
        FileInputStream inputStream = null;

        OSSClient ossClient = null;
        try {
            inputStream = new FileInputStream(file);
            ossClient = new OSSClient(this.ossEndPointer, this.accessId, this.accessKey);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.length());
            ossClient.putObject(this.bucketName, StringUtils.removeStart(path, "/"), inputStream, objectMetadata);
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            ossClient.shutdown();
        }

    }

    public FileLists browser(String path) {
        FileLists fileLists = new FileLists();
        List<FileInfo> fileInfos = new ArrayList();
        String nextMarker = null;
        boolean var5 = true;

        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(this.ossEndPointer, this.accessId, this.accessKey);

            ObjectListing objectListing;
            do {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucketName);
                listObjectsRequest.setPrefix(StringUtils.removeStart(path, "/"));
                listObjectsRequest.setMaxKeys(1000);
                listObjectsRequest.setMarker(nextMarker);
                objectListing = ossClient.listObjects(listObjectsRequest);
                List<FileInfo> _fileInfos = this.transOssObj(objectListing);
                fileInfos.addAll(_fileInfos);
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
        } catch (Exception var10) {
            var10.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        fileLists.setItems(fileInfos);
        return fileLists;
    }

    private List<FileInfo> transOssObj(ObjectListing objectListing) {
        List<FileInfo> fileInfos = new ArrayList();
        Iterator var3 = objectListing.getCommonPrefixes().iterator();

        FileInfo fileInfo;
        while(var3.hasNext()) {
            String commonPrefix = (String)var3.next();
            fileInfo = new FileInfo();
            fileInfo.setName(StringUtils.substringAfterLast(StringUtils.removeEnd(commonPrefix, "/"), "/"));
            fileInfo.setUrl("/" + commonPrefix);
            fileInfo.setIsDirectory(true);
            fileInfo.setSize(0L);
            fileInfos.add(fileInfo);
        }

        var3 = objectListing.getObjectSummaries().iterator();

        while(var3.hasNext()) {
            OSSObjectSummary ossObjectSummary = (OSSObjectSummary)var3.next();
            if (!ossObjectSummary.getKey().endsWith("/")) {
                fileInfo = new FileInfo();
                fileInfo.setName(StringUtils.substringAfterLast(ossObjectSummary.getKey(), "/"));
                fileInfo.setUrl("/" + ossObjectSummary.getKey());
                fileInfo.setIsDirectory(false);
                fileInfo.setSize(ossObjectSummary.getSize());
                fileInfo.setLastModified(ossObjectSummary.getLastModified());
                fileInfos.add(fileInfo);
            }
        }

        return fileInfos;
    }

    public FileLists browser(String path, String marker, Integer maxItems) {
        FileLists fileLists = new FileLists();
        new ArrayList();

        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(this.ossEndPointer, this.accessId, this.accessKey);
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(this.bucketName);
            if (StringUtils.isNoneBlank(new CharSequence[]{path})) {
                listObjectsRequest.setPrefix(path);
            }

            if (StringUtils.isNoneBlank(new CharSequence[]{marker})) {
                listObjectsRequest.setMarker(marker);
            }

            if (maxItems != null) {
                listObjectsRequest.setMaxKeys(maxItems);
            }

            ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
            List<FileInfo> fileInfos = this.transOssObj(objectListing);
            String newxNextMarker = objectListing.getNextMarker();
            fileLists.setMarker(newxNextMarker);
            fileLists.setItems(fileInfos);
            return fileLists;
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
    }
}
