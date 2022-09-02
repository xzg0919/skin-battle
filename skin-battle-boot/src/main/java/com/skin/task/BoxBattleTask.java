package com.skin.task;

import com.skin.core.service.BoxBattleService;
import com.skin.core.service.RollRoomService;
import com.skin.entity.BoxBattleInfo;
import com.skin.entity.RollRoom;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.DelayQueue;

/**
 * @Auther: xiangzhongguo
 * @Date: 2022/8/24 15:57
 * @Description:
 */
@Component
public class BoxBattleTask implements ApplicationRunner {


    @Autowired
    BoxBattleService boxBattleService;


    public static DelayQueue<BoxBattleItem> queue = new DelayQueue<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Runnable runnable =new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                List<BoxBattleInfo> unFinishRoom = boxBattleService.getUnfinishList();
                for (BoxBattleInfo boxBattleInfo : unFinishRoom) {
                    long time =boxBattleInfo.getUpdateDate().getTime()+1000*boxBattleInfo.getBoxCount();
                    BoxBattleItem item =  new BoxBattleItem(time,boxBattleInfo.getId());
                    queue.put(item);
                }
                System.out.println("拼箱task已启动：" + LocalDateTime.now());

                while(true){
                    BoxBattleItem take = queue.take();
                    boxBattleService.finishBattle(take.getId());
                }
            }
        };

        Thread t =new Thread(runnable);
        t.setName("Thread-boxBattleTask");
        t.setDaemon(true);
        t.start();
    }
}

