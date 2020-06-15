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
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/employee.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/afquery/afquery.js"></script>
</head>
<body>



<%--数据表格的顶部工具栏--最好不要以数组的形式去创建数据表格的顶部工具栏不方便后续因为不方便添加其他的DOM元素如文本框到顶部工具栏上面，
如果分开添加会导致底部的分页工具栏显示不了，且DOM元素和工具栏不在一行了 最好是用html的方式去定义一个工具栏--%>
<div id="tb">
    <%--该Shiro标签表示如果当前用户有 某个权限 才会显示这个Shiro标签中的子元素  相当于Thymeleaf条件判断--%>
    <shiro:hasPermission name="employee:add">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" id="add">添加</a>
    </shiro:hasPermission>
        <shiro:hasPermission name="employee:edit">
            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" id="edit">编辑</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="employee:edit">
            <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" id="delete">离职</a>
        </shiro:hasPermission>
       <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" id="reload">刷新</a>
    <input type="text" name="keyword" style="width: 200px; height: 30px;padding-left: 5px;" id="Searchcontent" >
    <a class="easyui-linkbutton" iconCls="icon-search" id="searchbtn">查询</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"  id="ExcelIn">导入Excel文件</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"  id="ExcelOut">导出Excel文件</a>
        <%--<a href="${pageContext.request.contextPath}/ExcelOut.action" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"  >导出2</a>--%>
</div>


<%--Easyui数据表格--%>
<table id="dg">
</table>


<%--Easyui对话框--%>
<div id="mydialog">
    <form id="employeeform">
<table align="center" style="border-spacing: 0px 10px">
    <tr>
        <td>用户名:</td>
        <td><input type="text"  name="username" class="easyui-validatebox" data-options="required:true" ></td>
    </tr>
       <%--用户的id--隐藏起来 添加操作时  该input的值为空  编辑操作时，或把row中的ID数据回显到这个input中--%>
       <input type="text"  class="employeeid" name="id" style="display: none;">
       <%-- &lt;%&ndash;用户的密码--隐藏起来&ndash;%&gt;
       <input type="text"  class="employeepassword" name="password" style="display: none;">--%>
    <tr>
        <td id="passwordss">密码:</td>
        <td><input type="text"   name="password"  class="easyui-validatebox" id="passwords" ></td>
    </tr>
    <tr>
        <td>手机:</td>
        <td><input type="text"   name="tel" class="easyui-validatebox" ></td>
    </tr>
    <tr>
        <td>邮箱:</td>
        <td><input type="text"  name="email" class="easyui-validatebox"  ></td>
    </tr>
    <tr>
        <td>入职日期:</td>
        <%--class="easyui-datebox" required="required" 表示必需有值--%>
        <td><input type="text"  name="inputtime" class="easyui-datebox" required="required"></td>
    </tr>
    <tr>
        <td>部门:</td>
        <%--这个值对应关联的department字段的id属性--%>
        <td><input id="department"  name="department.id" placeholder="请选择部门"  class="easyui-datebox" required="required"></input></td>
    </tr>
    <tr>
        <td>是否管理员:</td>
        <td><input id="state"    name="admin"  placeholder="是否为管理员"  ></input></td>
    </tr>
    <tr>
        <td>选择角色:</td>
        <td><input id="role"    name="role.rid"  placeholder="请选择角色"  class="easyui-datebox" required="required" ></input></td>
    </tr>

</table>
    </form>
</div>


<%--上传文件的界面（对话框）--%>
<div id="excelUpload">
    <form   method="post" enctype="multipart/form-data" id="uploadFile"  action="${pageContext.request.contextPath}/ExcelImport.action">
        <tabel>
            <tr>
                <td><input type="file" name="file" style="width: 180px; margin-top: 20px; margin-left: 5px;"></td>
                <td><a href="${pageContext.request.contextPath}/downLoadtml.action" id="downloadTml" >下载模板</a></td>
            </tr><br>
            <tr >
              <label style="color:red;font-size: 15px"> 注意:导入的Excel必须按照模板文件来--否则无效</label>
            </tr>
        </tabel>
    </form>
      <%-- <button  class="up" id="uploadbutton">选择文件上传</button>
       <input name="file" type="file" multiple="multiple" class="filedialog" onchange="upload(this)" style="display: none;"><br>--%>
