import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tzj.collect.Application;
import com.tzj.collect.core.service.AreaService;
import com.tzj.collect.core.service.CategoryCityService;
import com.tzj.collect.core.service.CategoryService;
import com.tzj.collect.entity.Area;
import com.tzj.collect.entity.Category;
import com.tzj.collect.entity.CategoryCity;
import lombok.SneakyThrows;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xiangzhongguo
 * @Date: 2021/8/3 15:12
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class 城市系数修改 {

    @Autowired
    AreaService areaService;
    @Autowired
    CategoryCityService categoryCityService;
    @Autowired
    CategoryService categoryService;

    @SneakyThrows
    @org.junit.Test
    public void 城市系数修改方法() {

        List<Area> allCity = areaService.findAllCity();
        List<Category> childList = categoryService.childList(78);
        List<Long> ids = new ArrayList<>();
        childList.forEach(category -> {
            ids.add(category.getId());
        });
        ids.add(113L);
        int i = 1;
        for (Area area : allCity) {

            int finalI = i;
            ids.forEach(id -> {
                CategoryCity categoryCity = categoryCityService.selectOne(new EntityWrapper<CategoryCity>().eq("city_id", area.getId())
                        .eq("category_id", id));
                System.out.println(finalI);
                if (null == categoryCity) {
                    categoryCity = new CategoryCity();
                    Category category = categoryService.selectById(id);
                    Category parentCategory = categoryService.selectById(category.getParentId());
                    categoryCity.setCityId(area.getId() + "");
                    categoryCity.setCategoryId(category.getId().intValue());
                    categoryCity.setParentId(category.getParentId());
                    categoryCity.setParentName(parentCategory.getName());
                    categoryCity.setParentIds(category.getParentIds());
                    categoryCity.setUnit(category.getUnit());
                    categoryCity.setPrice(new BigDecimal("108"));
                    categoryCityService.insertOrUpdate(categoryCity);
                }

            });
            i++;

        }


        System.out.println("结束");


    }
}
