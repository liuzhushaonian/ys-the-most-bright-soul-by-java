package ys.service.inter;

import ys.pojo.MusicImpl;

import java.util.List;

public interface MusicManager {
    List getMusicList();

    MusicImpl getMusic(int id);

    void getMusicUrlFrom163();


}
