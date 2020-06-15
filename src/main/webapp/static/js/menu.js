$(function () {
    /*--------------数据表格-----------*/
    $("#menu_datagrid").datagrid({
        url: contextpath + "/MenuList.action",
        columns: [[
            {field: 'text', title: "名称", width: 1, align: 'center'},
            {field: 'url', title: "跳转地址", width: 1, align: 'center'},
            {
                field: 'parent', title: "父菜单", width: 1, align: 'center', formatter: function (value, row, index) {
                    //value 当前字段的值  -这里当前字段是parent   值是一个对象

                    /*它的父节点是否有值，有值就显示父节点的文本，没有就显示''*/
                    if (value != null) {
                        return value.text;
                    } else {
                        return '';
                    }
                }
            }
        ]],
        striped: true,
        fit: true,
        singleSelect: true,/*只能选中一行*/
        fitColumns: true,/*真正的自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动。*/
        rownumbers: true,/*显示行号*/
        pagination: true,/*如果为true，则在数据表格控件底部显示分页工具栏。*/
        toolbar: '#menu_toolbar'
    });

    /*对话框*/
    $("#menu_dialog").dialog({
        width: 300,
        height: 300,
        closed: true,/*默认情况下对话框被关闭*/
        buttons:[{text:"保存",
            handler:function ()
            {
                /*区分不同操作的请求url*/
                var id= $("#menuid").val();
                console.log(id);
                var url;
                /*如果id有值则是更新编辑操作*/
                if(id!=null&&id.length>0)
                {
                    var  parenmenuid=$("[name='parent.id']").val();
                    /*不可以设置自己为父菜单-即父菜单ID不能与当前菜单ID一直*/
                    if(id==parenmenuid)
                    {

                        $.messager.alert("温馨提示","不可以设置自己为父菜单");
                       return;
                    }
                    url=contextpath+"/editMenu.action";
                }
                /*id为null则是添加操作*/
                else {

                    url=contextpath+"/savaMenu.action";
                }

                /*使用Easyui提供的Ajax方式去提交表单参数，向服务器发送请求
                 * 但是它并不会自动将应答返回的JSON字符串向我们之前的ajax一样自动转JSON对象，要我们自己手动去转
                 * */
                $("#menu_form").form("submit",{
                    url:url,
                    /*如果我们有额外的表单参数，就放到onSubmit回调方法（在提交之前触发）的formparam参数中 */
                   /* onSubmit:function(formparam){
                        /!*获取选择角色下拉列表中所有的选中选项的值*!/
                        var values=$("#role").combobox("getValues");
                        for(var i=0;i<values.length;i++)
                        {
                            /!*在后端将会把这个表单参数封装到一个集合中*!/
                            /!*Springmvc将表单参数封装成一个List时前端应该怎么做-这是一个易出错的地方*!/
                            formparam["roleList["+i+"].rid"]= values[i]
                        }
                    },*/
                    success:function (data) {
                        console.log(data);
                        data=JSON.parse(data);
                        $.messager.alert("温馨提示",data.data);
                        /*重新加载表格数据 */
                        $('#menu_datagrid').datagrid("reload");
                        /*重新加载下拉列表的数据*/
                        $("#parenmenu").combobox("reload");
                        /*关闭对话框*/
                        $('#menu_dialog').dialog("close");
                    }

                })


            }
            },
            {text:"关闭",
                handler:function ()
                {
                    $("#menu_dialog").dialog("close");
                }
            },

           ]
    });

    /*添加按钮的点击回调方法*/
    $("#add").click(function () {
        /*点击该按钮时的回调方法*
        因为我们之前可能加载了数据到表单中--即回显数据到表单---故每次调用时要清空表单中的数据或添加操作输入的数据
        但是呢要选中才能编辑，每次选中时都会重新加载数据到表单所以这个可以不加*/
        $("#menu_form").form("clear");
        $("#menu_dialog").dialog("open");
        $("#menu_dialog").dialog("setTitle","添加菜单");
    });

      /*编辑按钮的点击回调方法*/
     $("#edit").click(function () {
         /*点击该按钮时的回调方法*/
         /*我们先要确保已经选中了数据*/
         /*获取数据表格中被选中的行(这个行的数据)*/
         var rowdata= $('#menu_datagrid').datagrid("getSelected");
         console.log(rowdata);

         if(rowdata==null){
             $.messager.alert("温馨提示","还未选中数据");
             return;
         }
         else{
             /*因为我们之前可能加载了数据到表单中--即回显数据到表单---故每次调用时要清空表单中的数据或添加操作输入的数据*/
             $("#menu_form").form("clear");
             $("#menu_dialog").dialog("open");
             $("#menu_dialog").dialog("setTitle","编辑菜单");
             /*如果row中某个字段是一个对象类型的数据则无法直接使用要提出来，新设置一个字段，--记录*/
             /*如下*/
             if(rowdata["parent"]!=null)
             {
                 rowdata["parent.id"] = rowdata["parent"].id;
             }
             /*如果没有父菜单 就显示placeholder --即显示请选择父菜单*/
             if(rowdata["parent"]==null)
             {
                 $("#parenmenu").each(function(i){
                     var span = $(this).siblings("span")[i];
                     var targetInput = $(span).find("input:first");
                     if(targetInput){
                         $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                     }
                 });
             }
             /*加载一个JSON字符串中的数据，或JS对象中的数据到表单form中(即回显数据到表单)
            （根据同名匹配的规则 字段名与表单input中的name属性去匹配）---记录*/
             $("#menu_form").form("load",rowdata);

         }
     })

     /*删除按钮的点击回调方法*/
     /*删除一个菜单，必须先把它的子菜单删除了  */
      $("#del").click(function () {
          /*点击该按钮时的回调方法*/
          /*我们先要确保已经选中了数据*/
          /*获取数据表格中被选中的行(这个行的数据)*/
          var rowdata= $('#menu_datagrid').datagrid("getSelected");
          console.log(rowdata);
          if(rowdata==null){
              $.messager.alert("温馨提示","还未选中数据");
              return;
          }
          else {
              /*提示用户是否确认离职操作*/
              $.messager.confirm("确定","是否做删除操作",function (res) {
                  /*确认时做离职操作*/
                  if(res)
                  {
                      var req={};
                      req.id=rowdata.id;
                      Af.rest(contextpath+"/deleteMenu.action",req,function (data)
                      {   $.messager.alert("温馨提示",data);
                          /*重新加载表格数据 */
                          $('#menu_datagrid').datagrid("reload");
                          /*重新加载下拉列表的数据*/
                          $("#parenmenu").combobox("reload")
                      })
                  }
              })

          }

      })


    /*选择父菜单 下拉列表 */
    $("#parenmenu").combobox({
        width:160,
        panelHeight:"auto",/*下拉列表选项面版的高度*/
        editable: false,/*用户是否可以编辑下拉列表的选项*/
        url:contextpath+'/parentMenuList.action',
        textField: 'text',/*选项显示的文本是哪个字段*/
        valueField: 'id',/*选项的值是哪个字段*/
        onLoadSuccess:function () {/* 加载完远程中的数据后的回调方法*/
            $("#parenmenu").each(function(i){
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
            console.log("加载完远程中的数据后的回调方法33333333");
        }

    })

});