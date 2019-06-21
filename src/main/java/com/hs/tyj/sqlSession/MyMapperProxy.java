package com.hs.tyj.sqlSession;

import com.hs.tyj.config.Function;
import com.hs.tyj.config.MapperBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;


/**
 * @description:Mapper接口代理，主要功能：读取sql.xml文件，通过动态代理防范，实现UserMapper接口
 * @date:2019/6/20
 * @author:tangyj
 * @remark:
 * */
public class MyMapperProxy implements InvocationHandler{

    private MySqlSession mySqlSession;

    private MyConfiguration myConfiguration;

    public MyMapperProxy(MySqlSession mySqlSession, MyConfiguration myConfiguration) {
        this.mySqlSession = mySqlSession;
        this.myConfiguration = myConfiguration;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean readMapper = myConfiguration.readMapper("UserMapper.xml");//读取sql.xml,并将将接口名称和方法体封装到MapperBean；
        //是否是文件对应的接口
        if(!method.getDeclaringClass().getName().equals(readMapper.getInterfaceName())){
            return  null;
        }

        List<Function> list = readMapper.getList();//读取方法体
        if(null != list || 0 != list.size()){
            for(Function func : list){
                //id是否和接口方法名一样
                if(method.getName().equals(func.getFuncName())){
                    return mySqlSession.selectOne(func.getSql(),String.valueOf(args[0]));//执行selectOne方法
                }
            }
        }
        return null;
    }
}


