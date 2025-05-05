package cn.leafw.locktrans;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Desc MybatisPlusGenerator
 * @Author leafw
 **/
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://rm-bp141l93c8my01io2co.mysql.rds.aliyuncs.com:3306/lock_trans", "leafw", "Wyr95626@@@")
                .globalConfig(builder -> {
                    builder.author("leafw") // 设置作者
                            .outputDir("/Users/leafw/Documents/workspace/leafw-blog-demo/2025/lock_trans/src/main/java/cn/leafw/locktrans/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("cn.leafw")
                            .moduleName("lock_trans")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/leafw/Documents/workspace/leafw-blog-demo/2025/lock_trans/src/main/resources/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(new ArrayList<String>() {{
                                add("t_user_info");
                            }})
                            .addTablePrefix("t_")
                            .entityBuilder()
                            .enableLombok()
                            .formatFileName("%sEntity");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
