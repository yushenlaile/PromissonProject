package my.web;

import com.alibaba.fastjson.JSONObject;
import my.Util.RestfulResult;
import my.domain.Menu;
import my.domain.QueryParameter;
import my.domain.employee;
import my.domain.dataGridResult;
import my.service.MenuService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

@Controller
@Scope("prototype")//在WEB开发中我们尽量把控制器设置为多例的
public class menuControler {
    @Autowired
    /*注入业务层--必须是业务层的接口不可以是业务层的实现类*/
    public MenuService menuService;

    /*获取菜单页面*/
    @RequestMapping("/menu.action")
    public Object getmenuPage() {

        return "menu";
    }

    /*获取所有的菜单*/
    @RequestMapping("/MenuList.action")
    @ResponseBody
    public Object getMenuList(QueryParameter queryParameter) {
        System.out.println(queryParameter);
        System.out.println("------------MenuList---------------");
        dataGridResult dataGridResult = menuService.getMenuList(queryParameter);
        return dataGridResult;
    }


    /*获取父菜单*/
    @RequestMapping("/parentMenuList.action")
    @ResponseBody
    public Object parentMenuList() {

        List<Menu> list = menuService.getparentMenuList();
        return list;
    }

    /*添加菜单*/
    @RequestMapping("/savaMenu.action")
    @ResponseBody
    public Object savaMenu(Menu menu) {
        System.out.println(menu);
        RestfulResult restfulResult = new RestfulResult();
        try {
            menuService.addMenu(menu);
            restfulResult.setData("添加菜单成功");
            return restfulResult;
        } catch (Exception e) {
            System.out.println(e);
            restfulResult.setData("添加菜单失败");
            return restfulResult;
        }

    }

    /*编辑菜单*/
    @RequestMapping("/editMenu.action")
    @ResponseBody
    public Object editMenu(Menu menu) {
        System.out.println("编辑菜单");
        System.out.println(menu);
        return menuService.updateMenu(menu);
    }

    /*删除菜单*/
    @RequestMapping("/deleteMenu.action")
    @ResponseBody
    public Object deleteMenu(@RequestBody JSONObject jsonObject) {
        System.out.println("删除菜单");
        Long id = Long.valueOf(jsonObject.getString("id"));
        System.out.println(id);
        RestfulResult restfulResult = new RestfulResult();
        try {
            menuService.deleteMenu(id);
            restfulResult.setData("删除菜单成功");
            return restfulResult;
        } catch (Exception e) {
            System.out.println(e);
            restfulResult.setData("删除菜单失败");
            return restfulResult;
        }
    }

    /*获取树 tree的数据*/
    @RequestMapping("/GetTreeData.action")
    @ResponseBody
    public Object GetTreeData() {

        List<Menu> list = menuService.getTreeData();
        /*要判断当前用户是否有菜单（有多个菜单）需要的权限*/
        /*没有菜单的权限就从集合中删除对应的数据（菜单）--从而达到有对应的权限才显示对应的菜单*/
        /*1 获取当前用户  看当前用户是否是管理员 -是的话就无需判断，管理员拥有一切权限*/
        Subject subject = SecurityUtils.getSubject();
        /* 获取当前用户身份信息*/
        employee employee = (my.domain.employee) subject.getPrincipal();
        /*如果不是管理员*/
        if (employee.getAdmin() == false) {

            /*2 如果不是管理员  则做权限校验*/
            checkPermisson(list);
        }
        return list;
    }


    /*权限校验*/
    public void checkPermisson(List<Menu> list) {
        /* 获取当前用户 */
        Subject subject = SecurityUtils.getSubject();
        /*遍历所有的菜单及子菜单*/
        /* 获取集合的迭代器（我们要边遍历边删除）*/
        Iterator<Menu> iterator = list.iterator();
        /*是否有下一个数据*/
        while (iterator.hasNext()) {
            Menu menu = iterator.next();
            /*先判断当前遍历的菜单是否需要权限*/
         if(menu.getPermisson()!=null){
            String Presouce=menu.getPermisson().getPresouce();
             /*如果需要权限 ，再判断当前主体（当前用户）是否有当前遍历的菜单需要的权限，没有就把该菜单从集合中删除（有就不必刪除）*/
             /*没有就把该菜单从集合中删除*/
              if(subject.isPermitted(Presouce)==false)
              {
                  iterator.remove();
                  continue;
              }
         }
         /*判斷当前菜单是否有子菜单  子菜单也要做权限的校验*/
         if(menu.getChildren().size()>0)
         {
             checkPermisson(menu.getChildren());
         }

        }

    }


}
