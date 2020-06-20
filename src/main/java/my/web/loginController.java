package my.web;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("prototype")//在WEB开发中我们尽量把控制器设置为多例的
public class loginController {

    public loginController()
    {
        /*Controller默认是一个单例，在单例情况下，当Tomcat启动后（Springmvc框架加载后）就会自动创建一个控制器
        （设置为多例后就不会自动创建了--访问控制器的处理方法才会自动创建）*/
      //  System.out.println("-----创建了一个loginController控制器----");
    }


    @RequestMapping("/login.action")
    public Object login()
    {
        System.out.println("重定向到login.jsp");

        /*这个重定向的路径 不可以被Shiro过滤器所拦截，不然就会出现重定向次数过多的错误
        * 因为login.action是一个loginUrl登录认证路径，如果这个重定向路径被Shiro过滤器拦截，
        * 因为默认没有被认证故它又会再次跳转到login.action改请求中
        * 从而无线循环导致重定向次数过多
        *
        * */
        return "redirect:login.jsp";
    }
}
