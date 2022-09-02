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
public class BoxBattleItem implements Delayed {

    // 触发时间(失效时间点)
    private long time;
    //名称
    Long id;


    //数据失效时间
    public BoxBattleItem( long time, Long boxBattleId) {

        this.id = boxBattleId;
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
        BoxBattleItem item = (BoxBattleItem) o;
        return this.time - item.time <= 0 ? -1 : 1;
    }


}
