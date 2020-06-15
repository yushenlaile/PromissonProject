package my.service;

import my.domain.Permisson;

import java.util.List;

public interface PermissonService {
    public  abstract List<Permisson>  getallPermisson();

    List<Permisson> getPermissonByRoleid(Long rid);
}
