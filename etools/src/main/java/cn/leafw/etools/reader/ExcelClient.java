package cn.leafw.etools.reader;

import cn.leafw.etools.dto.UserDto;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.concurrent.*;

/**
 * @author CareyWYR
 * @date 2018/5/14 14:01
 */
public class ExcelClient {

    private static ArrayBlockingQueue<UserDto> userQueue = new ArrayBlockingQueue<>(100);

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File("C:\\Users\\qdd\\Desktop\\test.xlsx"));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            System.out.println(xssfWorkbook.getSheetAt(0).getLastRowNum());;
            ExecutorService producer = Executors.newSingleThreadExecutor();
            producer.submit(new ExcelProducer(userQueue,countDownLatch,sheet));
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,10,1000L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5));
            for (int i = 0; i < 5; i++) {
                threadPoolExecutor.submit(new ExcelConsumer(userQueue));
//                System.out.println(future.get());
            }
            countDownLatch.await();
            System.out.println("finish");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
