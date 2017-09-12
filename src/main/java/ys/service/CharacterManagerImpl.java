package ys.service;


import com.blade.ioc.annotation.Bean;

import ys.pojo.CharacterImpl;
import ys.service.inter.CharacterManager;

import java.util.List;


public class CharacterManagerImpl implements CharacterManager {

    private Character mapper;




    public CharacterManagerImpl() {

    }


    @Override
    public List getCharacterList() {

        return null;
    }

    @Override
    public CharacterImpl getSingleCharacterById(int id) {
       return null;
    }

//    @Override
//    public void saveImage() {
//        Image mapper1=jdbcContent.getBean(Image.class);
//
//        List<ImageImpl> imageList=mapper1.getImageList();
//
//        for (ImageImpl image:imageList){
//            String name=image.getTag();
//
//            String url=(image.getUrl().split(";"))[0];
//
//            mapper.updateCharacter(name,url);
//        }
//
//    }
}
