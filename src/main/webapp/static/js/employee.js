/*  $(function () {

    })是在<html>标签中的子标签都加载完成后执行的回调函数，并且只会执行一次。*/

/*加载员工数据的表格*/
$(function () {
    $("#dg").datagrid({
        url: "getemployeeList.action",
        columns: [[

            {field: "username", title: "姓名", width: 100, align: "center"},
            {field: "inputtime", title: "入职时间", width: 100, align: "center"},
            {field: "tel", title: "电话", width: 100, align: "center"},
            {
                field: "department", title: "部门", width: 100, align: "center", formatter: function (value, row, index) {
                    //不可以直接加载对象类型字段的数据，必须用formatter去进行格式化数据
                    //value 当前字段的值  -这里当前字段是department 值是一个对象
                    //row  整行的值
                    //有两种写法
                    /*   写法1： if(value.name)
                        {
                         return  value.name;

                        }*/
                    //写法2
                    if (row.department != null) {
                        return row.department.name;
                    }
                }
            },
            {
                field: "state", title: "状态", width: 100, align: "center", formatter: function (value, row, index) {
                    if (row.state) {
                        return "在职";
                    } else {
                        return "<label style='color: red'>离职</label>";
                    }
                }
            },
            {field: "email", title: "邮箱", width: 100, align: "center"},
            {
                field: "admin", title: "是否是管理员", width: 100, align: "center", formatter: function (value, row, index) {
                    console.log(row);
                    if (row.admin) {
                        return "<label style='color: red'>是</label>";
                    } else {
                        return "否";
                    }
                }
            },
        ]],
        fit: true,
        singleSelect: true,/*只能选中一行*/
        fitColumns: true,/*真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。*/
        rownumbers: true,/*显示行号*/
        pagination: true,/*如果为true，则在数据表格控件底部显示分页工具栏。*/
        /*toolbar: [
            {
            text:"添加",
            iconCls:"icon-add",
                handler:function () {  /!*点击该按钮时的回调方法*!/
                    /!*因为我们之前可能加载了数据到表单中--即回显数据到表单故每次调用时要清空表单的数据或添加操作输入的*!/
                    /!*但是呢要选中才能编辑，每次选中时都会重新加载数据到表单所以这个可以不加*!/
                    $("#employeeform").form("clear");
                $("#mydialog").dialog("open");
                $("#mydialog").dialog("setTitle","添加员工");
            }
           },
            {
                text:"编辑",
                iconCls:"icon-edit",
                handler:function () {
                    /!*点击该按钮时的回调方法*!/
                    /!*我们先要确保已经选中了数据*!/
                    /!*获取表格中被选中的行(这个行的数据)*!/
                    var rowdata= $('#dg').datagrid("getSelected");
                    console.log(rowdata);
                    /!*如果还未选中数据*!/
                    if(rowdata==null){
                        $.messager.alert("温馨提示","还未选中数据");
                        return;
                    }
                    else{
                        /!*因为我们之前可能加载了数据到表单中--即回显数据到表单故每次调用时要清空表单的数据或添加操作输入的*!/
                        /!*但是呢要选中才能编辑，每次选中时都会重新加载数据到表单所以这个可以不加*!/
                        $("#employeeform").form("clear");
                        /!*打开对话框*!/
                        $("#mydialog").dialog("open");
                        $("#mydialog").dialog("setTitle","编辑员工信息");
                        /!*回显数据到表单时要把boolean类型数据转为字符型--记录
                      * 如果row中某个字段是 一个对象类型的数据则无法直接使用要提出来，重新设置字段名--记录
                        * *!/
                        /!*表单中没有name为department只有name为department.id的
                        *  故我们要添加一个department.id字段在行数据中
                        * *!/
                        if(rowdata["department"]!=null)
                        {
                            rowdata["department.id"] = rowdata["department"].id;
                        }
                        if(rowdata["admin"])
                        {
                            rowdata["admin"]="true";
                        }
                        else{
                            rowdata["admin"]="false";
                        }
                        /!*加载一个JSON字符串中的数据，或JS对象中的数据到表单form中(即回显数据到表单)
                        （根据同名匹配的规则 字段名与表单input中的name属性去匹配）---记录*!/
                        $("#employeeform").form("load",rowdata);

                    }


                }
            },
            {
                text:"离职",
                iconCls:"icon-remove",
                id:"lizhiss",/!*注意给Easyui组件设置属性时，不仅可以设置Easyui组件的属性，还可以设置html标签的属性，如id*!/
                handler:function () {
                    /!*我们先要确保已经选中了数据*!/
                    /!*获取表格中被选中的行(这个行的数据)*!/
                    var rowdata= $('#dg').datagrid("getSelected");
                    console.log(rowdata);
                    /!*如果还未选中数据*!/
                    if(rowdata==null){
                        $.messager.alert("温馨提示","还未选中数据");
                        return;
                    }
                    /!*提示用户是否确认离职操作*!/
                    $.messager.confirm("确定","是否做离职操作",function (res) {
                         /!*确认时做离职操作*!/
                         if(res)
                         {
                             var req={};
                             req.id=rowdata.id;
                            Af.rest(contextpath+"/setState.action",req,function (data)
                            {   $.messager.alert("温馨提示",data);
                                /!*重新加载表格数据 *!/
                                $('#dg').datagrid("reload");
                           })


                         }
                    })

                }
            },
            {
                text:"刷新",
                iconCls:"icon-reload",
                handler:function () {
                    /!*点击该按钮时的执行的方法*!/
                    /!*清空搜索文本框中的内容*!/
                   $("#Searchcontent").val("");
                   /!*重新加载表格数据,把之前的content参数去掉不要了以防反复使用content参数*!/
                   $("#dg").datagrid("reload",{});
                }
            },

        ],*/
        toolbar: "#tb",
        /*点击DataGrid（数据表格）中的一行时触发的事件*/
        onClickRow: function (rowIndex, rowData) {
            /*rowData表示点击的这一行的数据*/
            /*已经离职的话禁用离职按钮*/
            if (rowData.state == false) {
                $("#delete").linkbutton("disable");

            } else {
                /*在职的话就可以设置为离职*/
                $("#delete").linkbutton("enable");
            }
        }

    });
    /*监听数据表格添加按钮的点击*/
    $("#add").click(function () {
        /*点击该按钮时的回调方法*
        因为我们之前可能加载了数据到表单中--即回显数据到表单故每次调用时要清空表单的数据或添加操作输入的*
        但是呢要选中才能编辑，每次选中时都会重新加载数据到表单所以这个可以不加*/
        $("#employeeform").form("clear");
        $("#mydialog").dialog("open");
        $("#mydialog").dialog("setTitle", "添加员工");

        /*添加时显示密码文本框--*/
        $("#passwords").show();
        $("#passwordss").show();


    });
    /*监听数据表格编辑按钮的点击*/
    $("#edit").click(function () {
        /*点击该按钮时的回调方法*/
        /*我们先要确保已经选中了数据*/
        /*获取数据表格中被选中的行(这个行的数据)*/
        var rowdata = $('#dg').datagrid("getSelected");
        console.log(rowdata);
        /*如果还未选中数据*/
        if (rowdata == null) {
            $.messager.alert("温馨提示", "还未选中数据");
            return;
        } else {
            /*因为我们之前可能加载了数据到表单中--即回显数据到表单故每次调用时要清空表单的数据或添加操作输入的*/
            /*但是呢要选中才能编辑，每次选中时都会重新加载数据到表单所以这个可以不加*/
            $("#employeeform").form("clear");
            /*打开对话框*/
            $("#mydialog").dialog("open");
            $("#mydialog").dialog("setTitle", "编辑员工信息");

            /*编辑时隐藏密码文本框--不进行密码的修改*/
            $("#passwords").hide();
            $("#passwordss").hide();

            /*回显数据到表单时要把boolean类型数据转为字符型--记录
          * 如果row中某个字段是一个对象类型的数据则无法直接使用要提出来，--记录
            * */
            /*表单中没有name为department只有name为department.id的
            *  故我们要添加一个department.id字段在行数据中
            * */
            if (rowdata["department"] != null) {
                rowdata["department.id"] = rowdata["department"].id;
            }
            if (rowdata["admin"]) {
                rowdata["admin"] = "true";
            } else {
                rowdata["admin"] = "false";
            }
            var req = {};
            req.id = rowdata.id;
            /*把当前员工的角色全部回显到表单上的选择角色下拉列表中*/
            Af.rest(contextpath + "/getrolebyEmployeeid.action", req, function (data) {
                console.log(data);
                $("#role").combobox("setValues", data);
            })

            /*加载一个JSON字符串中的数据，或JS对象中的数据到表单form中(即回显数据到表单)
            （根据同名匹配的规则 字段名与表单input中的name属性去匹配）---记录*/
            $("#employeeform").form("load", rowdata);
        }

    });
    /*监听数据表格离职按钮的点击*/
    $("#delete").click(function () {
        /*我们先要确保已经选中了数据*/
        /*获取表格中被选中的行(这个行的数据)*/
        var rowdata = $('#dg').datagrid("getSelected");
        console.log(rowdata);
        /*如果还未选中数据*/
        if (rowdata == null) {
            $.messager.alert("温馨提示", "还未选中数据");
            return;
        }
        /*提示用户是否确认离职操作*/
        $.messager.confirm("确定", "是否做离职操作", function (res) {
            /*确认时做离职操作*/
            if (res) {
                var req = {};
                req.id = rowdata.id;
                Af.rest(contextpath + "/setState.action", req, function (data) {
                    $.messager.alert("温馨提示", data);
                    /*重新加载表格数据 */
                    $('#dg').datagrid("reload");
                })
            }
        })
    })

    /*监听数据表格刷新按钮的点击*/
    $("#reload").click(function () {
        /*点击该按钮时的执行的方法*/
        /*清空搜索文本框中的内容*/
        $("#Searchcontent").val("");
        /*重新加载表格数据,把之前的content参数去掉不要了以防反复使用content参数*/
        $("#dg").datagrid("reload", {});
    })

    /*监听导出按钮的点击*/
    $("#ExcelOut").click(function () {
        alert("导出")
        /* window.open() 就是打开一个新的窗口  但是如果应答返回的数据是流的形式，就不会打开新的窗口，而是在当前窗口进行文件下载*/
        window.open(contextpath + "/ExcelOut.action")
    })
    /*监听导入按钮的点击*/
    $("#ExcelIn").click(function () {
        /* alert( "导入")*/
        $("#uploadFile").form("clear");
        $("#excelUpload").dialog("open");
    })
})

