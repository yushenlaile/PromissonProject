package my.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class employee {
    private Long id;

    private String username;

    private String password;

    private String tel;

    private Boolean state;

    private String email;

    private Boolean admin;

    /*关联的department对象字段 （与department是多对一关系）*/
    private department department;

    /*关联的Role集合字段  （与Role是多对多关系）*/
    public List<Role>roleList=new ArrayList<>();

    /*应答返回数据时时间的格式化*/
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    /*接受客户端请求参数时的时间格式化*/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inputtime;

    }