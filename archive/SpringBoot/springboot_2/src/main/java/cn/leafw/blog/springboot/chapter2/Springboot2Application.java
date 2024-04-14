package cn.leafw.blog.springboot.chapter2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Springboot2Application {

    @Value("${user.code}")
    private String code;
    @Value("${user.age}")
    private String age;
    @Value("${user.desc}")
    private String desc;

	@GetMapping("/")
	public String index(){
		return "Spring Boot";
	}

    @GetMapping("/getUser")
    public String getUser(){
        return "hello, "+code+" "+age+" "+ desc;
    }

	public static void main(String[] args) {
		SpringApplication.run(Springboot2Application.class, args);
	}
}
