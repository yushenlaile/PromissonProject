package my.service;

import my.domain.Menu;
import my.domain.QueryParameter;
import my.domain.dataGridResult;
import my.Util.RestfulResult;

import java.util.List;

public interface MenuService {

    dataGridResult getMenuList(QueryParameter queryParameter);

    /*获取所有的父菜单*/
    List<Menu> getparentMenuList();

    void addMenu(Menu menu);

    RestfulResult updateMenu(Menu menu);

    void deleteMenu(Long id);

    List<Menu> getTreeData();


}
