package my.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class Role {
    private Long rid;

    private String rnum;

    private String rname;

    /*关联的权限字段*/
    public List<Permisson>permissonList=new ArrayList<Permisson>();



}