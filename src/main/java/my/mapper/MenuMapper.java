package my.mapper;

import java.util.List;
import my.domain.Menu;
import my.domain.QueryParameter;

public interface MenuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Menu menu);

    Menu selectByPrimaryKey(Long id);

    List<Menu> selectAll();

    int updateByPrimaryKey(Menu record);

    /* 根据 选择的父菜单的id 去查询选择的父菜单（查询选择的父菜单-它的父菜单id）*/
    Long selectParentid(Long id);

    /*删除一个菜单的所有子菜单*/
    int deletezicaidan(Long id);

    List<Menu> getTreeData();


    /*获取一个菜单的所有的子菜单*/
    List<Menu> getziCaidan(Long id);

    /*   *//*获取所有的菜单*//*
    List<Menu> getMenuList(QueryParameter queryParameter);*/

}