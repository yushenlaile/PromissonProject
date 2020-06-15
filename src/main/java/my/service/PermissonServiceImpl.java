package my.service;

import my.domain.Permisson;
import my.mapper.PermissonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class PermissonServiceImpl implements PermissonService {

    @Autowired
    public PermissonMapper permissonMapper;

    /*获取所有的权限*/
    @Override
    public List<Permisson> getallPermisson() {

        List<Permisson> list=permissonMapper.selectAll();
        return list;
    }
    //根据角色的id获取角色的所有的权限
    @Override
    public List<Permisson> getPermissonByRoleid(Long rid) {
        return permissonMapper.getPermissonbyroleid(rid);
    }


}
