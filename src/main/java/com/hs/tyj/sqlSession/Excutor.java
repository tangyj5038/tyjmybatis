package com.hs.tyj.sqlSession;

public interface Excutor {

    public <T> T query(String stattement, Object parameter);

}
