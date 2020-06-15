package my.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
/*应答返回给dataGrid组件的结果--dataGrid组件的对应答返回的JSON字符串的字段名有要求当dataGrid组件使用分页工具时*/
public class dataGridResult {
    //必须要有以下两个字段
    public Long Total;
    public List<?>rows;

}
