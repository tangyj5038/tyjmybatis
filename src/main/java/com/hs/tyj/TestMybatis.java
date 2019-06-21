package com.hs.tyj;

import com.hs.tyj.entity.User;
import com.hs.tyj.mapper.UserMapper;
import com.hs.tyj.sqlSession.MySqlSession;

import java.util.List;

public class TestMybatis {

    public static void main(String[] args){
        MySqlSession mySqlSession = new MySqlSession();
        UserMapper mapper = mySqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById(1);
        System.out.println(user);
    }
}
