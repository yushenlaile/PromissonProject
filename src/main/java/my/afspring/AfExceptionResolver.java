package my.afspring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

// 当注册多个 异常处理器时，@Order决定顺序，Order越低的越先执行



//自定义异常处理器---可以捕获Spring框架中抛出的异常
@Order(-100)
public class AfExceptionResolver implements HandlerExceptionResolver
{

	@Override
	public ModelAndView resolveException(HttpServletRequest request
			, HttpServletResponse response
			, Object handler
			, Exception exception)
	{

		//String uri = request.getRequestURI();
		//if(uri.endsWith(".do"))
		//{
			// REST 出错处理
		//	Map<String,Object> model = new HashMap<>();
		//	View view = new AfRestError(exception);
		//	System.err.println("访问" + request.getRequestURI() + " 发生错误, 错误信息:" + exception.getMessage());
		
		//	//把model中的数据放入自定义的VIEW的render方法中去
		//	return new ModelAndView(view, model);
		//}
		
		/*else
		{
			// MVC 异常处理
			Map<String,Object> model = new HashMap<>();
			model.put("message", exception.getMessage());
				
			StringWriter stringWriter = new StringWriter();
			exception.printStackTrace(new PrintWriter(stringWriter));
			model.put("detail", stringWriter.toString());
				
			return new ModelAndView("error", model);
		}	*/
		 return null; // 返回null表示此Resolver未能处理该异常，则继续按默认方式处理
	}

}
