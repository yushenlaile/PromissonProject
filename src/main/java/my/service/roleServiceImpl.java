package my.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import my.domain.*;
import my.mapper.RoleMapper;
import my.mapper.employeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class roleServiceImpl implements roleService {
   @Autowired
   public RoleMapper roleMapper;

   /*获取所有的角色信息并封装成dataGridResult*/
    @Override
    public dataGridResult getallRoleinfo(QueryParameter queryParameter) {
        System.out.println("进入Role业务层");
        /*分页查询--使用分页插件*/
        Page<Role> page = PageHelper.startPage(queryParameter.page,queryParameter.rows);
        List<Role> list=  roleMapper.selectAll();
        for (Role role:list)
        {
            System.out.println(role);
        }

        /*把查询到的数据封装成datagridresult  因为DataGrid组件对应答返回的JSON字符串的字段名有要求*/
        dataGridResult GridResult=new dataGridResult();
        GridResult.setRows(list);
        GridResult.setTotal(page.getTotal());

        return GridResult;
    }

    /*保存角色和角色相关联的权限*/
    @Override
    public void saveRole(Role role) {
        /*1 保存角色*/
        roleMapper.insert(role);
        /*2 保存角色相关联的权限(可能有多个) */
          for(Permisson permisson:role.permissonList)
          {
              roleMapper.insertRolerelpermisson(role.getRid(),permisson.getPid());
          }
    }

    /* 更新角色和其对应的权限*/
    @Override
    public void editRole(Role role)
    {   /*1 更新角色根据角色的id*/
        roleMapper.updateByPrimaryKey(role);
        /*2 删除  权限角色关系表中的数据根据rid （打破关系）*/
        roleMapper.deleterolerelpermisson(role.getRid());


        /*3重新保存角色相关联的权限(可能有多个)*/
        for(Permisson permisson:role.permissonList)
        {
            roleMapper.insertRolerelpermisson(role.getRid(),permisson.getPid());
        }

    }

    /*删除角色，和删除角色关联的权限关系 （删除关系表中的数据）*/
    @Override
    public void deleterolebyid(Long rid) {
       /* 1  先删除权限角色关系表中的数据  根据rid  (即删除角色关联的权限关系)*/
        roleMapper.deleterolerelpermisson(rid);

        /*2 再删除角色根据rid */
        roleMapper.deleteByPrimaryKey(rid);
    }


    /*获取所有的角色，给角色的下拉列表*/
    @Override
    public List<Role> getroleList() {

        List<Role> list=  roleMapper.selectAll();
        for (Role role:list)
        {
            System.out.println(role);
        }
        return list;
    }

    /*根据员工的id获取员工对应的角色id  去员工角色关系表中查*/
    @Override
    public List<Long> getrolebyeid(Long id) {

        List<Long>list= roleMapper.getRoleBYeid(id);
        return list;
    }


}
