package com.tzj.collect.common.handler;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Item implements Delayed {

        // 触发时间(失效时间点)
        private long time;
        //名称
        String name;

        //数据失效时间
        public Item(String name, long time, TimeUnit unit) {
            this.name = name;
            this.time = System.currentTimeMillis() + (time > 0? unit.toMillis(time): 0);
        }
        //返回剩余有效时间
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
        /**
         *  比较两个Delayed对象的大小, 比较顺序如下:
         *  比较失效时间点, 先失效的返回-1,后失效的返回1
         */
        @Override
        public int compareTo(Delayed o) {
            Item item = (Item) o;
            return this.time - item.time <= 0 ? -1 : 1;
        }

        @Override
        public String toString() {
            return "Item{" + "time=" + time + ", name='" + name + '\'' + '}';
        }
    }