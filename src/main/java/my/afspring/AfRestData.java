package my.afspring;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

//这是利用自定义VIEW生成的一个自定义Rest应答
/*利用自定义VIEW生成的一个自定义Rest应答  -AfRestData
就可以不用设置@ResponseBody注解了，因为我们不必让Spring框架去把处理方法返回值自动转为JSON字符串了
，在自定义VIEW中我们自己会处理应答*/

public class AfRestData implements AfRestView
{

	/// 要发送给客户端的数据信息--记录
	Object data;

	public AfRestData(Object data)
	{
		this.data = data;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject json = new JSONObject(true);
		json.put("error", 0);
		json.put("reason", "OK");
		if (data != null)
		{ // 如果data本身就是 JSONObject 或 JSONArray
			if (data instanceof JSON)
				json.put("data", data);
			else

				// 如果是POJO对象、就直接把它转为JSON对象，放在data字段中
				
				//如果是String 类型就把它转为一个Object类型值   放在data字段中
				//一个不符合JSON格式的字符串无法直接转为JSON  只能转为一个Object值放在某个字段中 无论是用的哪种JSON库--记录
				json.put("data", JSON.toJSON(data));
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print(JSON.toJSONString(json, SerializerFeature.PrettyFormat));
	}

}
