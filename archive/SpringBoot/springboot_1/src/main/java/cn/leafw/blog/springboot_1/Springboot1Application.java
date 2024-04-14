package cn.leafw.blog.springboot_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Springboot1Application {

	@GetMapping("/")
	public String index(){
		return "Spring Boot";
	}

	public static void main(String[] args) {
		SpringApplication.run(Springboot1Application.class, args);
	}
}
