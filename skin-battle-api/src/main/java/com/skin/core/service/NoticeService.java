package com.skin.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.skin.entity.Notice;

public interface NoticeService extends IService<Notice> {

    Page<Notice> getPage(Integer pageNo, Integer pageSize);
}
