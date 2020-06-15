<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
    <%--这个不用加contextPath注意 --里面包含了Easyui的JS文件和CSS文件--%>
    <%@include file="/static/common/common.jsp"%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/index.js"></script>
</head>
<body class="easyui-layout">

<%--上部--%>
<div data-options="region:'north'" style="height:100px; background: #ec4e00; padding: 20px 20px">
    <img src="${pageContext.request.contextPath}/static/images/main_logo.png" alt="">

     <span style="float: right; color: white;font-size: 18px" >
         <img src="${pageContext.request.contextPath}/static/images/user.png" style="vertical-align: middle; margin-right: 10px;">
             <%--使用shiro标签显示当前用户名--%>
         <shiro:principal property="username"></shiro:principal>
          <%--取消认证 后下一次当前用户必须重新认证）取消认证后会自动跳转到loginUrl（登录认证路径）中
          注意---我们必须在Shiro配置中添加    /logout=logout 该配置  否则取消认证无效
          --%>
           <a href="${pageContext.request.contextPath}/logout" style="font-size: 18px; color: white;text-decoration: none;" >||  退出登录</a>
     </span>

 </div>

<%--下部--%>
<div data-options="region:'south'" style="height:50px; border-bottom: 3px solid #ec4e00">
    <p align="center" style="font-size: 14px">撩课学院</p>
</div>

<%--左部--%>
<div data-options="region:'west',split:true" style="width:200px;">
    <div id="aa" class="easyui-accordion" data-options="fit:true">
        <div title="菜单" data-options="iconCls:'icon-save',selected:true" style="overflow:auto;padding:10px;">
            <!--tree-->
            <ul id="tree"></ul>
        </div>
        <div title="公告" data-options="iconCls:'icon-reload'" style="padding:10px;">
        </div>
    </div>
</div>

<%--中部--%>
<div data-options="region:'center'" style="background:#eee;">
    <!--标签卡-->
    <div id="tabs" style="overflow: hidden">
    </div>
</div>
</body>
<script>
    /*因为我们将大量的js代码都抽离到一个JS文件中了，JS文件无法使用EL表达式从而无法直接获取contextPath，
    故我们在JSP中定义一个contextPath 然后在JS文件中直接引入它*/
    var contextpath="${pageContext.request.contextPath}";
</script>

</html>
