package my.web;

import my.afspring.AfRestData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class test1Controller {


    @RequestMapping("/test3.action")
    public Object testss()
    {

        return new AfRestData("测试成功2222");
    }
}
