package cn.leafw.etools.reader;

import cn.leafw.etools.dto.UserDto;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/14 14:12
 */
public class ExcelConsumer implements Runnable{

    private ArrayBlockingQueue<UserDto> userQueue;

    @Override
    public void run() {
        while (true) {
            try {
                UserDto userDto = userQueue.take();
                System.out.println("Thread:  "+Thread.currentThread().getName() +" -> " + userDto.getUserId()+ " : " + userDto.getUserName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ExcelConsumer(ArrayBlockingQueue<UserDto> userQueue) {
        this.userQueue = userQueue;
    }
}
