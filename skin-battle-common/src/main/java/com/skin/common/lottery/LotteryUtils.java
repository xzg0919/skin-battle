package com.skin.common.lottery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LotteryUtils {

    /**
     * 抽奖方法
     * create time: 2019/7/5 23:08
     *
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
        /*List<Goods> data=new ArrayList<>();
        Goods goods1=new Goods();
        goods1.setBili(0.0001);
        goods1.setGoodsId("1");
        Goods goods2=new Goods();
        goods2.setBili(0.00);
        goods2.setGoodsId("2");
        Goods goods3=new Goods();
        goods3.setBili(10.00);
        goods3.setGoodsId("3");
        Goods goods4=new Goods();
        goods4.setBili(12.00);
        goods4.setGoodsId("4");

        Goods goods5=new Goods();
        goods5.setBili(77.9999);
        goods5.setGoodsId("5");
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
            String name= data.get(orignalIndex).getGoodsId();
            if(map.containsKey(name)){
                Integer integer = map.get(name);
                map.put(name,++integer);
            }else{
                map.put(name,1);
            }
        }
        System.out.println("我抽奖了一百万次商品出现的概率"+map);*/

        List<LotteryBean> lotteryBeans = new ArrayList<>();
        LotteryBean goods1 = new LotteryBean();
        goods1.setBili(0.00);
        goods1.setGoodsId(1L);
        LotteryBean goods2 = new LotteryBean();
        goods2.setBili(20.00);
        goods2.setGoodsId(2L);
        LotteryBean goods3 = new LotteryBean();
        goods3.setBili(20.00);
        goods3.setGoodsId(3L);
        LotteryBean goods4 = new LotteryBean();
        goods4.setBili(77.9999);
        goods4.setGoodsId(4L);

        LotteryBean goods5 = new LotteryBean();
        goods5.setBili(77.9999);
        goods5.setGoodsId(5L);
        lotteryBeans.add(goods1);
        lotteryBeans.add(goods2);
        lotteryBeans.add(goods3);
        lotteryBeans.add(goods4);
        lotteryBeans.add(goods5);

        List<HashMap<String, Object>> lotteryResult = new ArrayList<>();
        List<Double> orignalRates = new ArrayList<>(lotteryBeans.size());
        //把商品比例放进来
        for (LotteryBean orignalRate : lotteryBeans) {
            orignalRates.add(orignalRate.getBili());
        }
        for (int i = 1; i <= 30000; i++) {
            int orignalIndex = LotteryUtils.lottery(orignalRates);
            Long goodsId = lotteryBeans.get(orignalIndex).getGoodsId();
            HashMap<String, Object> map = new HashMap();
            map.put("goodsId", goodsId);
            lotteryResult.add(map);
        }

        System.out.println(lotteryResult);

    }

    public static List<HashMap<String, Object>> lottery(List<LotteryBean> lotteryBeans, Integer num) {
        List<HashMap<String, Object>> lotteryResult = new ArrayList<>();
        List<Double> orignalRates = new ArrayList<>(lotteryBeans.size());
        //把商品比例放进来
        for (LotteryBean orignalRate : lotteryBeans) {
            orignalRates.add(orignalRate.getBili());

            System.out.println("id:"+orignalRate.getGoodsId()+"概率："+orignalRate.getBili());
        }
        for (int i = 1; i <= num; i++) {
            int orignalIndex = LotteryUtils.lottery(orignalRates);
            Long goodsId = lotteryBeans.get(orignalIndex).getGoodsId();
            HashMap<String, Object> map = new HashMap();
            map.put("goodsId", goodsId);
            lotteryResult.add(map);
        }
        return lotteryResult;
    }
}
