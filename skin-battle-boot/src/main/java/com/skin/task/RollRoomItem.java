package com.skin.task;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/24 16:06
 * @Description:
 */
@Data
public class RollRoomItem implements Delayed {

    // 触发时间(失效时间点)
    private long time;
    //名称
    Long id;

    String name;

    //数据失效时间
    public RollRoomItem(String name, long time, Long roomId) {
        this.name = name;
        this.id = roomId;
        this.time = time;
    }

    //返回剩余有效时间
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 比较两个Delayed对象的大小, 比较顺序如下:
     * 比较失效时间点, 先失效的返回-1,后失效的返回1
     */
    @Override
    public int compareTo(Delayed o) {
        RollRoomItem item = (RollRoomItem) o;
        return this.time - item.time <= 0 ? -1 : 1;
    }

    @Override
    public String toString() {
        return "Item{" + "房间id:" + id + "time=" + time + ", name='" + name + '\'' + '}';
    }
}
