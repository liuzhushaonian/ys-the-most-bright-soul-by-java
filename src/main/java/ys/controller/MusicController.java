package ys.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import ys.jdbc.Database;
import ys.jdbc.inter.Music;
import ys.pojo.MusicImpl;
import ys.service.MusicManagerImpl;
import ys.service.inter.MusicManager;

import java.util.List;

@Path
public class MusicController {



//    @Inject
//    private Database database;

    @Inject
    private Music music;


    public MusicController() {

    }

    /**
     * 获取所有音乐
     * @return
     */

    @GetRoute("get-music-list")
    @JSON
    public List getMusicList(){


        return music.getMusicList();
    }

    /**
     * 获取单曲
     * @param id
     * @return
     */

    @GetRoute("get-music-by-id")
    @JSON
    public MusicImpl getMusic(@Param int id){
        return music.getMusicById(id);
    }


    /**
     * 每天定时更新音乐URL
     */

    @GetRoute("get-music-from-163")
    public void getMusicUrlFrom163(){
        music.getMusicFrom163();
    }
}
