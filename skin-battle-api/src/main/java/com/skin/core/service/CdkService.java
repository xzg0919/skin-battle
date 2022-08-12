package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.CdkInfo;

import java.math.BigDecimal;


public interface CdkService extends IService<CdkInfo> {


	Page<CdkInfo> getCdkPage(Integer pageNum, Integer pageSize);


	void insertCdk(BigDecimal price);


	void updateCdk(Long id ,BigDecimal price);

	void changeStatus (Long id );


	void deleteCdk(Long id );
}
