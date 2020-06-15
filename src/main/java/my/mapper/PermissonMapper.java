package my.mapper;

import java.util.List;
import my.domain.Permisson;

public interface PermissonMapper {
    int deleteByPrimaryKey(Long pid);

    int insert(Permisson record);

    Permisson selectByPrimaryKey(Long pid);

    List<Permisson> selectAll();

    int updateByPrimaryKey(Permisson record);

    /* //根据角色的id获取角色的所有的权限*/
    List<Permisson> getPermissonbyroleid(Long rid);

}