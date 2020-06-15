package my.web;

import my.domain.Permisson;
import my.service.PermissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Scope("prototype")//在WEB开发中我们尽量把控制器设置为多例的
public class PermissonController {
    @Autowired
    public PermissonService permissonService;

    /*获取所有的权限*/
    @RequestMapping("/getallpermisson.action")
    @ResponseBody
    public List<Permisson> getallpermisson() {
        List<Permisson> list = permissonService.getallPermisson();
        System.out.println("权限Controller");
        return list;
    }

    //根据角色的id获取角色的所有的权限
    @RequestMapping("/getPermissonByRid.action")
    @ResponseBody
    public Object getPermissonByRid(Long rid) {
        System.out.println("根据角色的id获取角色的所有的权限:" + rid);
        List<Permisson> list = permissonService.getPermissonByRoleid(rid);
        return list;
    }
}
