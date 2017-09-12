package ys.controller;


import com.blade.ioc.annotation.Inject;
import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import ys.jdbc.Database;
import ys.jdbc.inter.Image;
import ys.pojo.ImageImpl;
import ys.service.inter.ImageManager;

@Path
public class ImageController {



    @Inject
    private Image image;



    public ImageController() {

    }




    @GetRoute("get-image-by-name")
    @JSON
    public ImageImpl getImage(@Param String name){
        return image.getImageByName(name);
    }
}
