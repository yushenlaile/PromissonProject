package my.mapper;

import java.util.List;
import my.domain.department;

public interface departmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(department record);

    department selectByPrimaryKey(Long id);

    List<department> selectAll();

    int updateByPrimaryKey(department record);
}