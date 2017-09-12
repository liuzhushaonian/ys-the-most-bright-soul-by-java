package ys.jdbc.inter;

import ys.pojo.CharacterImpl;

import java.util.List;

public interface Character {

    CharacterImpl getCharacterById(int id);

    List getCharacterList();
}
