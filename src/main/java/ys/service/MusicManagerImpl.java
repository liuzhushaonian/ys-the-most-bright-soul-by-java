package ys.service;


import com.blade.ioc.annotation.Bean;
import ys.pojo.MusicImpl;
import ys.service.inter.MusicManager;
import ys.util.DownloadMusic;

import java.util.List;


public class MusicManagerImpl implements MusicManager {




    public MusicManagerImpl() {


    }

    @Override
    public List getMusicList() {


        List<MusicImpl> musicList=null;
        try {


        }catch (Exception e){

        }

        return musicList;
    }

    @Override
    public MusicImpl getMusic(int id) {

        MusicImpl music=null;
        try {


        }catch (Exception e){

        }
        return music;
    }


    /**
     * 每天更新播放URL
     */
    @Override
    public void getMusicUrlFrom163() {

        List<Integer> musicList=null;
        try {


            for ( Integer id:musicList){
                String url=getUrl(id);

        }

        }catch (Exception e){

        }

    }


    private String getUrl(int id){

        DownloadMusic downloadMusic=new DownloadMusic();

        return downloadMusic.getMusicUrl(id);

    }
}
