package my.service;

import my.domain.QueryParameter;
import my.domain.Role;
import my.domain.dataGridResult;

import java.util.List;
public interface roleService {

    public  abstract dataGridResult getallRoleinfo(QueryParameter queryParameter);

    public  abstract void saveRole(Role role);
    /*更新角色和其对应的权限*/
    void editRole(Role role);

    void deleterolebyid(Long rid);

    List<Role> getroleList();

    List<Long> getrolebyeid(Long id);
}
