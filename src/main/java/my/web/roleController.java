package my.web;

import com.alibaba.fastjson.JSONObject;
import my.afspring.AfRestData;
import my.domain.QueryParameter;
import my.Util.RestfulResult;
import my.domain.Role;
import my.domain.dataGridResult;
import my.service.roleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Scope("prototype")//在WEB开发中我们尽量把控制器设置为多例的
public class roleController {
    @Autowired
    public roleService Roleservice;

    /*返回角色页面*/
    @RequestMapping("/role.action")
    public Object getrolePage() {

        return "role";
    }

    /*获取所有的角色信息*/
    @RequestMapping("/getrolelist.action")
    @ResponseBody
    public Object getrolelist(QueryParameter queryParameter) {
        System.out.println(queryParameter);
        System.out.println("roleController");
        dataGridResult datagridresult = Roleservice.getallRoleinfo(queryParameter);
        return datagridresult;
    }

    /*保存角色*/
    @RequestMapping("/saveRole.action")
    @ResponseBody
    public Object saveRole(Role role) {
        System.out.println(role);
        RestfulResult restfulResult = new RestfulResult();
        //调用业务层，保存角色和角色相关联的权限
        try {
            Roleservice.saveRole(role);
            restfulResult.setData("保存角色成功");
            return restfulResult;
        } catch (Exception e) {
            System.out.println(e);
            Roleservice.saveRole(role);
            restfulResult.setData("保存角色失败");
            return restfulResult;
        }
    }

    /*更新角色和其对应的权限*/
    @RequestMapping("/editRole.action")
    @ResponseBody
    public Object editRole(Role role) {
        System.out.println("更新角色和其对应的权限" + role);
        RestfulResult restfulResult = new RestfulResult();
        try {
            Roleservice.editRole(role);
            restfulResult.setData("编辑角色成功");
            return restfulResult;

        } catch (Exception e) {
            System.out.println(e);
            restfulResult.setData("编辑角色失败");
            return restfulResult;
        }

    }

    /*删除角色*/
    @RequestMapping("/deleterole.action")
    @ResponseBody
    public Object deleterole(@RequestBody JSONObject jsonObject) {
        Long rid = jsonObject.getLong("rid");
        RestfulResult restfulResult = new RestfulResult();
        try {
            Roleservice.deleterolebyid(rid);

            System.out.println("删除角色的rid为" + rid);
            restfulResult.setData("删除角色成功22");
            return restfulResult;

        } catch (Exception e) {
            System.out.println(e);
            restfulResult.setData("删除角色失败22");
            return restfulResult;

        }


    }

    /*获取所有的角色，给角色的下拉列表*/
    @RequestMapping("/getallrole.action")
    @ResponseBody
    public Object getallrole() {
        List<Role> list = Roleservice.getroleList();
        return list;
    }


    /*根据员工的id获取员工对应的角色*/
    @RequestMapping("/getrolebyEmployeeid.action")
    public Object getrolebyEmployeeid(@RequestBody JSONObject jsonObject) {

        Long id = jsonObject.getLong("id");
        List<Long> ridlist = Roleservice.getrolebyeid(id);
        return new AfRestData(ridlist);
    }

}
