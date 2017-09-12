package ys.killer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseKiller{

    private static final String INT="int";
    private static final String STRING="class java.lang.String";
    private static final String LONG="long";
    private static final String DOUBLE="double";
    private static final String CHAR="char";
    private static final String FLOAT="float";
    private static final String BOOLEAN="boolean";

    /**
     * 获取一个对象
     * @param t
     * @param resultSet
     * @return
     */
    public Object getKiller(Class<?> t, ResultSet resultSet){

        Object object=null;
        try {
            while (resultSet.next()) {
                object = getKillerObject(t, resultSet);
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     *
     * 遍历并封装对象
     * @param t
     * @param resultSet
     * @return
     * @throws Exception
     */
    private Object getKillerObject(Class<?> t,ResultSet resultSet) throws Exception {
        Class<?> tClass = null;

        tClass = Class.forName(t.getName());


        Field[] fields = tClass.getDeclaredFields();

//        Constructor constructor=tClass.getConstructor();
//
        Object object = tClass.newInstance();

        for (Field field : fields) {


            String type = field.getType().toString();

            field.setAccessible(true);
            switch (type) {
                case INT:
                    int intVar = resultSet.getInt(field.getName());


                    field.set(object, intVar);
                    break;
                case STRING:
                    String string = resultSet.getString(field.getName());
                    field.set(object, string);
                    break;

                case LONG:
                    long longVar = resultSet.getLong(field.getName());

                    field.set(object, longVar);
                    break;
                case DOUBLE:
                    double doubleVar = resultSet.getDouble(field.getName());

                    field.set(object, doubleVar);


                    break;
                case FLOAT:
                    float floatVar = resultSet.getFloat(field.getName());

                    field.set(object, floatVar);

                    break;
                case BOOLEAN:

                    boolean booleanVar = resultSet.getBoolean(field.getName());

                    field.set(object, booleanVar);

                    break;
            }

        }

        return object;
    }

    public List getKillerList(Class<?> t,ResultSet resultSet){
        List killerList=new ArrayList();

        try {
            while (resultSet.next()){
                killerList.add(getKillerObject(t,resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return killerList;
    }

}
