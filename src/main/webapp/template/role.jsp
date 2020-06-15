<%--
  Created by IntelliJ IDEA.
  User: MINI
  Date: 2020-04-08
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%--这个不用加contextPath注意 --里面包含了Easyui的JS文件和CSS文件--%>
    <%@include file="/static/common/common.jsp"%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/role.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/afquery/afquery.js"></script>
    <style>
      #roledialog  #myform  .panel-header{
            height: 25px;


        }
      #roledialog  #myform  .panel-title{

          color: black;
          margin-top: -7px;
      }


    </style>
</head>
<body>
<%--数据表格的顶部工具栏--最好不要以数组的形式去创建数据表格的顶部工具栏不方便后续因为不方便添加其他的DOM元素如文本框到顶部工具栏上面，
如果分开添加会导致底部的分页工具栏显示不了，且DOM元素和工具栏不在一行了 最好是用html的方式去定义一个工具栏--%>
<div id="tb">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" id="add" >添加</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" id="edit">编辑</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" id="delete">删除</a>
</div>

<%--role数据表格--%>
<table  id="roledg"></table>



<%--role对话框--%>
<div id="roledialog">
    <form id="myform">
        <table align="center" style="border-spacing: 20px 30px">
            <input type="hidden" name="id">
            <tr align="center">
                <td>角色编号: <input type="text" name="rnum" class="easyui-validatebox" ></td>
                <td>角色名称: <input type="text" name="rname" class="easyui-validatebox" ></td>
                <%--ROLE的id--隐藏起来 添加操作时  该input的值为空  编辑操作时，或把row中的rid数据回显到这个input中  --%>
                <input type="text"  class="roleid" name="rid" style="display: none;">
            </tr>
            <tr>
                <td><div id="role_data1"></div></td>
                <td><div id="role_data2"></div></td>
            </tr>
        </table>
    </form>
</div>
</body>
<script>
    /*因为我们将大量的js代码都抽离到一个JS文件中了，JS文件无法使用EL表达式从而无法直接获取contextPath，
    故我们在JSP中定义一个contextPath 然后在JS文件中直接引入它*/
    var contextpath="${pageContext.request.contextPath}";
</script>
</html>
