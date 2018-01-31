package cn.leafw.blog.springcloud.serviceprovider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class ServiceproviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceproviderApplication.class, args);
	}

	@Value("${server.port}")
	String port;

	@GetMapping("/test")
	public String getPort(@RequestParam String userName){
		return "this is "+userName+":"+port+" request";
	}
}
