package my.Util;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
/*Restful服务的结果  --字段名要和AfRestData的一致，
因为很多前端的Af框架（如Af.rest,afupload）都要求应答返回的JSON字符串有data 和error字段否则前端框架无效*/
/*我们使用它去取代afrestData  因为在处理Shiro的授权异常时我们要区分当前请求是否是Restful服务*/
public class RestfulResult {
    private Integer error=0;
    private String reason="OK";
    private Object data;
}
