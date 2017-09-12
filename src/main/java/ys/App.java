package ys;

import com.blade.Blade;
import org.junit.Test;
import ys.pojo.CharacterImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Blade.me().get("/*",(request, response) -> response.text("Hello World!")).start(App.class,args);
    }



}
