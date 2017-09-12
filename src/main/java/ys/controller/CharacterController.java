package ys.controller;



import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.http.Request;
import ys.jdbc.Database;
import ys.jdbc.inter.Character;
import ys.pojo.CharacterImpl;
import ys.service.CharacterManagerImpl;
import ys.service.inter.CharacterManager;

import java.util.List;

@Path
public class CharacterController {


    @Inject
    private Character character;




    public CharacterController() {

    }


    /**
     * 根据ID获取角色信息
     * @param id
     * @return
     */


    @GetRoute("get-character-by-id")
    @JSON
    public CharacterImpl getCharacterById(@Param int id){

        return character.getCharacterById(id);
    }


    @GetRoute("get-character-list")
    @JSON
    public List getCharacterList(){
        return character.getCharacterList();
    }


    @GetRoute("/test")
    @JSON
    public String test(@Param int id){



        character.getCharacterById(1);

        return "success!";


    }




}
