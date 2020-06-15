/*role数据表格*/
$(function () {
    $("#roledg").datagrid({
        url:contextpath+"/getrolelist.action",
        columns:[[
            {field:"rnum", title:"角色的编号",width:100,align:"center"},
            {field:"rname", title:"角色的名称",width:100,align:"center"},
        ]],
        fit: true,
        singleSelect:true,/*只能选中一行*/
        fitColumns: true,/*真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。*/
        rownumbers: true,/*显示行号*/
        pagination: true,/*如果为true，则在数据表格控件底部显示分页工具栏。*/
       toolbar:"#tb",
    });

     /*对话框中所有权限的数据表格*/
    $("#role_data1").datagrid({
        title:"所有权限",
        width:250,
        height: 250,
        singleSelect:true,/*只能选中一行*/
        fitColumns: true,
        rownumbers: true,/*显示行号*/
        url:contextpath+"/getallpermisson.action",
        columns: [[
            {field:"pname",title:"权限名称",width:50,align:"left"},
        ]],
        /* /*点击DataGrid（数据表格）中的一行时触发的事件*/
        onClickRow:function (rowIndex, rowData) {
            /*rowData表示点击的这一行的数据*/

            /*getRows 这个方法表示获取一个DataGrid组件当前页的全部行的数据*/

            /*获取已选权限数据表格的全部行的数据*/
             var  allrows=  $("#role_data2").datagrid("getRows");

             /*遍历已选权限数据表格的每一行的数据，取出他们的pid字段
             只要有一个与当前点击的这一行的pid字段的值相同就终结-表示已经有该权限了
             * */
             for(var i=0;i<allrows.length;i++)
             {
                var  row=allrows[i];
                if(row.pid==rowData.pid)
                {
                    /*getRowIndex--返回指定行的索引号，该行的参数可以是一行记录或一个ID字段值。*/
                    /*获取这个已有权限行的索引*/
                   var index= $("#role_data2").datagrid("getRowIndex",row);
                   /*让该行成为选中状态*/
                    $("#role_data2").datagrid("selectRow",index);
                     return;
                }
             }

            /*appendRow 这个方法表示给DataGrid组件添加一行数据，新行将被添加到最后的位置*/
            $("#role_data2").datagrid("appendRow",rowData);

        }


    })
    /*对话框中已选权限的数据表格*/
    $("#role_data2").datagrid({
        title:"已选权限",
        width:250,
        height: 250,
        singleSelect:true,/*只能选中一行*/
        fitColumns: true,
        rownumbers: true,/*显示行号*/
        columns: [[
            {field:"pname",title:"权限名称",width:50,align:"left"},
        ]],
        onClickRow:function (rowIndex, rowData) {
            /*rowData表示点击的这一行的数据，rowIndex表示选中行的索引*/

           /*删除当前选中的这一行,根据选中行的索引*/
            $("#role_data2").datagrid("deleteRow",rowIndex);
        }
    })


    /*点击添加按钮的回调方法*/
    $("#add").click(function () {
        /*打开Role对话框（对话框默认关闭）*/
        $('#roledialog').dialog("open");
        $('#roledialog').dialog("setTitle","添加角色");
        /*清除表单数据*/
        $("#myform").form("clear")
           /*清空已经选择的权限*/
          /*加载本地数据，旧的行将被移除。*/
        $("#role_data2").datagrid("loadData",{rows:[]});
    });

    /*点击编辑按钮的回调方法*/
    $("#edit").click(function () {
        /*我们先要确保已经选中了数据*/
        /*获取数据表格中被选中的行(这个行的数据)*/
        var rowdata= $('#roledg').datagrid("getSelected");
        console.log(rowdata);
        /*如果还未选中数据*/
        if(rowdata==null){
            $.messager.alert("温馨提示","还未选中数据");
            return;
        }
        else{
        /*清除表单数据*/
        $("#myform").form("clear")
        /*清空已经选择的权限*/
        /*加载本地数据，旧的行将被移除。*/
        $("#role_data2").datagrid("loadData",{rows:[]});
        /*打开Role对话框（对话框默认关闭）*/
        $('#roledialog').dialog("open");
        $('#roledialog').dialog("setTitle","编辑角色");

        /*加载当前角色的权限  根据当前角色的id*/
            /*返回当前id为role_data2的数据表格组件的属性对象*/
         var options= $("#role_data2").datagrid("options");
            options.url=contextpath+"/getPermissonByRid.action?rid="+rowdata.rid;
            /*数据表格再次发送请求，重新加载数据表格的数据*/
            $("#role_data2").datagrid("load");

            /*加载一个JSON字符串中的数据，或JS对象中的数据到表单form中(即回显数据到表单)
                （根据同名匹配的规则 字段名与表单input中的name属性去匹配）---记录*/
            $("#myform").form("load",rowdata);

    }})

     /*点击删除按钮的回调方法*/
    $("#delete").click(function () {
        /*我们先要确保已经选中了数据*/
        /*获取数据表格中被选中的行(这个行的数据)*/
        var rowdata= $('#roledg').datagrid("getSelected");
        console.log(rowdata);
        /*如果还未选中数据*/
        if(rowdata==null){
            $.messager.alert("温馨提示","请先选中一行数据");
            return;
        }
        else {
            var req={};
            req.rid=rowdata.rid;
              /*删除操作*/
          Af.rest(contextpath+"/deleterole.action",req,function (data) {
              alert(data);
              $('#roledg').datagrid("reload")
          })




        }

    })

})


/*<%--role的添加和编辑对话框--%>*/
$(function () {
    /*执行后mydialog就被转为一个Easyui组件了*/
    $("#roledialog").dialog({
        width:650,
        height:550,
        closed:true,/*默认情况下对话框是否关闭*/
        buttons:[{
            text:'保存',
            handler:function()
            {
                /*区分不同操作的请求url*/
                var id= $(".roleid").val();
                console.log(id);
                var url;
                /*如果id有值则是编辑操作*/
                if(id!=null&&id.length>0)
                {
                    url=contextpath+"/editRole.action"
                }
                /*id为null则是添加操作*/
                else {
                    url=contextpath+"/saveRole.action";
                }

                /*使用Easyui提供的Ajax方式去提交表单参数，向服务器发送请求
                  * 但是它并不会自动将应答返回的JSON字符串向我们之前的ajax一样自动转JSON对象，要我们自己手动去转
                  * */
                $("#myform").form("submit",{
                    url:url,
                    /*如果我们有额外的表单参数，就放到onSubmit回调方法（在提交之前触发）的formparam参数中 */
                    onSubmit:function(formparam){
                        /*获取已选权限数据表格的全部行的数据*/
                        var  allrows=  $("#role_data2").datagrid("getRows");
                        for(var i=0;i<allrows.length;i++)
                        {
                            var  row=allrows[i];
                            /*在后端将会把这个表单参数封装到一个集合中*/
                            /*Springmvc将表单参数封装成一个List时前端应该怎么做-这是一个易出错的地方*/
                            formparam["permissonList["+i+"].pid"]=row.pid
                            formparam["permissonList["+i+"].pname"]=row.pname
                        }
                    },
                    success:function (data) { /*应答返回数据时（Ajax方式表单提交成功）的回调方法*/
                        console.log(data);
                        data=JSON.parse(data);
                        $.messager.alert("温馨提示",data.data);
                        $('#roledg').datagrid("reload")
                        $('#roledialog').dialog("close");
                    }
                })
            }
        },{
            text:'关闭',
            handler:function(){
                /*关闭对话框*/
                $('#roledialog').dialog("close");
            }
        }]

    })

})