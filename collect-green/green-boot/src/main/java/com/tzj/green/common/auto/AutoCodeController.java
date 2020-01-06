package com.tzj.green.common.auto;

import com.tzj.green.entity.YhTest;
import com.tzj.green.service.YhTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * <p>Created on 2019年6月6日</p>
 * <p>Title:       [杭州绿账]_[]_[]</p>
 * <p>Description: []</p>
 * <p>Copyright:   Copyright (c) 2017</p>
 * <p>Company:     上海挺之军信息科技有限公司 </p>
 * <p>Department:  研发部</p>
 * @author         [杨欢][yanghuan1937@aliyun.com]
 * @version        1.0
 */
@Controller("autoCodeController")
@RequestMapping("/auto")
public class AutoCodeController
{
    @Autowired
    private YhTestService yhTestService;

    @RequestMapping("/getTables")
    public @ResponseBody String getTabels(final HttpServletResponse response)
    {
        StringBuffer sb = new StringBuffer();
        List<YhTest> tablesList = yhTestService.getAllTables();
        if(null != tablesList && !tablesList.isEmpty())
        {
            for(YhTest yhTest : tablesList)
            {
                sb.append("<option value='");
                sb.append(yhTest.getTableName());
                sb.append("'>");
                sb.append(yhTest.getTableComment());
                sb.append("----");
                sb.append(yhTest.getTableName());
                sb.append("</option>");
            }
        }
        return sb.toString();
    }
    
    @RequestMapping("/makeCode")
    public @ResponseBody String makeCode(final String tableInfo,final String auth)
    {
        String msg = null;
        if(null == tableInfo || "".equals(tableInfo))
        {
            return "选一张啊";
        }
        try
        {
            msg = yhTestService.makeCode(tableInfo,auth);
        }
        catch (Exception e)
        {
            msg = "异常";
            e.printStackTrace();
        }
        return msg;
    }
    
}
