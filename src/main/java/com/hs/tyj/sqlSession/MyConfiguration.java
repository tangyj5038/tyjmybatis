package com.hs.tyj.sqlSession;


import com.hs.tyj.config.Function;
import com.hs.tyj.config.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @description:Configuration类，1-读取数据库相关配置-2-提供数据库链接方法-3-解析sql.Mapper文件
 * @date:2019/6/20
 * @author:tangyj
 * @remark:
 * */
public class MyConfiguration {

    private static ClassLoader loader = ClassLoader.getSystemClassLoader();


    /**
     * @description:读取xml信息并处理
     * @param:[resource]
     * @return:com.mysql.jdbc.Connection
     * @date:2019/6/19
     * @author:tangyj
     * @remark:
     * */
    public Connection bulid(String resource){
        try {
            InputStream resourceAsStream = loader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();//SAXReader creates a DOM4J tree from SAX parsing events.
            Document document = reader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            return evalDataSource(rootElement);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("error accured while eval xml " + resource);
        }
    }


    /**
     * @description:解析mapper.xml文件
     * @param:[path]
     * @return:com.hs.tyj.config.MapperBean
     * @date:2019/6/19
     * @author:tangyj
     * @remark:
     * */
    @SuppressWarnings("rawtypes")
    public MapperBean readMapper(String path){
        MapperBean mapper = new MapperBean();

        try {
            InputStream resourceAsStream = loader.getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            mapper.setInterfaceName(rootElement.attributeValue("nameSpace").trim());//把mapper节点的nameSpace值存为接口名
            List<Function> list = new ArrayList<Function>();
            for(Iterator rootIter = rootElement.elementIterator();rootIter.hasNext();){//遍历跟节点下的所有子节点
                Function func = new Function();
                Element e = (Element)rootIter.next();
                String sqlType = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText().trim();
                String resultType = e.attributeValue("resultType").trim();

                func.setSqlType(sqlType);
                func.setFuncName(funcName);
                Object newInstance = null;

                try {
                    newInstance = Class.forName(resultType).newInstance();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

                func.setResultType(newInstance);
                func.setSql(sql);
                list.add(func);
            }
            mapper.setList(list);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return mapper;
    }

    //本类私有方法
    //1-解析数据库配置
    private Connection evalDataSource(Element node) throws ClassNotFoundException {
        if(!node.getName().equals("database")){
            throw  new RuntimeException("root should be <database>");
        }
        String driverClassName = null;
        String url = null;
        String userName = null;
        String password = null;

        //1-获取属性节点
        List property = node.elements("property");
        for(Object item : property){
            Element i = (Element)item;
            String value = getValue(i);
            String name = i.attributeValue("name");
            if(name == null || value == null){
                throw  new RuntimeException("[database]:<property> should contain name and value");
            }
            //2-赋值
            if(name.equals("driverClassName")){
                driverClassName = value;
            }else if(name.equals("url")){
                url = value;
            }else if(name.equals("username")){
                userName = value;
            }else if(name.equals("password")){
                password = value;
            }else{
                throw new RuntimeException("[database]:<property> unknow name");
            }
        }
        Class.forName(driverClassName);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;

    }
    //2-获取property的属性的值，如果有value值，则读取，则设置
    private String getValue(Element node){
        return node.hasContent()? node.getText() : node.attributeValue("value");
    }
}
