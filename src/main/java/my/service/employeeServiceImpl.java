package my.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import my.domain.QueryParameter;
import my.domain.Role;
import my.domain.dataGridResult;
import my.domain.employee;

import my.mapper.employeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
//@Service//该类对象交给Spring框架去管理（它实现的（Service层）接口也会交给Spring框架去管理）-记录
@Transactional
//事务已经全部交给Spring框架去管理配置该注解，Spring框架会帮我们自动提交事务
//DML操作必须提交事务才会生效  故必须使用@Transactional注解（故所有的Service层都加一个这个注解以防DML操作更新数据库失败）-记录
public class employeeServiceImpl implements employeeService {

    /*Mapper接口已经都交给Spring框架去管理故可以直接依赖注入Mapper接口*/
    @Autowired
    public employeeMapper employeeMapper;

    /*查询所有的员工*/
    @Override
    public dataGridResult getallemployee(QueryParameter queryParameter) {
        System.out.println("进入业务层");
        /*分页查询--使用分页插件*/
        Page<employee> page = PageHelper.startPage(queryParameter.page, queryParameter.rows);
        List<employee> list = employeeMapper.selectAllemployee(queryParameter);
        for (employee employee : list) {
            System.out.println(employee);
        }
        /*把查询到的数据封装成datagridresult  因为DataGrid组件对应答返回的JSON字符串的字段名有要求*/
        dataGridResult employeeResult = new dataGridResult();
        employeeResult.setTotal(page.getTotal());/*总记录数*/
        employeeResult.setRows(list);
        return employeeResult;
    }

    /*添加员工*/
    @Override
    public void addEmployee(employee employee) {
        /*1 先保存角色*/
        employeeMapper.insert(employee);
        /*2 保存 员工相关联的角色 到员工角色关系表中(可能有多个)*/
        /*保存员工与角色的关系*/
        for (Role role : employee.roleList) {
            employeeMapper.savemployeeRelrole(employee.getId(), role.getRid());

        }

    }
    /*仅仅添加员工*/
    @Override
    public void OnlyaddEmployee(employee employee) {
        employeeMapper.insert(employee);
    }


    /*编辑员工*/
    @Override
    public void EditEmployee(employee employee) {
        /*1  先更新员工*/
        employeeMapper.updateByPrimaryKey(employee);
        /*2  删除   员工角色关系表中的数据（删除关系根据员工的 id）*/
        employeeMapper.deleteemployeerelrole(employee.getId());
        /*3 重新建立员工角色关系  向员工角色关系表中添加数据 */
        for (Role role : employee.roleList) {
            employeeMapper.savemployeeRelrole(employee.getId(), role.getRid());
        }
    }

    /*设置离职状态*/
    @Override
    public void updateState(Integer id) {
        employeeMapper.updateEmployeeState(id);
    }

    /* 根据用户名获取用户*/
    @Override
    public employee getEmployeeWithUsername(String usernname) {
        employee employee=  employeeMapper.getEmployeeByUsername(usernname);
        return employee;
    }

    @Override
    public List<String> getRolesByemployeeId(Long id) {
        List<String> rolesNamelist=   employeeMapper.getRolesByemployeeId(id);
        return rolesNamelist;
    }

    /*查询当前用户拥有的权限资源名称 根据当前用户身份信息*/
    @Override
    public List<String> getPermissonsByemployeeId(Long id) {


        return employeeMapper.getPermissonsByemployeeId(id);
    }


    /*根据部门名字获取部门id*/
    @Override
    public Long getDepartmentid(String name) {
        return   employeeMapper.getDepartmentid(name);
    }


}
