package cn.leafw.blog.springboot.thread;

import cn.leafw.blog.springboot.thread.thread.ThreadExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThreadApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testThread(){
		ThreadExecutor threadExecutor = new ThreadExecutor();
		try {
			threadExecutor.DataExecutor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
