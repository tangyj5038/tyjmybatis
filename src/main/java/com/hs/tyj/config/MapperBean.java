package com.hs.tyj.config;

import java.util.List;

/**
 * @description:用于映射sql.xml
 * @date:2019/6/20
 * @author:tangyj
 * @remark:
 * */
public class MapperBean {

    private String interfaceName;
    private List<Function> list;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<Function> getList() {
        return list;
    }

    public void setList(List<Function> list) {
        this.list = list;
    }
}
