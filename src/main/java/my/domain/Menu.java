package my.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class Menu {
    private Long id;

    private String text;

    private String url;

    private Menu parent;

    private List<Menu>children=new ArrayList<>();

    private Permisson permisson;

}