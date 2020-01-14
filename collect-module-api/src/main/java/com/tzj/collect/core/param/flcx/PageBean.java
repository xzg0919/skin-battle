package com.tzj.collect.core.param.flcx;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 前台page对象
 * @author Michael_Wang
 *
 */
@Data
public class PageBean implements Serializable {
	//页数
	private Integer pageNumber=1;
	//页面大小
	private Integer pageSize=10;
}
