package com.tzj.collect.core.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tzj.collect.entity.YhTest;

/**
 * <p>Created on 2019年6月7日</p>
 * <p>Title:       [收呗绿账]_[]_[]</p>
 * <p>Description: []</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
*/
public class MakeCode
{
    public static String makeService(String entityName, String tableComment, String filePath, String makeDate,
            String auth)
    {
        StringBuffer sb = new StringBuffer();
        File serviceFile = null;
        FileWriter fileWritter = null;
        try
        {
            sb.append("package com.tzj.collect.core.service;");
            sb.append("\n");
            sb.append("\n");
            sb.append("import com.baomidou.mybatisplus.service.IService");
            sb.append("\n");
            sb.append("import com.tzj.collect.entity.");
            sb.append(entityName);
            sb.append(";");
            sb.append("\n");
            sb.append("\n");
            sb.append("/**");
            sb.append("\n");
            sb.append(" *");
            sb.append("\n");
            sb.append(" * <p>Created on");
            sb.append(makeDate);
            sb.append("</p>");
            sb.append("\n");
            sb.append(" * <p>Title:       [收呗绿账]_[]_[]</p>");
            sb.append("\n");
            sb.append(" * <p>Description: [");
            sb.append(tableComment);
            sb.append("service");
            sb.append("]</p>");
            sb.append("\n");
            sb.append(" * <p>Copyright:   Copyright (c) 2017</p>");
            sb.append("\n");
            sb.append(" * <p>Company:     上海挺之军信息科技有限公司 </p>");
            sb.append("\n");
            sb.append(" * <p>Department:  研发部</p>");
            sb.append("\n");
            sb.append(" * @author         [");
            sb.append(auth);
            sb.append("]");
            sb.append("\n");
            sb.append(" * @version        1.0");
            sb.append("\n");
            sb.append(" */");
            sb.append("\n");
            sb.append("public interface ");
            sb.append(entityName);
            sb.append("Service extends IService<");
            sb.append(entityName);
            sb.append(">");
            sb.append("\n");
            sb.append("{");
            sb.append("\n");
            sb.append("\n");
            sb.append("}");
            serviceFile = new File(filePath.concat(entityName).concat("Service.java"));
            if (!serviceFile.exists())
            {
                serviceFile.createNewFile();
            }
            fileWritter = new FileWriter(serviceFile);
            fileWritter.write(sb.toString());
            fileWritter.flush();
            fileWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Created on 2019年6月7日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    public static String makeServiceImpl(String entityName, String tableComment, String filePath, String makeDate,
            String auth)
    {
        StringBuffer sb = new StringBuffer();
        File serviceImplFile = null;
        FileWriter fileWritter = null;
        try
        {
            sb.append("package com.tzj.collect.core.service.impl;");
            sb.append("\n");
            sb.append("\n");
            sb.append("import com.baomidou.mybatisplus.service.impl.ServiceImpl;");
            sb.append("\n");
            sb.append("import javax.annotation.Resource;");
            sb.append("\n");
            sb.append("import com.tzj.collect.entity.");
            sb.append(entityName);
            sb.append(";");
            sb.append("\n");
            sb.append("import com.tzj.collect.core.service.");
            sb.append(entityName);
            sb.append("Service;");
            sb.append("\n");
            sb.append("import org.springframework.stereotype.Service;");
            sb.append("\n");
            sb.append("import org.springframework.transaction.annotation.Transactional;");
            sb.append("\n");
            sb.append("import com.tzj.collect.core.mapper.");
            sb.append(entityName);
            sb.append("Mapper;");
            sb.append("\n");
            
            
            sb.append("/**");
            sb.append("\n");
            sb.append(" *");
            sb.append("\n");
            sb.append(" * <p>Created on");
            sb.append(makeDate);
            sb.append("</p>");
            sb.append("\n");
            sb.append(" * <p>Title:       [收呗绿账]_[]_[]</p>");
            sb.append("\n");
            sb.append(" * <p>Description: [");
            sb.append(tableComment);
            sb.append("service实现类");
            sb.append("]</p>");
            sb.append("\n");
            sb.append(" * <p>Copyright:   Copyright (c) 2017</p>");
            sb.append("\n");
            sb.append(" * <p>Company:     上海挺之军信息科技有限公司 </p>");
            sb.append("\n");
            sb.append(" * <p>Department:  研发部</p>");
            sb.append("\n");
            sb.append(" * @author         [");
            sb.append(auth);
            sb.append("]");
            sb.append("\n");
            sb.append(" * @version        1.0");
            sb.append("\n");
            sb.append(" */");
            sb.append("\n");

            sb.append("@Service");
            sb.append("\n");
            sb.append("@Transactional(readOnly = true, rollbackFor = Exception.class)");
            sb.append("\n");
            sb.append("public class ");
            sb.append(entityName);
            sb.append("ServiceImpl extends ServiceImpl<");
            sb.append(entityName);
            sb.append("Mapper, ");
            sb.append(entityName);
            sb.append("> implements ");
            sb.append(entityName);
            sb.append("Service");
            sb.append("\n");
            sb.append("{");
            
            sb.append("\n");
            sb.append("    @Resource");
            sb.append("\n");
            sb.append("    private ");
            sb.append(entityName.substring(0, 1).toUpperCase().concat(entityName.substring(1)));
            sb.append("Mapper ");
            sb.append(entityName);
            sb.append("Mapper;");
            sb.append("\n");
            sb.append("\n");
            sb.append("}");

            serviceImplFile = new File(filePath.concat(entityName).concat("ServiceImpl.java"));
            if (!serviceImplFile.exists())
            {
                serviceImplFile.createNewFile();
            }
            fileWritter = new FileWriter(serviceImplFile);
            fileWritter.write(sb.toString());
            fileWritter.flush();
            fileWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Created on 2019年6月7日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    public static String makeMapper(String entityName, String tableComment, String filePath, String makeDate,
            String auth)
    {
        StringBuffer sb = new StringBuffer();
        File serviceImplFile = null;
        FileWriter fileWritter = null;
        try
        {
            sb.append("package com.tzj.collect.mapper");
            sb.append("\n");
            sb.append("\n");
            sb.append("import com.baomidou.mybatisplus.mapper.BaseMapper;");
            sb.append("\n");
            sb.append("import com.tzj.collect.entity.");
            sb.append(entityName);
            sb.append(";");
            sb.append("\n");

            sb.append("/**");
            sb.append("\n");
            sb.append(" *");
            sb.append("\n");
            sb.append(" * <p>Created on");
            sb.append(makeDate);
            sb.append("</p>");
            sb.append("\n");
            sb.append(" * <p>Title:       [收呗绿账]_[]_[]</p>");
            sb.append("\n");
            sb.append(" * <p>Description: [");
            sb.append(tableComment);
            sb.append("映射类");
            sb.append("]</p>");
            sb.append("\n");
            sb.append(" * <p>Copyright:   Copyright (c) 2017</p>");
            sb.append("\n");
            sb.append(" * <p>Company:     上海挺之军信息科技有限公司 </p>");
            sb.append("\n");
            sb.append(" * <p>Department:  研发部</p>");
            sb.append("\n");
            sb.append(" * @author         [");
            sb.append(auth);
            sb.append("]");
            sb.append("\n");
            sb.append(" * @version        1.0");
            sb.append("\n");
            sb.append(" */");
            sb.append("\n");

            sb.append("public interface ");
            sb.append(entityName.substring(0, 1).toUpperCase().concat(entityName.substring(1)));
            sb.append("Mapper extends BaseMapper<");
            sb.append(entityName);
            sb.append(">");
            sb.append("\n");
            sb.append("{");
            sb.append("\n");
            sb.append("\n");
            sb.append("}");

            serviceImplFile = new File(filePath.concat(entityName).concat("Mapper.java"));
            if (!serviceImplFile.exists())
            {
                serviceImplFile.createNewFile();
            }
            fileWritter = new FileWriter(serviceImplFile);
            fileWritter.write(sb.toString());
            fileWritter.flush();
            fileWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * <p>Created on 2019年6月7日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    public static String makeMapperXML(String entityName, String filePath)
    {
        StringBuffer sb = new StringBuffer();
        File mapperXMLFile = null;
        FileWriter fileWritter = null;
        try
        {
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            sb.append("\n");
            sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            sb.append(" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >");
            sb.append("\n");
            sb.append("<mapper namespace=\"");
            sb.append("com.tzj.collect.core.mapper.");
            sb.append(entityName);
            sb.append("Mapper\">");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("</mapper>");
            mapperXMLFile = new File(filePath.concat(entityName).concat("Mapper.xml"));
            if (!mapperXMLFile.exists())
            {
                mapperXMLFile.createNewFile();
            }
            fileWritter = new FileWriter(mapperXMLFile);
            fileWritter.write(sb.toString());
            fileWritter.flush();
            fileWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Created on 2019年6月7日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return void
     */
    public static String makeEntity(String entityName, String tableComment, String filePath, String makeDate,
            String auth,String tableName,List<YhTest> columnList)
    {
        StringBuffer sb = new StringBuffer();
        StringBuffer columnSb = new StringBuffer();
        StringBuffer getSetSb = new StringBuffer();
        List<String> commonColumnList = getCommonColumnList();
        File entityFile = null;
        FileWriter fileWritter = null;
        String dateType = null;
        String fieldName = null;
        try
        {
            sb.append("package com.tzj.collect.entity;");
            sb.append("\n");
            sb.append("\n");
            sb.append("import com.baomidou.mybatisplus.annotation.TableName;");
            sb.append("\n");
            sb.append("import com.baomidou.mybatisplus.enums.IdType;");
            sb.append("\n");
            sb.append("import com.baomidou.mybatisplus.annotation.TableId;");
            sb.append("\n");
            sb.append("/**");
            sb.append("\n");
            sb.append(" *");
            sb.append("\n");
            sb.append(" * <p>Created on");
            sb.append(makeDate);
            sb.append("</p>");
            sb.append("\n");
            sb.append(" * <p>Title:       [收呗绿账]_[]_[]</p>");
            sb.append("\n");
            sb.append(" * <p>Description: [");
            sb.append(tableComment);
            sb.append("实体类");
            sb.append("]</p>");
            sb.append("\n");
            sb.append(" * <p>Copyright:   Copyright (c) 2017</p>");
            sb.append("\n");
            sb.append(" * <p>Company:     上海挺之军信息科技有限公司 </p>");
            sb.append("\n");
            sb.append(" * <p>Department:  研发部</p>");
            sb.append("\n");
            sb.append(" * @author         [");
            sb.append(auth);
            sb.append("]");
            sb.append("\n");
            sb.append(" * @version        1.0");
            sb.append("\n");
            sb.append(" */");
            sb.append("\n");
            
            sb.append("@TableName(\"");
            sb.append(tableName);
            sb.append("\")");
            sb.append("\n");
            sb.append("public class ");
            sb.append(entityName);
            sb.append(" extends DataEntity<Long>");
            sb.append("\n");
            
            sb.append("{");
            sb.append("\n");
            columnSb.append("    /**");
            columnSb.append("\n");
            columnSb.append("     * 主键");
            columnSb.append("\n");
            columnSb.append("     */");
            columnSb.append("\n");
            columnSb.append("    @TableId(value = \"id\", type = IdType.AUTO)");
            columnSb.append("\n");
            columnSb.append("    private Long id;");
            columnSb.append("\n");
            
            getSetSb.append("\n");
            getSetSb.append("\n");
            getSetSb.append("    /**");
            getSetSb.append("\n");
            getSetSb.append("     * <p>Description:[获取id]</p>");
            getSetSb.append("\n");
            getSetSb.append("     * @return String id");
            getSetSb.append("\n");
            getSetSb.append("     */");
            getSetSb.append("\n");
            getSetSb.append("    @Override");
            getSetSb.append("\n");
            getSetSb.append("    public Long getId()");
            getSetSb.append("\n");
            getSetSb.append("    {");
            getSetSb.append("\n");
            getSetSb.append("        return id;");
            getSetSb.append("\n");
            getSetSb.append("    }");
            getSetSb.append("    /**");
            getSetSb.append("\n");
            getSetSb.append("     * <p>Description:[设置id]</p>");
            getSetSb.append("\n");
            getSetSb.append("     * @param String id ");
            getSetSb.append("\n");
            getSetSb.append("     */");
            getSetSb.append("\n");
            getSetSb.append("    @Override");
            getSetSb.append("\n");
            getSetSb.append("    public void setId(Long id)");
            getSetSb.append("\n");
            getSetSb.append("    {");
            getSetSb.append("\n");
            getSetSb.append("        this.id=id;");
            getSetSb.append("\n");
            getSetSb.append("    }");
            getSetSb.append("\n");
            
            
            
            if(null != columnList && !columnList.isEmpty())
            {
                for(YhTest yhTest : columnList)
                {
                    if(!commonColumnList.contains(yhTest.getColumnName()))
                    {
                        dateType = getDateType(yhTest.getDataType());
                        fieldName = getFieldName(yhTest.getColumnName(),"0");
                        columnSb.append("    /**");
                        columnSb.append("\n");
                        columnSb.append("     * ");
                        columnSb.append(yhTest.getColumnComment());
                        columnSb.append("\n");
                        columnSb.append("     */"); 
                        columnSb.append("\n");
                        columnSb.append("    private ");
                        columnSb.append(dateType);
                        columnSb.append(" ");
                        columnSb.append(fieldName);
                        columnSb.append(";");
                        columnSb.append("\n");
                        
                        
                        getSetSb.append("\n");
                        getSetSb.append("    /**");
                        getSetSb.append("\n");
                        getSetSb.append("     * <p>Description:[获取");
                        getSetSb.append(yhTest.getColumnComment());
                        getSetSb.append("]</p>");
                        getSetSb.append("\n");
                        getSetSb.append("     * @return ");
                        getSetSb.append(dateType);
                        getSetSb.append(" ");
                        getSetSb.append(fieldName);
                        getSetSb.append("\n");
                        getSetSb.append("     */");
                        getSetSb.append("\n");
                        getSetSb.append("    public ");
                        getSetSb.append(dateType);
                        getSetSb.append(" get");
                        getSetSb.append(getFieldName(yhTest.getColumnName(),"1"));
                        getSetSb.append("()");
                        getSetSb.append("\n");
                        getSetSb.append("    {");
                        getSetSb.append("\n");
                        getSetSb.append("        return ");
                        getSetSb.append(fieldName);
                        getSetSb.append(";");
                        getSetSb.append("\n");
                        getSetSb.append("    }");
                        getSetSb.append("\n");
                        getSetSb.append("    /**");
                        getSetSb.append("\n");
                        getSetSb.append("     * <p>Description:[设置");
                        getSetSb.append(yhTest.getColumnComment());
                        getSetSb.append("]</p>");
                        getSetSb.append("\n");
                        getSetSb.append("     * @param ");
                        getSetSb.append(dateType);
                        getSetSb.append(" ");
                        getSetSb.append(fieldName);
                        getSetSb.append("\n");
                        getSetSb.append("     */");
                        getSetSb.append("\n");
                        getSetSb.append("\n");
                        getSetSb.append("    public void ");
                        getSetSb.append("set");
                        getSetSb.append(getFieldName(yhTest.getColumnName(),"1"));
                        getSetSb.append("(");
                        getSetSb.append(dateType);
                        getSetSb.append(" ");
                        getSetSb.append(fieldName);
                        getSetSb.append(")");
                        getSetSb.append("\n");
                        getSetSb.append("    {");
                        getSetSb.append("\n");
                        getSetSb.append("        this.");
                        getSetSb.append(fieldName);
                        getSetSb.append(" = ");
                        getSetSb.append(fieldName);
                        getSetSb.append(";");
                        getSetSb.append("\n");
                        getSetSb.append("    }");
                        getSetSb.append("\n");
                    }
                }
            }
            
            
            
            
            
            
            
            
            sb.append(columnSb);
            sb.append(getSetSb);
            sb.append("}");
            sb.append("\n");
            
            
            
            
            entityFile = new File(filePath.concat(entityName).concat(".java"));
            if (!entityFile.exists())
            {
                entityFile.createNewFile();
            }
            fileWritter = new FileWriter(entityFile);
            fileWritter.write(sb.toString());
            fileWritter.flush();
            fileWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * <p>Created on 2019年6月8日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    private static String getFieldName(String columnName,String type)
    {
        StringBuffer entityName = new StringBuffer(); 
        String tableParts[] = columnName.split("_");
        String tablePart = null;
        for(int i=0,j=tableParts.length;i<j;i++)
        {
            tablePart = tableParts[i];
            if(i == 0)
            {
                if("0".equals(type))
                {
                    entityName.append(tablePart.substring(0, 1));
                }
                else
                {
                    entityName.append(tablePart.substring(0, 1).toUpperCase());
                }
            }
            else
            {
                entityName.append(tablePart.substring(0, 1).toUpperCase());
            }
            entityName.append(tablePart.substring(1));
        }
        return entityName.toString();
    }

    /**
     * <p>Created on 2019年6月8日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return String
     */
    private static String getDateType(String dataType)
    {
        if("varchar".equals(dataType) || "char".equals(dataType) || "text".equals(dataType))
        {
            return "String";
        }
        if("int".equals(dataType) || "bigint".equals(dataType)) 
        {
            return "Long";
        }
        if("mediumint".equals(dataType) || "smallint".equals(dataType) || "tinyint".equals(dataType)) 
        {
            return "Integer";
        }
        if("decimal".equals(dataType) || "double".equals(dataType) || "float".equals(dataType) || 
                "numeric".equals(dataType) || "real".equals(dataType))
        {
            return "java.math.BigDecimal";
        }
        if("date".equals(dataType) || "datetime".equals(dataType) || "timestamp".equals(dataType)) 
        {
            return "java.util.Date";
        }
        return "wangmeixia";
    }

    /**
     * <p>Created on 2019年6月7日</p>
     * <p>Description:[]</p>
     * @author:[杨欢][yanghuan1937@aliyun.com] 
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @return List<String>
     */
    private static List<String> getCommonColumnList()
    {
        List<String> commonColumnList = new ArrayList<String>();
        commonColumnList.add("remarks");
        commonColumnList.add("id");
        commonColumnList.add("create_date");
        commonColumnList.add("update_date");
        commonColumnList.add("del_flag");
        commonColumnList.add("version_");
        commonColumnList.add("create_by");
        commonColumnList.add("update_by");
        return commonColumnList;
    }
}
