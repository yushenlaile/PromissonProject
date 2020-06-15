package my.mapper;

import java.util.List;

import my.domain.QueryParameter;
import my.domain.employee;
import org.apache.ibatis.annotations.Param;

public interface employeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(employee record);

    employee selectByPrimaryKey(Long id);

    /*查询所有的员工并关联部门表*/
    List<employee> selectAllemployee(QueryParameter queryParameter);

    int updateByPrimaryKey(employee record);

    public void updateEmployeeState(Integer id);

     /* 保存员工与角色的关系*/
    /*保存  员工相关联的角色 到员工角色关系表中(可能有多个)*/
    void savemployeeRelrole(@Param("id") Long id, @Param("rid") Long rid);

    /*<!--删除   员工角色关系表中的数据（删除关系根据员工的 id）-->*/
    void deleteemployeerelrole(Long id);
    /* 根据用户名获取用户*/
    employee getEmployeeByUsername(String usernname);

    /*根据员工id去查询它拥有的角色名*/
    List<String> getRolesByemployeeId(Long id);

    /*查询当前用户拥有的权限资源名称 根据当前用户身份信息*/
    List<String> getPermissonsByemployeeId(Long id);
    /*根据部门名字获取部门id*/
    Long getDepartmentid(String name);
}