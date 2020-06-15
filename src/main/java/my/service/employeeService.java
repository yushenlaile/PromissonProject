package my.service;

import my.domain.QueryParameter;
import my.domain.dataGridResult;
import my.domain.employee;

import java.util.ArrayList;
import java.util.List;


public interface employeeService {

    /*查询所有的员工*/
    public abstract dataGridResult getallemployee(QueryParameter queryParameter);

    /*添加员工和员工之间的关系*/
    public  abstract  void  addEmployee(employee employee);

    /*仅仅添加员工*/
    public  abstract  void  OnlyaddEmployee(employee employee);

    /*编辑员工*/
    public  abstract  void  EditEmployee(employee employee);

    public   abstract  void  updateState(Integer id);

    /*根据用户名获取用户*/
    employee getEmployeeWithUsername(String usernname);

    /*查询当前用户拥有的角色 根据当前用户身份信息 id*/
    List<String> getRolesByemployeeId(Long id);

    /*查询当前用户拥有的权限 根据当前用户身份信息 id*/
    List<String> getPermissonsByemployeeId(Long id);

   /*根据部门名字获取部门id*/
    public  abstract  Long  getDepartmentid(String name);



}
