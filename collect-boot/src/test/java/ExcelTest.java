import com.tzj.collect.Application;
import com.tzj.collect.common.utils.ExcelHelper;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.core.service.CompanyCategoryService;
import com.tzj.collect.core.service.CompanyStreeService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CompanyCategory;
import com.tzj.collect.entity.CompanyStree;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/6/4 15:36
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ExcelTest {
    @Autowired
    AreaService areaService;
    @Autowired
    CompanyStreeService companyStreeService;
    @Autowired
    CompanyCategoryService companyCategoryService;
    @Autowired
    CategoryService categoryService;

    @Test
    @SneakyThrows
    public void wugongjin() {
        List<Map<String, Object>> excelObj =
                ExcelHelper.importExeclFileForPoi(new File("/Users/xiangzhongguo/Downloads/五公斤城市.xlsx"), 0, "xlsx");
        Long companyId = 20L;
        excelObj.forEach(excel -> {
            System.out.println(excel.get("map0").toString());
            List<Area> districtList = areaService.findAreaByCityName(excel.get("map0").toString());
            districtList.forEach(area -> {
                System.out.println(area.getAreaName());
                List<Area> streetList = areaService.findstreetByAreaId(area.getId());
                streetList.forEach(street -> {

                    //判断是否存在
                    if (companyStreeService.selectCount(companyId, street.getId(), area.getId()) == 0) {
                        System.out.println(area.getAreaName()+"--------"+street.getAreaName()+"-------新增");
                        //不存在 新增
                        CompanyStree companyStree = new CompanyStree();
                        companyStree.setAreaId(Integer.parseInt(area.getId() + ""));
                        companyStree.setCompanyId(Integer.parseInt(companyId + ""));
                        companyStree.setStreeId(Integer.parseInt(street.getId() + ""));
                        companyStreeService.insert(companyStree);
                        List<Category> childList = categoryService.childList(45);
                        Category parentCategory = categoryService.selectById(45);
                        for (Category category : childList) {
                            if (companyCategoryService.selectCount(companyId, category.getId()) == 0) {
                                if (category.getId() != 51) {
                                    //新增品类
                                    CompanyCategory companyCategory = new CompanyCategory();
                                    companyCategory.setCategoryId(category.getId().toString());
                                    companyCategory.setParentId(parentCategory.getId());
                                    companyCategory.setParentName(parentCategory.getName());
                                    companyCategory.setParentIds(category.getParentIds());
                                    companyCategory.setCompanyId(companyId + "");
                                    companyCategory.setPrice(category.getMarketPrice().floatValue());
                                    companyCategory.setUnit(category.getUnit());
                                    companyCategory.setAdminCommissions(new BigDecimal("0.12"));
                                    companyCategory.setCompanyCommissions(BigDecimal.ZERO);
                                    companyCategory.setFreeCommissions(new BigDecimal("0.4"));
                                    companyCategory.setIsCommissions("1");
                                    companyCategoryService.insert(companyCategory);
                                }
                            }
                        }
                    }else{
                        System.out.println(area.getAreaName()+"--------"+street.getAreaName());
                    }
                });
            });


        });

        System.out.println("结束");

    }



    @Test
    @SneakyThrows
    public void wugongjin1() {
        List<Map<String, Object>> excelObj =
                ExcelHelper.importExeclFileForPoi(new File("/Users/xiangzhongguo/Downloads/五公斤城市.xlsx"), 0, "xlsx");
        Long companyId = 20L;

        List<String> companyNames = companyStreeService.findCompanyCityName(companyId);
        List<String> companyNamesNew = new ArrayList<>();

        for(Map excel:excelObj){
            if (!companyNames.contains(excel.get("map0").toString())) {
                companyNamesNew.add(excel.get("map0").toString());
            }
        }
        System.out.println("共：" + companyNamesNew.size());
        companyNamesNew.forEach(name -> {
            System.out.println("城市-------" + name);
            List<Area> districtList = areaService.findAreaByCityName(name);
            districtList.forEach(area -> {
                System.out.println(area.getAreaName());
                List<Area> streetList = areaService.findstreetByAreaId(area.getId());
                streetList.forEach(street -> {

                    //判断是否存在
                    if (companyStreeService.selectCount(companyId, street.getId(), area.getId()) == 0) {
                        System.out.println(area.getAreaName() + "--------" + street.getAreaName() + "-------新增");
                        //不存在 新增
                        CompanyStree companyStree = new CompanyStree();
                        companyStree.setAreaId(Integer.parseInt(area.getId() + ""));
                        companyStree.setCompanyId(Integer.parseInt(companyId + ""));
                        companyStree.setStreeId(Integer.parseInt(street.getId() + ""));
                        companyStreeService.insert(companyStree);
                        /*List<Category> childList = categoryService.childList(45);
                        Category parentCategory = categoryService.selectById(45);
                        for (Category category : childList) {
                            if (companyCategoryService.selectCount(companyId, category.getId()) == 0) {
                                if (category.getId() != 51) {
                                    //新增品类
                                    CompanyCategory companyCategory = new CompanyCategory();
                                    companyCategory.setCategoryId(category.getId().toString());
                                    companyCategory.setParentId(parentCategory.getId());
                                    companyCategory.setParentName(parentCategory.getName());
                                    companyCategory.setParentIds(category.getParentIds());
                                    companyCategory.setCompanyId(companyId + "");
                                    companyCategory.setPrice(category.getMarketPrice().floatValue());
                                    companyCategory.setUnit(category.getUnit());
                                    companyCategory.setAdminCommissions(new BigDecimal("0.12"));
                                    companyCategory.setCompanyCommissions(BigDecimal.ZERO);
                                    companyCategory.setFreeCommissions(new BigDecimal("0.4"));
                                    companyCategory.setIsCommissions("1");
                                    companyCategoryService.insert(companyCategory);
                                }
                            }
                        }*/
                    } else {
                        System.out.println(area.getAreaName() + "--------" + street.getAreaName());
                    }
                });

            });

            System.out.println("结束");


        });


    }
}
