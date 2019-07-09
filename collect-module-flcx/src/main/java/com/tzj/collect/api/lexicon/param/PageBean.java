package com.tzj.collect.api.lexicon.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 前台page对象
 * @author Michael_Wang
 *
 */
@Data
public class PageBean {
	//页数
	private Integer pageNumber=1;
	//页面大小
	private Integer pageSize=10;
}
