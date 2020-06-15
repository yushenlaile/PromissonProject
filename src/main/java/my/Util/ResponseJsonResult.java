package my.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseJsonResult {

    public  static void  responseJsonResult( HttpServletResponse response,Object msg) throws IOException {
        JSONObject json = new JSONObject(true);
        json.put("error", 0);
        json.put("reason", "OK");
        json.put("data", msg);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        response.getWriter().print(JSON.toJSONString(json, SerializerFeature.PrettyFormat));
    }


}
