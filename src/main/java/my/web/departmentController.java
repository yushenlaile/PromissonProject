package my.web;

import my.domain.department;
import my.service.departmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Scope("prototype")//在WEB开发中我们尽量把控制器设置为多例的
public class departmentController {

    /*注入业务层*/
    @Autowired
    public departmentService departmentService;

    /*查询所有的部门*/
    @RequestMapping("/getalldepartment.action")
    @ResponseBody
    public Object getalldepartment() {
        List<department> list = departmentService.getalldepartments();
        return list;
    }


}