/*对话框*/
$(function () {
    /*执行后mydialog就被转为一个Easyui组件了*/
    $("#mydialog").dialog({
        width: 350,
        height: 400,
        closed: true,/*默认情况下对话框是否关闭*/
        buttons: [{
            text: '保存',
            handler: function () {
                /*区分不同操作的请求url*/
                var id = $(".employeeid").val();
                console.log(id);
                var url;
                /*如果id有值则是更新编辑操作*/
                if (id != null && id.length > 0) {
                    url = contextpath + "/editEmployee.action"
                }
                /*id为null则是添加操作*/
                else {

                    url = contextpath + "/savaEmployee.action";
                }
                /*使用Easyui提供的Ajax方式去提交表单参数，向服务器发送请求
                * 但是它并不会自动将应答返回的JSON字符串向我们之前的ajax一样自动转JSON对象，要我们自己手动去转
                * */
                $("#employeeform").form("submit", {
                    url: url,
                    /*如果我们有额外的表单参数，就放到onSubmit回调方法（在提交之前触发）的formparam参数中 */
                    onSubmit: function (formparam) {
                        /*获取选择角色下拉列表中所有的选中选项的值*/
                        var values = $("#role").combobox("getValues");
                        for (var i = 0; i < values.length; i++) {
                            /*在后端将会把这个表单参数封装到一个集合中*/
                            /*Springmvc将表单参数封装成一个List时前端应该怎么做-这是一个易出错的地方*/
                            formparam["roleList[" + i + "].rid"] = values[i]
                        }
                    },
                    success: function (data) {
                        console.log(data);
                        data = JSON.parse(data);
                        $.messager.alert("温馨提示", data.data);
                        /*重新加载表格数据 */
                        $('#dg').datagrid("reload");
                        /*关闭对话框*/
                        $('#mydialog').dialog("close");
                    }

                })
            }
        }, {
            text: '关闭',
            handler: function () {
                /*关闭对话框*/
                $('#mydialog').dialog("close");
            }
        }]

    })

})

