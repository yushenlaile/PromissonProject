package my.Aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.Util.RequestUtil;
import my.domain.Systemlog;
import my.mapper.SystemlogMapper;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/*系统切面类  已经交给Spring框架去管理了  故可以进行依赖注入*/
public class SystemAspect {
  @Autowired
  public SystemlogMapper systemlogMapper;
    public  SystemAspect()
    {
        System.out.println("----------------系统切面类---------------");

    }
    /*通知--即要增强的内容*/
    public void Writelog(JoinPoint joinPoint) throws JsonProcessingException {
        /*Joinpoint 即连接点(一个类中可以被增强的方法称为连接点--基本上一个类中所有的方法都可以称为连接点)
        --我们可以从连接点到里面获取被切入方法的名字，被切入方法的参数*/
        System.out.println("记录到日志");

        Systemlog systemlog = new Systemlog();
        /*1 设置操作的时间*/
        systemlog.setOptime(new Date());

      /*2  设置ip地址 从HttpServletRequest 中可以获取ip地址
      (但是这里无法直接获取HttpServletRequest对象，我们可以从拦截器中去获取)*/
        HttpServletRequest request = RequestUtil.getRequest();
        if (request != null) {

            String IpAddr = request.getRemoteAddr();
            /*本地域名localhost 对应的ip地址为0:0:0:0:0:0:0:1*/
            /*我们也可以使用本地ip地址127.0.0.1  */
            System.out.println(IpAddr);
            systemlog.setIp(IpAddr);
        }
        /*3 设置方法的名称*/

        /*获取被切入方法的全路径*/
        String methodurl = joinPoint.getTarget().getClass().getName();
        System.out.println("切入方法的全路径:" + methodurl);
        /*获取方法的签名*/
        String Signature = joinPoint.getSignature().getName();
        System.out.println("方法的签名:" + Signature);
        /*方法名称*/
        String methodName = methodurl + "." + Signature;
        systemlog.setFunction(methodName);

        /*4 设置方法的参数*/
        /*joinPoint.getArgs()获取方法的参数是返回值是一个Object数组*/
                                           /*将Object数组转为JSON字符串*/
        String params = new ObjectMapper().writeValueAsString(joinPoint.getArgs());
        systemlog.setParams(params);

        systemlogMapper.insert(systemlog);
    }
}
