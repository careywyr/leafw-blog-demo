package cn.leafw.etools.reader;

import cn.leafw.etools.config.ThreadPoolConfig;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
public class ExcelAnalyze {

    public ArrayBlockingQueue queue = new ArrayBlockingQueue(100);

    public void readExcel() throws Exception{
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new File(""));
        SXSSFWorkbook workbook = new SXSSFWorkbook(xssfWorkbook);
        SXSSFSheet sheet = workbook.getSheetAt(0);
        //总行数
        int rowNum = sheet.getLastRowNum();

        for (int i = 0; i < rowNum; i++) {
            SXSSFRow row = sheet.getRow(i);
            String userId = row.getCell(0).getStringCellValue();

        }
    }

    private static void threadTask(){
        final CountDownLatch countDownLatch = new CountDownLatch(1000);
        final Semaphore semaphore = new Semaphore(5);
        ExecutorService executorService = ThreadPoolConfig.getExecutor();

    }
}
