package cn.leafw.locktrans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cn.leafw.locktrans.mapper")
public class LockTransApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockTransApplication.class, args);
    }

}
