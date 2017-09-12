package ys.pojo;


import lombok.Data;
import ys.jdbc.Database;


@Data
public class CharacterImpl{
    private int id;
    private String name;

    private String introduction;

    private String bgm;
    private String e_name;

    private String book_url;





    public CharacterImpl(){

    }
}
