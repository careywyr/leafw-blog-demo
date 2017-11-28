package cn.leafw.blog.springboot.thread.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Carey on 2017/11/28.
 */
@RestController
public class MainController
{
    @GetMapping("/")
    public String mainInfo(){
        return "---";
    }
}
