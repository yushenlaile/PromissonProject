package my.web.Realms;

import my.domain.employee;
import my.service.employeeService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Employeerealms extends AuthorizingRealm {
    /*注入业务层*/
    @Autowired
    public my.service.employeeService employeeService;

    /*授权
    * 在Web项目中什么时候会调用该doGetAuthorizationInfo()-授权这个方法？
    * 1 当我们访问的控制器处理方法上有授权注解-就会调用该方法
    * 2 当页面中有Shiro的授权标签时，也会调用该方法 （每有一个Shiro的授权标签就会调用一次该方法）
    * -注意是Shiro授权标签，而不是其他的Shiro标签
    * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("-----------------授权调用----------------");
        /*1 获取当前用户身份信息*/
        employee employee = (employee) principalCollection.getPrimaryPrincipal();

        /*2 根据当前用户身份信息去 数据库中获取授权信息（权限信息） 如角色和角色对应权限资源名称*/
        /*当前用户拥有的角色*/
        List<String> roles = new ArrayList();
        /*当前用户拥有的权限资源名称*/
        List<String> permissons = new ArrayList();
        /*判断当前用户是不是管理员，是的话就拥有全部的权限，不是的话就去数据库中查询当前用户拥有的角色和权限资源名称*/
          if(employee.getAdmin())
          {
      /*  *:*表示对任意资源有任意的操作权限---  超级权限(即拥有素有的权限）  */
              permissons.add("*:*");
              System.out.println("当前用户是管理员拥有全部的权限");
          }
          else {
              /*到数据库中查询当前用户拥有的角色 根据当前用户身份信息*/
              roles=employeeService.getRolesByemployeeId(employee.getId());
              System.out.println("当前用户拥有的角色:"+roles);
              /*到数据库中查询当前用户拥有的权限资源名称 根据当前用户身份信息*/
              permissons= employeeService.getPermissonsByemployeeId(employee.getId());
              System.out.println("当前用户拥有的权限资源名称:"+permissons);
          }

        /*把当前用户拥有的角色和权限资源名称添加到授权信息中--记录*/
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissons);

        return simpleAuthorizationInfo;
    }



    /*认证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken Token) throws AuthenticationException {
        System.out.println("-----------来到了认证----------------");

      /*1 获取Token中的身份信息*/
        String usernname = (String) Token.getPrincipal();
        System.out.println("身份信息："+usernname);
        /*默认情况下 认证时表单参数中的  username字段的值，password字段的值  会被自动封装到Token令牌中
          故以后这个认证字段的名不要变 否则无法成功自动封装到Token令牌中*/
       /*注意 认证必须是一个post请求  请求参数要求是表单参数不可以是Json字符串 否则无法将数据自动封装到Token令牌中*/

        /*2 根据身份信息从数据库中获取对应的认证信息  (去数据库中查询有没有当前用户)*/
        employee employee = employeeService.getEmployeeWithUsername(usernname);
        System.out.println(employee);
        if(employee==null)
        {
            return null;
        }

      /*3 为true 表示有认证信息--有当前用户
       我们就自己创建一个认证信息返回给认证器*/
       /*参数 1 当前用户主体  2  正确的密码 ，3 严 4 当前Realms名*/
        /*认证成功时会自动把  当前用户主体放到Shiro当中的会话管理中Session Manager --记录*/
        SimpleAuthenticationInfo Info = new SimpleAuthenticationInfo(employee,
                employee.getPassword(),
                ByteSource.Util.bytes("xia"),//md5算法的严  --必须要有这个参数
                this.getName());
        return Info;

    }
}
