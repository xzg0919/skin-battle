package com.tzj.collect.common.handler;

import java.util.*;

public class Utils {

    /**
     * 抽奖方法
     * create time: 2019/7/5 23:08
     * @param orignalRates 商品中奖概率列表，保证顺序和实际物品对应
     * @return 中奖商品索引
     */
    public static int lottery(List<Double> orignalRates) {

        if (orignalRates == null || orignalRates.isEmpty()) {
            return -1;
        }

        int size = orignalRates.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : orignalRates) {
            sumRate += rate;
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0d;
        for (double rate : orignalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }
        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return sortOrignalRates.indexOf(nextDouble);
    }

    public static void main(String[] args) {
        List<Goods> data=new ArrayList<>();
        Goods goods1=new Goods();
        goods1.setBili(0.0001);
        goods1.setGoodsId("1");
        goods1.setGoodsname("苹果11pro 256G");
        Goods goods2=new Goods();
        goods2.setBili(0.00);
        goods2.setGoodsId("2");
        goods2.setGoodsname("华为P40 pro");
        Goods goods3=new Goods();
        goods3.setBili(10.00);
        goods3.setGoodsId("3");
        goods3.setGoodsname("100元现金");
        Goods goods4=new Goods();
        goods4.setBili(12.00);
        goods4.setGoodsId("3");
        goods4.setGoodsname("100元京东卡");

        Goods goods5=new Goods();
        goods5.setBili(77.9999);
        goods5.setGoodsId("3");
        goods5.setGoodsname("50元话费");
        data.add(goods1);
        data.add(goods2);
        data.add(goods3);
        data.add(goods4);
        data.add(goods5);
        List<Double> orignalRates = new ArrayList<Double>(data.size());
        //把商品比例放进来
        for (Goods orignalRate : data) {
            orignalRates.add(orignalRate.getBili());
        }
        Map<String,Integer> map=new HashMap();
        for (int i = 0; i <1000000 ; i++) {
            int orignalIndex = Utils.lottery(orignalRates);
            String name= data.get(orignalIndex).getGoodsname();
            if(map.containsKey(name)){
                Integer integer = map.get(name);
                map.put(name,++integer);
            }else{
                map.put(name,1);
            }
        }
        System.out.println("我抽奖了一百万次商品出现的概率"+map);

    }
}


class Goods{
    private String goodsId;

    private String goodsname;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Double getBili() {
        return bili;
    }

    public void setBili(Double bili) {
        this.bili = bili;
    }

    //中奖比例 总和为1
    private Double  bili;

}