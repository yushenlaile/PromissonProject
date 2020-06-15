$(function () {
    /*选项卡*/
    $("#tabs").tabs({
        fit:true,/*让选项卡铺满父容器*/
    })

    /*树*/
    $('#tree').tree({
        url:contextpath+'/GetTreeData.action',
        lines:true,
        /*点击tree中的子节点时，添加选项卡面板或者选中一个选项卡面板*/
        /*tree点击事件,node点击的子节点*/
        /*   alert(node.text);  // 在用户点击的时候提示*/
        /*先判断选项卡中是否已经有该选项卡面板--根据选项的名字  -返回值是boolean*/
        onSelect: function(node){
            /*在添加之前, 做判断  判断这个选项卡面板是否存在 */
            var exists =   $("#tabs").tabs("exists",node.text);
            if(exists){
                /*存在,就让它选中*/
                $("#tabs").tabs("select",node.text);
            }else {
                if (node.url !=''&& node.url !=null){
                    /*如果不存在 ,添加新标签（新的选项卡面板）*/
                    $("#tabs").tabs("add",{
                        title:node.text,
                        /*选项卡面板的内容是 其他窗口<iframe>*/
                        /*href:node.attributes.url,*/  /*href  引入的是body当中*/
                        content:"<iframe src="+node.url+" frameborder='0' width='100%' height='100%'></iframe>",
                        closable:true
                    })
                }
            }
        },
        /*加载完远程中的数据后---立即选中第一个节点中的第一个子节点--记录*/
           onLoadSuccess: function (node, data) {
            console.log(data[0].children[0].id);
            if (data.length > 0) {
                //找到第一个元素
                var n = $('#tree').tree('find', data[0].children[0].id);
                //调用选中事件
                $('#tree').tree('select', n.target);
            }
        }
    });
});