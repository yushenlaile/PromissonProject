package my.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
/*查询参数*/
public class QueryParameter {

    /*当前页码*/
    public Integer page;
    /*当前页的记录数量*/
    public  Integer rows;
    /*输入的查询参数*/
    public  String content;
}
