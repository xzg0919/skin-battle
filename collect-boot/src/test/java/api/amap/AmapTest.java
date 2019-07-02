package api.amap;

import api.util.ApiUtils;
import api.util.TestConst;
import com.alibaba.fastjson.JSON;
import com.tzj.collect.api.ali.param.AmapAroundParam;
import io.itit.itf.okhttp.FastHttpClient;
import io.itit.itf.okhttp.Response;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AmapTest extends TestCase {

    private String amap_key = "43d675a28f264a241bd878df2c420176";

    /**
     * 测试高德逆地址编码
     *
     * @throws Exception
     */
    @Test
    public void testRegeo() throws Exception {
        String url="https://restapi.amap.com/v3/geocode/regeo";
        Response response= FastHttpClient.get().url(url)
                .addParams("key",amap_key)
                .addParams("location","121.442891,31.236803")
                .addParams("extensions","all")
                .addParams("poitype","120000|120100|120200|120201|120202|120203|120300|120301|120302|120303")
                .addParams("homeorcorp","1")
                .build().execute();
        String result=response.body().string();
        System.out.println(result);
    }

    @Test
    public void testAround() throws Exception {
        String url="https://restapi.amap.com/v3/place/around";
        Response response= FastHttpClient.get().url(url)
                .addParams("key",amap_key)
                .addParams("location","121.551919,31.281297")
                //.addParams("keywords","紫藤苑")
                .addParams("offset","20")
                .addParams("page","2")
                //.addParams("extensions","all")
                .build().execute();
        String result=response.body().string();
        System.out.println(result);
    }

    @Test
    public void testAmapApiRegeo() throws Exception{
        String resultJson= ApiUtils.createCommonParam(TestConst.appid,TestConst.gateway,
                "amap.regeo","121.451635,31.240466",TestConst.token,null);
        System.out.println(resultJson);
    }

    @Test
    public void testAmapApiRound() throws Exception{

        AmapAroundParam aroundParam=new AmapAroundParam();
        aroundParam.setOffset("50");
        aroundParam.setPage("1");
        aroundParam.setLocation("112.609588,35.0827");

        String resultJson= ApiUtils.createCommonParam(TestConst.appid,TestConst.gateway,
                "amap.around",aroundParam,TestConst.token,null);
        System.out.println(resultJson);
    }

    @Test
    public void testAmapApiUpdate() throws Exception{
        String resultJson= ApiUtils.createCommonParam(TestConst.appid,TestConst.gateway,
                "amap.update",null,TestConst.token,null);
        System.out.println(resultJson);
    }

    @Test
    public void testAll() throws Exception {

        List<List<Object>> dataList = new ArrayList<List<Object>>();
        List<Object> rowList = null;
        for (int i = 0; i <= 100000; i++) {
            String url = "https://api02.aliyun.venuscn.com/area/all";
            Response response = FastHttpClient.get().url(url).addHeader("Authorization", "APPCODE " + "971d4aff34a446dca09536cd2c43ab20")
                    .addParams("level", "3")
                    .addParams("page", String.valueOf(i))
                    .addParams("size", "50")
                    .build().execute();
            String result = response.body().string();
            System.out.println(result);
            if (result != null && result.length() > 0) {
                ResultBean resultBean = JSON.parseObject(result, ResultBean.class);
                if (resultBean != null && resultBean.getData() != null) {
                    List<ResultBean.DataBean> dataBeans = resultBean.getData();
                    if (dataBeans != null && dataBeans.size() > 0) {
                        for (ResultBean.DataBean dataBean : dataBeans) {

                            if(dataBean==null){
                                break;
                            }



                            //写入库或者文件
                            if(dataBean.getMerger_name().contains("台湾,")){
                                continue;
                            }

                            rowList = new ArrayList<Object>();
                            rowList.add(dataBean.getName());
                            rowList.add(dataBean.getArea_code());
                            rowList.add(dataBean.getMerger_name());
                            dataList.add(rowList);
                        }
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }else{
                break;
            }
        }

        Object[] head = {"街道名称", "街道编码", "省市街道"};
        List<Object> headList = Arrays.asList(head);

        String fileName = "jiedao.csv";//文件名称
        String filePath = "/Users/shark/Documents"; //文件路径


        File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            csvFile = new File(filePath + fileName);
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 1024);


            int num = headList.size() / 2;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < num; i++) {
                buffer.append(" ,");
            }
            csvWtriter.write(buffer.toString() + fileName + buffer.toString());
            csvWtriter.newLine();

            // 写入文件头部
            writeRow(headList, csvWtriter);

            // 写入文件内容
            for (List<Object> row : dataList) {
                writeRow(row, csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        for (Object data : row) {
            StringBuffer sb = new StringBuffer();
            String rowStr = sb.append("\"").append(data).append("\",").toString();
            csvWriter.write(rowStr);
        }
        csvWriter.newLine();
    }
}
