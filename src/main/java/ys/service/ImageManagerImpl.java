package ys.service;


import com.blade.ioc.annotation.Bean;

import ys.pojo.ImageImpl;
import ys.service.inter.ImageManager;


public class ImageManagerImpl implements ImageManager{



    public ImageManagerImpl() {

    }

    @Override
    public ImageImpl getImage(String name) {
        ImageImpl image=null;

        try {

        }catch (Exception e){
            System.out.println("出现异常"+e);
        }

        return image;
    }
}
