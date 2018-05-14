package cn.leafw.etools.reader;

import cn.leafw.etools.config.PageConfig;
import cn.leafw.etools.config.ThreadPoolConfig;
import cn.leafw.etools.dto.UserDto;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 11:21
 */
public class ExcelProducer implements Runnable{

    private ArrayBlockingQueue<UserDto> queue;

    private CountDownLatch countDownLatch;

    private volatile boolean flag;

    private XSSFSheet sheet;

    public void readExcel() throws Exception{
        //总行数
        final int rowNum = sheet.getLastRowNum();

        final int pageSize = (int)Math.ceil((double)rowNum/(PageConfig.PAGE_SIZE));
        System.out.println("读取excel，rowNum = " + rowNum + " ,pageSize = "+pageSize);

        //分页插入队列
        for (int i = 1; i < pageSize+1; i++) {
            for (int j = (i-1)*PageConfig.PAGE_SIZE; j < i*PageConfig.PAGE_SIZE; j++) {
                XSSFRow row = sheet.getRow(j);
//                String userId = row.getCell(0).getStringCellValue();
                double userIdNum = row.getCell(0).getNumericCellValue();
                String userName = row.getCell(1).getStringCellValue();
                UserDto userDto = new UserDto();
                userDto.setUserId(userIdNum);
                userDto.setUserName(userName);
                queue.put(userDto);
            }
        }
        countDownLatch.countDown();
    }

    @Override
    public void run() {
        try {
            readExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExcelProducer(ArrayBlockingQueue<UserDto> queue, CountDownLatch countDownLatch, XSSFSheet sheet) {
        this.queue = queue;
        this.countDownLatch = countDownLatch;
        this.sheet = sheet;
    }
}
