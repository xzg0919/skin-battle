package com.tzj.collect.core.service.impl;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tzj.collect.core.mapper.YhTestMapper;
import com.tzj.collect.core.service.YhTestService;
import com.tzj.collect.entity.YhTest;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class YhTestServiceImpl extends ServiceImpl<YhTestMapper, YhTest> implements YhTestService
{
    @Resource
    private YhTestMapper YhTestMapper;
    /**
     * <p>Created on 2019年6月6日</p>
     * <p>Description:[获取所有表]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    public List<YhTest> getAllTables()
    {
        return YhTestMapper.getAllTables();
    }
    /**
     * <p>Created on 2019年6月6日</p>
     * <p>Description:[生成代码]</p>
     * @author:[杨欢] [yanghuan1937@aliyun.com]
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return  
     */
    @Override
    public String makeCode(String tableInfo,String auth)
    {
        String msg = null;
        String tableComment = tableInfo.split("----")[0];
        String tableName = tableInfo.split("----")[1];
        String entityName = getEntityName(tableName);
        msg = checkExist(entityName);
        String makeDate = getMakeDate();
        if(null != msg)
        {
            //return msg;
        }
        String filePath = YhTestServiceImpl.class.getResource("").toString();
        filePath = filePath.replaceAll("file:/","");
        filePath = filePath.substring(0,filePath.indexOf("/target"));
        filePath = filePath.concat("/src/main/java/com/tzj/collect/common/auto/tmp/"); 
        filePath = "d:/sbCode/";
        File file = new File(filePath);
        if(!file.isDirectory())
        {
            file.mkdirs();
        }
        MakeCode.makeService(entityName, tableComment, filePath,makeDate,auth);
        MakeCode.makeServiceImpl(entityName, tableComment, filePath,makeDate,auth);
        MakeCode.makeMapper(entityName, tableComment, filePath,makeDate,auth);
        MakeCode.makeMapperXML(entityName,filePath);
        List<YhTest> columnList = YhTestMapper.getAllColumns(tableName);
        MakeCode.makeEntity(entityName, tableComment, filePath,makeDate,auth,tableName,columnList);
        return "ok";
    }
    /**
     * <p>Created on 2019年6月7日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    private String getMakeDate()
    {
        Calendar calendar = Calendar.getInstance(); //获取当前的系统时间。
        return calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1) + "月" + calendar.get(Calendar.DATE) + "日";
    }
    /**
     * <p>Created on 2019年6月7日</p>
     * <p>Description:[校验存在]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    private String checkExist(String entityName)
    {
        String rootPath = YhTestServiceImpl.class.getResource("").toString();
        rootPath = rootPath.replaceAll("file:/","");
        rootPath = rootPath.substring(0,rootPath.indexOf("/target"));
        rootPath = rootPath.concat("/src/main/java/com/tzj/collect/entity");
        rootPath = rootPath.concat("/");
        rootPath = rootPath.concat(entityName);
        rootPath = rootPath.concat(".java");
        File file = new File(rootPath);
        if(file.exists() && file.isFile())
        {
            return "已生成过此表，全覆盖啊？？？";
        }
        return null;
    }
    /**
     * <p>Created on 2019年6月7日</p>
     * <p>Description:[获取实体类名称]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    private String getEntityName(String tableName)
    {
        StringBuffer entityName = new StringBuffer(); 
        tableName = tableName.replaceAll("sb_", "");
        String tableParts[] = tableName.split("_");
        String tablePart = null;
        for(int i=0,j=tableParts.length;i<j;i++)
        {
            tablePart = tableParts[i];
            entityName.append(tablePart.substring(0, 1).toUpperCase());
            entityName.append(tablePart.substring(1));
        }
        return entityName.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
