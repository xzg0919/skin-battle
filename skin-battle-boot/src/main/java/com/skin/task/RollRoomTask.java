package com.skin.task;

import com.skin.core.service.RollRoomService;
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
public class RollRoomTask implements ApplicationRunner {

    @Autowired
    RollRoomService rollRoomService;


    public static DelayQueue<RollRoomItem> queue = new DelayQueue<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Runnable runnable =new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                List<RollRoom> unFinishRoom = rollRoomService.getUnfinishRoom();
                for (RollRoom rollRoom : unFinishRoom) {
                    RollRoomItem item =  new RollRoomItem(rollRoom.getName(),rollRoom.getLotteryTime().getTime(),rollRoom.getId());
                    queue.put(item);
                }
                System.out.println("roll房task已启动" + LocalDateTime.now());

                while(true){
                    RollRoomItem take = queue.take();
                    rollRoomService.roll(take.getId());
                }
            }
        };

        Thread t =new Thread(runnable);
        t.setDaemon(true);
        t.start();
    }
}