/*下拉列表*/
$(function () {
    /*部门选择的下拉列表*/
    $("#department").combobox({
        width: 150,
        panelHeight: "auto",/*下拉列表选项面版的高度*/
        editable: false,/*用户是否可以编辑下拉列表的选项*/
        url: contextpath + '/getalldepartment.action',
        textField: 'name',/*选项显示的文本是哪个字段*/
        valueField: 'id',/*选项的值是哪个字段*/
        onLoadSuccess: function () {/* 加载完远程中的数据后的回调方法*/
            $("#department").each(function (i) {
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if (targetInput) {
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
            console.log("加载完远程中的数据后的回调方法33333333");
        }


    });
    /*是否为管理员下拉列表*/
    $("#state").combobox({
        width: 150,
        panelHeight: "auto",/*下拉列表选项面版的高度*/
        textField: 'label',/*选项显示的文本是哪个字段*/
        valueField: 'value',/*选项的值是哪个字段*/
        editable: false,/*用户是否可以编辑下拉列表的选项*/
        data: [{  /*下拉列表中选项比较少时不必加载远程数据直接使用data去定义选项文本和值*/
            label: "是", /*下拉列表选项的文本*/
            value: "true",/*下拉列表选项的值*/
        },
            {
                label: "否",
                value: "false",
            },
        ],
        onLoadSuccess: function () {/* 加载完远程中的数据后的回调方法*/
            $("#state").each(function (i) {
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if (targetInput) {
                    $(targetInput).attr("placeholder", "是否是管理员");
                }
            });
            console.log("加载完远程中的数据后的回调方法2222");
        }
    });

    /*选择角色下拉列表*/
    $("#role").combobox({
        width: 200,
        panelHeight: "auto",/*下拉列表选项面版的高度*/
        textField: 'rname',/*选项显示的文本是哪个字段*/
        valueField: 'rid',/*选项的值是哪个字段*/
        editable: false,/*用户是否可以编辑下拉列表的选项*/
        multiple: true,/*是否支持多选*/
        url: contextpath + "/getallrole.action",
        onLoadSuccess: function () {/* 加载完远程中的数据后的回调方法*/
            $("#role").each(function (i) {
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if (targetInput) {
                    // $(targetInput).attr("placeholder", $(this).attr("placeholder"));

                    $(targetInput).attr("placeholder", "请选择角色");
                }
            });
            console.log("加载完远程中的数据后的回调方法2222");
        }
    });


})

/*高级查询--输入条件进行查询*/
$(function () {
    /*点击搜素时执行*/
    $("#searchbtn").click(function () {
        /*获取搜素框中的内容*/
        var content = $("#Searchcontent").val();
        console.log(content);
        /*数据表格的方法---加载数据表格的第一页的所有数据，并指定参数传给后端（并把搜素框中的内容content传给后端）*/
        $("#dg").datagrid("load", {content: content});
    });


    /*<%--上传文件的界面（对话框）--%>*/
    $("#excelUpload").dialog({
        width: 350,
        height: 180,
        title: "导入Excel",
        buttons: [{
            text: '保存',
            handler: function () {
            /*    $("#uploadFile").form("submit", {
                    url: contextpath + "/ExcelImport.action",
                    success: function (data) {
                        data = JSON.parse(data);
                        $.messager.alert("温馨提示", data.data);
                        /!*重新加载表格数据 *!/
                        $('#dg').datagrid("reload");
                        $("#excelUpload").dialog("close");

                    }
                })*/          /*/ExcelImport.action    /TestsimpleFileUpload.action */
             $("#uploadFile").form("submit",{
                    url: contextpath + "/TestsimpleFileUpload.action",
                    success:function (data) {
                        data = JSON.parse(data);
                        $.messager.alert("温馨提示", data.data);
                        $('#dg').datagrid("reload");
                        $("#excelUpload").dialog("close");
                    }
                })

           }
        }, {
            text: '关闭',
            handler: function () {
                $("#excelUpload").dialog("close");
            }
        }],
        closed: true
    })



})