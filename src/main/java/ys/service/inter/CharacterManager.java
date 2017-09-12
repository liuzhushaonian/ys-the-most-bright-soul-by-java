package ys.service.inter;

import ys.pojo.CharacterImpl;

import java.util.List;

public interface CharacterManager {
    List getCharacterList();

    CharacterImpl getSingleCharacterById(int id);

//    void saveImage();


}