</div>


<%--
<div id="excelUpload">
    <form   method="post" enctype="multipart/form-data" id="uploadFile"  action="${pageContext.request.contextPath}/ExcelImport.action">
        <input type="file" name="file" style="width: 180px; margin-top: 20px; margin-left: 5px;">
           <a href="${pageContext.request.contextPath}/downLoadtml.action" id="downloadTml" >下载模板</a><br>
         <label style="color:red;font-size: 15px"> 注意:导入的Excel必须按照模板文件来--否则无效</label><br>
        <input type="submit" value="提交">
    </form>
</div>
--%>


</body>
<script>
    /*因为我们将大量的js代码都抽离到一个JS文件中了，JS文件无法使用EL表达式从而无法直接获取contextPath，
    故我们在JSP中定义一个contextPath 然后在JS文件中直接引入它*/
    var contextpath="${pageContext.request.contextPath}";

    $("#uploadbutton").click(function () {
        $(".filedialog").click();
    })



    //循环遍历每个文件对象，每个文件对象都调用一次Afupload(文件上传)
    function upload(button) {
        // 取得待上传的文件数组,可能选择了多个文件
        var files = [];

        //button.files表示在文件选择对话框中选中的所有对象
        for(var i = 0; i < button.files.length; i++) {
            var file = button.files[i];
            files.push(file);
            console.log('file: ' + file.name);
        }
        //清楚文件选择对话框的选择（反正文件对象已经存入files数组）
        //以便重新上传
        button.value = '';
        //遍历文件数组中的文件对象--实现并发上传--多文件上传
        //利用每个文件对象都去调用一次AfUploader实现文件上传
        //并不用等待上一个文件上传完成才进行上传下一个，
        //而是立马上传下一个文件
        for(var i=0; i<files.length; i++)
        {

            var file = files[i];
            uploadfile(file);

        }
    }

    //多文件上传
    function uploadfile(file) {
        //图片临时路径
        var  results={};
        results.storePath;
        //文件上传工具类对象
        var up = new AfUploader()
        // 设置上传服务URL(api)
        up.setUploadUrl(contextpath+"/ExcelImport.action");
        //设置文件对象（这里没有用setButton()--注意
        //setButton()-设置关联的file input控件（这个方法内部已经设置了 文件对象file--注意不需要我们再设置了）
        //并且这个方法内部还设置了开始文件上传startUpload( );只要我们选中了文件他就开始文件上传不需要我们手动设置
        //如果直接使用文件选择对话框按钮而
        //不设置关联的Button用-setButton()则文件选择对话的事件为onchange()  而不是onclick()
        //onchange 在元素值改变时触发。 --记录
        //设置文件对象--从方法参数中取得当前上传的file对象(有多个文件要上传)
        up.file=file//因为目前是多文件上传（实质还是每次只上传一个文件）
                    //所以取得文件数组对象中的第一个文件赋值给文件对象


        // 是否允许上传(限制上传的文件类型)
        up.beforeUpload = function() {
            var type =  up.file.type;
            console.log(type);
            if(type != "image/jpeg" && type != "image/png") {
                alert("只支持jpeg和png图片!");
                return  false;
            }
            return true;
        };

        // up.beforeUpload();
        // 开始上传
        up.startUpload();
        //上传完成后的事件处理
        up.processEvent = function(event) {
            if(event == 'progress') {
                Af.log("进度: " + this.progress);
            } else if(event == 'complete') {
                Af.log("完成上传", this.response);
                //把服务器应答返回的JSON数据转为JS对象
                var result = JSON.parse(this.response);
                //提取JS对象的中data字段的url的值
                // var url = result.data.url;
                alert(result.data);
            }
        };
    }

</script>
</html>
