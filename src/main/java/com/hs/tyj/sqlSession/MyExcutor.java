package com.hs.tyj.sqlSession;

import com.hs.tyj.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @description:Excutor实现类；提供数据库查询等方法
 * @date:2019/6/20
 * @author:tangyj
 * @remark:
 * */
public class MyExcutor implements  Excutor{

    private MyConfiguration xmlConfiguration = new MyConfiguration();


    //1-获取单条数据
    public <T> T query(String sql, Object parameter) {
        Connection connection = getConnecttion();
        ResultSet set = null;
        PreparedStatement pre = null;

        try {
            pre = connection.prepareStatement(sql);
            //设置参数
            pre.setString(1,parameter.toString());
            set = pre.executeQuery();

            User u = new User();
            //遍历结果集
            while(set.next()){
                u.setId(set.getInt(1));
                u.setUserName(set.getString(2));
                u.setPassword(set.getString(3));
            }
            return (T)u;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(set != null){
                    set.close();
                }
                if(pre != null){
                    pre.close();
                }
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    //本类私有方法
    private Connection getConnecttion(){

        try {
            Connection connection = xmlConfiguration.bulid("config.xml");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
