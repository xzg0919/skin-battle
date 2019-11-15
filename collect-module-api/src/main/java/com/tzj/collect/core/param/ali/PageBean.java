package com.tzj.collect.core.param.ali;

import org.springframework.web.multipart.MultipartFile;

/**
 * 前台page对象
 * @author Michael_Wang
 *
 */
public class PageBean {
	private Integer memberId;//暂用
	//页数
	private Integer pageNumber=1;
	//页面大小
	private Integer pageSize=10;

	private MultipartFile headImg;

	private String  imgFile;

	private String side;
	private String fileName;
	private String fileContentBase64;

	public String getImgFile() {
		return imgFile;
	}

	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public MultipartFile getHeadImg() {
		return headImg;
	}
	public void setHeadImg(MultipartFile headImg) {
		this.headImg = headImg;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileContentBase64() {
		return fileContentBase64;
	}

	public void setFileContentBase64(String fileContentBase64) {
		this.fileContentBase64 = fileContentBase64;
	}
}
