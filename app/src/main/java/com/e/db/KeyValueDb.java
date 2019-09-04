package com.e.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name="kv")
public class KeyValueDb {
    @Column(name="k" , isId=true)
    private String key ;
    @Column(name="v")
    private String value ;

    public KeyValueDb(){}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
