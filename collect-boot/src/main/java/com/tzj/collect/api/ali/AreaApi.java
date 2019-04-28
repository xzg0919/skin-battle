package com.tzj.collect.api.ali;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.api.ali.param.AreaBean;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Community;
import com.tzj.collect.service.AreaService;
import com.tzj.collect.service.CommunityService;
import com.tzj.module.api.annotation.Api;
import com.tzj.module.api.annotation.ApiService;
import com.tzj.module.api.annotation.RequiresPermissions;
import com.tzj.module.api.annotation.SignIgnore;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tzj.collect.common.constant.TokenConst.ALI_API_COMMON_AUTHORITY;

/**
 * 地区相关api
 * @Author 王美霞20180305
 **/
@ApiService
public class AreaApi {
	    
	@Autowired
	private AreaService areaService;
	
	@Resource
	private CommunityService communityService;
	
	
    /**
     * 根据层级取得所有该层地区
     * @param 
     * @return
     */
    @Api(name = "area.getByArea", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public Object getByArea(AreaBean area){
    	if(StringUtils.isBlank(area.getCityId())||"0".equals(area.getCityId())) {
    		return "您所在的市暂不提供回收地址";
    	}
    	return areaService.getByArea(2,area.getCityId());
    }
	
	
	
	/**
     * 根据父级取得所有子地区
     * @param 
     * @return
     */
    @Api(name = "area.child", version = "1.0")
    @SignIgnore
    @RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
    public JSONArray childArea(AreaBean area){
    	 List<Area>  areaLi= areaService.getChildArea(Long.valueOf(area.getId()));
    	 //经度
    	 String longitude = area.getLongitude();
    	 //纬度
    	 String latitude = area.getLatitude();
    	 JSONArray array=new JSONArray();
    	 for (Area area2 : areaLi) {
			JSONObject obj=new JSONObject();
			obj.put("area", area2);
			if(StringUtils.isBlank(longitude)||StringUtils.isBlank(latitude)) {
				obj.put("community", communityService.areaCommunity(Integer.parseInt(area2.getId()+"")));
			}else {
				obj.put("community", communityService.areaCommunityList(Integer.parseInt(area2.getId()+""),Double.parseDouble(longitude),Double.parseDouble(latitude)));
			}
			array.add(obj);
		}
    	 return array;
    }


	/**
	 * 小程序根据行政区名字获取该行政区下级街道
	 * @param
	 * @return
	 */
	@Api(name = "area.getStreetByAreaName", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object getStreetByAreaName(AreaBean areaBean){
		if(StringUtils.isBlank(areaBean.getAreaName())) {
			return "请传入行政区名字";
		}
		//根据行政区名字获取行政区Id
		Area area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", areaBean.getAreaName()).in("parent_id",areaBean.getCityId()));
		//返回街道信息
		return areaService.selectList(new EntityWrapper<Area>().eq("parent_id",area.getId()));
	}

	/**
	 * 小程序根据街道名字获取该街道下级小区
	 * @param
	 * @return
	 */
	@Api(name = "area.getCommunityBystreetName", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object getCommunityBystreetName(AreaBean areaBean){
		if(StringUtils.isBlank(areaBean.getStreetName())) {
			return "请传入街道名字";
		}
		//根据街道名字获取街道信息
		Area area = areaService.selectOne(new EntityWrapper<Area>().eq("area_name", areaBean.getStreetName()));
		//返回街道信息
		return communityService.selectList(new EntityWrapper<Community>().eq("area_id",area.getId()));
	}
	/**
	 * 小程序获取所有城市列表
	 * @param
	 * @return
	 */
	@Api(name = "area.getCityList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object getCityList(){
		List<Area> areaList = areaService.selectList(new EntityWrapper<Area>().eq("type", 1));
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Area area : areaList) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("city",area.getAreaName());
			map.put("adCode",area.getId());
			map.put("spell",getPingYin(area.getAreaName()));
			resultList.add(map);
		}
		return resultList;
	}
	/**
	 * 小程序获取上海所有的行政区及对应的街道
	 * @param
	 * @return
	 */
	@Api(name = "area.getCityAreaList", version = "1.0")
	@SignIgnore
	@RequiresPermissions(values = ALI_API_COMMON_AUTHORITY)
	public Object getCityAreaList(){
		//获取上海的所有行政区
		List<Area> areaList = areaService.selectList(new EntityWrapper<Area>().eq("parent_id", 737).eq("del_flag", 0));
		List<Map<String,Object >> list = new ArrayList<>();
		Map<String,Object > map = null;
		for (Area area: areaList) {
			map = new HashMap<>();
			map.put("name",area.getAreaName());
			List<Area> areaLists = areaService.selectList(new EntityWrapper<Area>().eq("parent_id", area.getId()).eq("del_flag", 0));
			List<Map<String,Object >> lists = new ArrayList<>();
			Map<String,Object > maps = null;
			for (Area areas: areaLists) {
				maps = new HashMap<>();
				maps.put("name",areas.getAreaName());
				maps.put("id",areas.getId());
				lists.add(maps);
			}
			map.put("subList",lists);
			list.add(map);
		}
		return list;
	}


	// 将汉字转换为全拼
	public  String getPingYin(String src) {

		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(t1[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				} else
					t4 += java.lang.Character.toString(t1[i]);
			}
			// System.out.println(t4);
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}


}
