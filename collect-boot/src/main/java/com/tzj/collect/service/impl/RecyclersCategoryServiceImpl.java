
/**
* @Title: SbRecyclersCatagoryServiceImpl.java
* @Package com.tzj.collect.service.impl
* @Description: 【】
* @date 2018年3月5日 下午2:05:25
* @version V1.0
* @Company: 上海挺之军科技有限公司
* @Department： 研发部
* @author:[王池][wjc2013481273@163.com]
*/

package com.tzj.collect.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.entity.RecyclersCategory;
import com.tzj.collect.mapper.RecyclersCategoryMapper;
import com.tzj.collect.service.RecyclersCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @ClassName: SbRecyclersCatagoryServiceImpl
* @Description: 【】
* @date 2018年3月5日 下午2:05:25
* @Company: 上海挺之军科技有限公司
* @Department：研发部
* @author:[王池][wjc2013481273@163.com]
*/
@Service
@Transactional(readOnly = true)
public class RecyclersCategoryServiceImpl extends ServiceImpl<RecyclersCategoryMapper,RecyclersCategory> implements RecyclersCategoryService{

}
