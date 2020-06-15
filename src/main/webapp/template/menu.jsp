<%--
  Created by IntelliJ IDEA.
  User: MINI
  Date: 2020-04-08
  Time: 20:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%--这个不用加contextPath注意 --里面包含了Easyui的JS文件和CSS文件--%>
    <%@include file="/static/common/common.jsp"%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/menu.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/afquery/afquery.js"></script>
</head>
<body>
<!-- 数据表格的顶部工具栏按钮-->
<div id="menu_toolbar">
    <div>
        <a class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add">新增</a>
        <a class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="edit">编辑</a>
        <a class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del">刪除</a>
        <a class="easyui-linkbutton" iconCls="icon-reload" plain="true" id="reload">刷新</a>
    </div>
</div>
<!-- 数据表格 -->
<table id="menu_datagrid">
    <thead>
    <tr>
        <th>名称</th>
        <th>url</th>
        <th>父菜单</th>
    </tr>
    </thead>
</table>

<%--对话框--%>
<div id="menu_dialog">
    <form id="menu_form" method="post">
        <table align="center" style="margin-top: 15px;">
            <input type="hidden" name="id" id="menuid">
            <tr>
                <td>名称</td>
                <td><input type="text" name="text"></td>
            </tr>
            <tr>
                <td>url</td>
                <td><input type="text" name="url"></td>
            </tr>
            <tr>
                <td>父菜单</td>
                <td><input type="text" name="parent.id" id="parenmenu" class="easyui-combobox" placeholder="请选择父菜单"/></td>
            </tr>
        </table>
    </form>
</div>

<%--<!-- 对话框保存取消按钮 -->
&lt;%&ndash;对话框的底部按钮&ndash;%&gt;
<div id="menu_dialog_bt">
    <a class="easyui-linkbutton" iconCls="icon-save" plain="true" data-cmd="save">保存</a>
    <a class="easyui-linkbutton" iconCls="icon-cancel" plain="true" data-cmd="cancel">取消</a>
</div>--%>

<script>
    /*因为我们将大量的js代码都抽离到一个JS文件中了，JS文件无法使用EL表达式从而无法直接获取contextPath，
  故我们在JSP中定义一个contextPath 然后在JS文件中直接引入它*/
    var contextpath="${pageContext.request.contextPath}";
</script>
</html>
