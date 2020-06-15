package my.mapper;

import java.util.List;

import my.domain.Role;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    int deleteByPrimaryKey(Long rid);

    int insert(Role record);

    Role selectByPrimaryKey(Long rid);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    /*  <!--保存角色相关联的权限(向它们的中间表中插入数据-->*/
    void insertRolerelpermisson(@Param("rid") Long rid, @Param("pid") Long pid);
    /*2 删除  权限角色关系表中的数据 根据rid（打破关系）*/
    void deleterolerelpermisson(Long rid);

     /* <!--获取角色id根据员工的ID--->*/
    List<Long> getRoleBYeid(Long id);
}