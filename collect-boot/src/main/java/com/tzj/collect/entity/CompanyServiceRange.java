package com.tzj.collect.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 *
 *回收企业服务范围表
 *
 * @Author 王灿
 **/
@TableName("sb_company_service")
public class CompanyServiceRange extends  DataEntity<Long>{
    
	/**
	* @Fields field:field:【】
	*/
	
	private static final long serialVersionUID = -7990931553853279851L;
	
	

	private Long id;

    /**
     * 小区ID
     * @return
     */
    private Integer communityId;
    
    @TableField(exist=false)
    private Community community;
    /**
     * 区域ID
     * @return
     */
    private Integer areaId;
    @TableField(exist=false)
    private Area Area;
    /**
     * 0为私海,1为公海
     * @return
     */
    private String level;
    /**
     * 区域层级
     * @return
     */
    private String parentIds;

    private String companyId;
    @TableField(exist=false)
    private Company company;
    
    
	/**
	* @return company
	*/
	
	public Company getCompany() {
		return company;
	}

	
	/**
	* @param paramtheparamthe{bare_field_name} to set
	*/
	
	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Area getArea() {
		return Area;
	}

	public void setArea(Area area) {
		Area = area;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

   
    
}
