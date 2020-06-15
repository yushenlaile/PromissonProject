package my.Util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    /* ThreadLocal是本地线程变量  我们可以往里面储存数据，也可以从里面取出数据*/
    public static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal();

    /*从本地线程变量 里面取出数据*/
    public static HttpServletRequest getRequest() {
        return threadLocal.get();
    }
    /*往本地线程变量 里面储存数据*/
    public static void SetRequest(HttpServletRequest request) {
        threadLocal.set(request);
    }

}
