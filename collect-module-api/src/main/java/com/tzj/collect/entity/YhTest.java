package com.tzj.collect.entity;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("sb_yh_test")
public class YhTest extends DataEntity<Long>
{
    /**
     * <p>Description:[]</p>
     */
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    private Long id;
    private String tableName;
    private String tableComment;
    private String columnName;
    private String dataType;
    private String columnComment;
    
    @Override
    public Long getId()
    {
        return id;
    }

    @Override
    public void setId(Long aLong)
    {
        this.id = aLong;
    }
    /**
     * <p>Description:[获取]</p>
     * @return String tableName.
     */
    public String getTableName()
    {
        return tableName;
    }
    /**
     * <p>Description:[设置]</p>
     * @param String tableName 
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    /**
     * <p>Description:[获取]</p>
     * @return String tableComment.
     */
    public String getTableComment()
    {
        return tableComment;
    }
    /**
     * <p>Description:[设置]</p>
     * @param String tableComment 
     */
    public void setTableComment(String tableComment)
    {
        this.tableComment = tableComment;
    }

    /**
     * <p>Description:[获取]</p>
     * @return String columnName.
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * <p>Description:[设置]</p>
     * @param String columnName 
     */
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    /**
     * <p>Description:[获取]</p>
     * @return String dataType.
     */
    public String getDataType()
    {
        return dataType;
    }

    /**
     * <p>Description:[设置]</p>
     * @param String dataType 
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    /**
     * <p>Description:[获取]</p>
     * @return String columnComment.
     */
    public String getColumnComment()
    {
        return columnComment;
    }

    /**
     * <p>Description:[设置]</p>
     * @param String columnComment 
     */
    public void setColumnComment(String columnComment)
    {
        this.columnComment = columnComment;
    }
    
}
