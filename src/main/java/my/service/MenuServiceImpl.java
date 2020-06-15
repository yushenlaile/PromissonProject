package my.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import my.domain.Menu;
import my.domain.QueryParameter;
import my.domain.dataGridResult;
import my.mapper.MenuMapper;
import my.Util.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    public MenuMapper menuMapper;

    /*获取所有的菜单*/
    @Override
    public dataGridResult getMenuList(QueryParameter queryParameter) {
        System.out.println("进入Menu业务层");
        /*分页查询--使用分页插件*/
        Page<Menu> page = PageHelper.startPage(queryParameter.page, queryParameter.rows);
        List<Menu> list = menuMapper.selectAll();
        for (Menu menu : list) {
            System.out.println(menu);
        }
        /*把查询到的数据封装成datagridresult  因为DataGrid组件对应答返回的JSON字符串的字段名有要求*/
        dataGridResult menuResult = new dataGridResult();
        menuResult.setTotal(page.getTotal());/*总记录数*/
        menuResult.setRows(list);
        return menuResult;
    }

    /*获取所有的父菜单  --相当于获取所有的菜单*/
    @Override
    public List<Menu> getparentMenuList() {
        return menuMapper.selectAll();
    }

    /*添加菜单*/
    @Override
    public void addMenu(Menu menu) {
        menuMapper.insert(menu);

    }

    /*更新菜单*/
    @Override
    public RestfulResult updateMenu(Menu menu) {
        /*先判断 选择的父菜单是否是自己的子菜单*/
        /*自己的子菜单是不可以做自己的父菜单的*/

        /*1 先取出  选择的父菜单的id*/
        Long id = menu.getParent().getId();

        /*2 根据 选择的父菜单的id 去查询选择的父菜单（查询选择的父菜单-它的父菜单id）*/
        Long panrenid = menuMapper.selectParentid(id);
        RestfulResult restfulResult = new RestfulResult();
        /*3 看当前要编辑的菜单是否是选择的父菜单的父菜单（自己的子菜单是不可以做自己的父菜单的）*/
          if(menu.getId()==panrenid)
          {  restfulResult.setData("不能设置自己的子菜单做自己的父菜单");
              return  restfulResult;
          }
          else{
              try {
                  menuMapper.updateByPrimaryKey(menu);
                  restfulResult.setData("编辑菜单成功");
                  return  restfulResult;
              }catch (Exception e){
                  System.out.println(e);
                  restfulResult.setData("编辑菜单失败");
                  return  restfulResult;
              }


          }

    }

    /*刪除菜单*/
    @Override
    public void deleteMenu(Long id) {
        /*1 删除一个菜单，必须先把它的子菜单删除了  （因为有外键的约束） */
        menuMapper.deletezicaidan(id);
        /* 2 再删除 这个菜单*/
        menuMapper.deleteByPrimaryKey(id);
    }

    /*获取树 tree的数据*/
    @Override
    public List<Menu> getTreeData() {
        return menuMapper.getTreeData();
    }
}
