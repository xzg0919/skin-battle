package com.skin.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.Admin;


public interface AdminService extends IService<Admin> {


	Object getToken(String userName,String password);
}
