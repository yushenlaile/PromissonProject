package my.service;

import my.domain.department;
import my.domain.employee;
import my.mapper.departmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class departmentServiceImpl implements departmentService {
   @Autowired
   public departmentMapper departmentMapper;

    @Override
    public List<department> getalldepartments() {
        List<department>list= departmentMapper.selectAll();
        for(department department:list)
        {
            System.out.println(department);
        }
        return list;
    }



}
