package my.web.Filter;

import my.Util.ResponseJsonResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*FormAuthenticationFilter--表单认证过滤器--（它用来监听认证是否成功）*/
public class FormFilter extends FormAuthenticationFilter {

    /*当认证成功时调用*/
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse resp) throws Exception {
        HttpServletResponse response= (HttpServletResponse) resp;
        //当认证成功时 我们直接应答返回信息给客户端即可
        System.out.println("认证成功");
        ResponseJsonResult.responseJsonResult(response,"认证成功");
        return false;//return false之后就不会往下走继续调用其他过滤器了
    }
    /*当认证失败时调用*/
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse resp) {
        HttpServletResponse response= (HttpServletResponse) resp;
        //当认证失败时 我们直接应答返回信息给客户端即可
        System.out.println("认证失败");
        if(e!=null)
        {
            /*获取异常的名字*/
          String exceptionName= e.getClass().getName();
            System.out.println(exceptionName);
          //没有对应账号
          if(exceptionName.equals(UnknownAccountException.class.getName()))
          {
              try {
                  ResponseJsonResult.responseJsonResult(response,"认证失败--没有对应账号");
              } catch (IOException ex) {
                  ex.printStackTrace();
              }

          }
          /*密码错误*/
          else  if(IncorrectCredentialsException.class.getName().equals(exceptionName))
          {
              try {
                  ResponseJsonResult.responseJsonResult(response,"认证失败--密码错误");
              } catch (IOException ex) {
                  ex.printStackTrace();
              }

          }
          /*未知异常*/
          else {
              try {
                  ResponseJsonResult.responseJsonResult(response,"认证失败--出现未知异常");
              } catch (IOException ex) {
                  System.out.println(ex);
                  ex.printStackTrace();
              }
          }
        }


        return false;//return false之后就不会往下走继续调用其他过滤器了
    }
}
