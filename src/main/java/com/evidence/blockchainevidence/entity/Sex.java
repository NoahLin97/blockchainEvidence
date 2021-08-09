package com.evidence.blockchainevidence.entity;

import java.util.HashMap;

public enum Sex {

    MALE("男",0),FEMALE("女",1);

    private String key;
    private Integer value;

    // 将数值0,1和MALE,FAMALE一起封装到HashMap中
    private static HashMap<Integer,Sex> valueMap = new HashMap<Integer,Sex>();

    private Sex(String key,Integer value){
        this.key = key;
        this.value = value;
    }

    // 静态代码块
    static{
        for (Sex item:Sex.values()){
            valueMap.put(item.getValue(), item);
        }
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public Integer getValue() {
        return value;
    }
    public void setValue(Integer value) {
        this.value = value;
    }

    // 前台传进来的值通过这个方法来转换为Sex类型
    public static Sex getByValue(int value) {
        Sex result = valueMap.get(value);
        if(result == null) {
            throw new IllegalArgumentException("No element matches " + value);
        }
        return result;
    }



}
