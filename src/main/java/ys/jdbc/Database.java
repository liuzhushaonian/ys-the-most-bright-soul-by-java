package ys.jdbc;



import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.blade.ioc.annotation.Bean;
import com.blade.ioc.annotation.Inject;
import com.blade.ioc.annotation.Order;
import ys.jdbc.inter.Character;
import ys.jdbc.inter.Image;
import ys.jdbc.inter.Movie;
import ys.jdbc.inter.Music;
import ys.killer.DatabaseKiller;
import ys.pojo.CharacterImpl;
import ys.pojo.ImageImpl;
import ys.pojo.MusicImpl;
import ys.util.DownloadMusic;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


@Bean
public class Database<T> implements Character,Image,Movie,Music{

    private static DataSource dataSource;

    Logger logger=Logger.getLogger("database.log");


    /**
     * 初始化连接池
     */
    public Database() {

        logger.setLevel(Level.ALL);




//            dataSource=new DruidDataSource();
            Properties properties=new Properties();
            InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream("druid");
            try {


                properties.load(inputStream);

                dataSource=DruidDataSourceFactory.createDataSource(properties);


            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    public void test(){
        try {
            Connection connection=dataSource.getConnection();

            String sql="select * from t_character limit 2;";

            PreparedStatement statement=connection.prepareStatement(sql);

            ResultSet resultSet=statement.executeQuery();
            DatabaseKiller databaseKiller=new DatabaseKiller();

            List list=databaseKiller.getKillerList(CharacterImpl.class,resultSet);



            System.out.println(list.get(0));

            System.out.println(list.get(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public CharacterImpl getCharacterById(int id) {

        String sql="select * from t_character where id = "+id+";";



       CharacterImpl character= (CharacterImpl) execute(sql,CharacterImpl.class);



        return character;
    }

    @Override
    public List getCharacterList() {


        String sql="select * from t_character";

        List list= (List) executeList(sql,CharacterImpl.class);

        return list;
    }

    @Override
    public ImageImpl getImageByName(String name) {

        String sql="select * from t_image where tag= '"+name+"';";

        ImageImpl image= (ImageImpl) execute(sql,ImageImpl.class);
        return image;
    }



    @Override
    public List getMusicList() {
        String sql="select * from t_music";

        List list= (List) executeList(sql,MusicImpl.class);

        return list;
    }

    @Override
    public MusicImpl getMusicById(int id) {
        String sql="select * from t_music where id ="+id+";";

        MusicImpl music= (MusicImpl) execute(sql,MusicImpl.class);
        return music;
    }


    @Override
    public void getMusicFrom163() {
        String sql="select id from t_music;";

        List<Integer> list=getMusicId(sql);

        DownloadMusic downloadMusic=new DownloadMusic();


        for (Integer id:list){
            String url=downloadMusic.getMusicUrl(id);

            logger.info("get url----"+url+"the id is-------="+id);

            String updateUrl="UPDATE t_music SET url= '"+url+"' WHERE id="+id+";";

            updateMusicUrl(updateUrl);

        }

    }

    //获取对象数组
    private T executeList(String sql,Class<?> t){
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        DatabaseKiller databaseKiller=null;
        List list=null;
        try {
            connection=dataSource.getConnection();

            statement=connection.prepareStatement(sql);

            resultSet=statement.executeQuery();

            databaseKiller=new DatabaseKiller();

            list=databaseKiller.getKillerList(t,resultSet);



        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(connection,statement,resultSet);
        }

        return (T) list;
    }

    private void close(Connection connection,PreparedStatement statement,ResultSet resultSet){
        try {
            if (null!=resultSet) {
                resultSet.close();
            }
            if (null!=statement) {
                statement.close();
            }

            if (null!=connection) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //获取单个对象
    private T execute(String sql,Class<?> t){
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        DatabaseKiller databaseKiller=null;

        try {
            connection=dataSource.getConnection();

            statement=connection.prepareStatement(sql);

            resultSet=statement.executeQuery();

            databaseKiller=new DatabaseKiller();

            return (T) databaseKiller.getKiller(t,resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(connection,statement,resultSet);
        }

        return null;

    }

    private List getMusicId(String sql){
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;

        List<Integer> list=new ArrayList();
        try {
            connection=dataSource.getConnection();

            statement=connection.prepareStatement(sql);

            resultSet=statement.executeQuery();

           while (resultSet.next()){
               list.add(resultSet.getInt("id"));
           }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(connection,statement,resultSet);
        }
        return list;
    }

    private void updateMusicUrl(String sql){
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        int i=-2;
        try {
            connection=dataSource.getConnection();

            statement=connection.prepareStatement(sql);

            i=statement.executeUpdate();



        } catch (SQLException e) {

            logger.info("sql is "+sql);

            logger.info("result is "+i);

            e.printStackTrace();

        }finally {
            close(connection,statement,resultSet);
        }
    }
}
