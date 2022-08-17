package com.skin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skin.dto.UserPage;
import com.skin.entity.UserPackage;

import java.util.List;

public interface UserPackageService  extends IService<UserPackage> {


    List<UserPackage> getLastPageList();
}
