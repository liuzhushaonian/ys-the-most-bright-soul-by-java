package ys.jdbc.inter;

import ys.pojo.MusicImpl;

import java.util.List;

public interface Music {
    List getMusicList();

    MusicImpl getMusicById(int id);

    void getMusicFrom163();
}
