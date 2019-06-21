package com.hs.tyj.sqlSession;

import java.lang.reflect.Proxy;
import java.util.List;


/**
 * @description:sqlSession类，SqlSession提供select/insert/update/delete方法
 * @date:2019/6/20
 * @author:tangyj
 * @remark:
 * */
public class MySqlSession {

    private Excutor excutor = new MyExcutor();

    private MyConfiguration myConfiguration = new MyConfiguration();

    //1-获取单条数据
    public <T> T selectOne(String statement, Object parameter){
        return excutor.query(statement,parameter);
    }

    public <T> T getMapper(Class<T> clas){
        //动态代理
        return (T) Proxy.newProxyInstance(clas.getClassLoader(),new Class[]{clas},new MyMapperProxy(this,myConfiguration));
    }

}
