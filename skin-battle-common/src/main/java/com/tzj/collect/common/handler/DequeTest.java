package com.tzj.collect.common.handler;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DequeTest {
   public static DelayQueue<Item> queue = new DelayQueue<>();

    public static void main(String[] args) throws InterruptedException {
        Item item1 = new Item("item1", 5, TimeUnit.SECONDS);
        Item item2 = new Item("item2",10, TimeUnit.SECONDS);
        Item item3 = new Item("item3",15, TimeUnit.SECONDS);

        queue.put(item1);
        queue.put(item2);
        queue.put(item3);
        System.out.println("begin time:" + LocalDateTime.now());


        Runnable runnable=new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                int i =4;
                while (true){
                    Item item5 = new Item("item"+i,15, TimeUnit.SECONDS);
                    queue.put(item5);
                    Thread.sleep(3000);
                }
            }
        };

        Thread t1 =new Thread(runnable);
        t1.start();
        while(true){
            if(queue.size()<=0){
                break;
            }
            Item take = queue.take();
            System.out.format("name:{%s}, time:{%s}\n",take.name, LocalDateTime.now());
        }

    }



}
