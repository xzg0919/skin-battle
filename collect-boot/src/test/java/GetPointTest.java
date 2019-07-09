import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.CategoryBean;
import com.tzj.collect.api.ali.param.PageBean;
import com.tzj.module.easyopen.util.ApiUtil;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GetPointTest {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "ZK");
        map.put("age", 1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "ZA");
        map2.put("age", 2);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("name", "CX");
        map3.put("age", 1);

        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("name", "CX");
        map4.put("age", 1);

        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        list.add(map);
        list.add(map2);
        list.add(map3);
        list.add(map4);

        // 排序代码如下
        List<Map<String, Object>> collect = list.stream().sorted(Comparator.comparing(GetPointTest::comparingByAge).reversed())
                .collect(Collectors.toList());
        collect.stream().forEach(System.out::println);
    }

    private static Integer comparingByAge(Map<String, Object> map){
        return (Integer) map.get("age");
    }
}
