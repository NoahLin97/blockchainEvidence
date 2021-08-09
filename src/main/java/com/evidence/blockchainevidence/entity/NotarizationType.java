package com.evidence.blockchainevidence.entity;

import java.util.HashMap;

public enum NotarizationType {

        HOUSE("房产证公证",0),DRIVER("驾驶证公证",1),EDUCATION("学历公证",2);

        private String key;
        private Integer value;

        // 将数值0,1,2和HOUSE,DRIVER,EDUCATION一起封装到HashMap中
        private static HashMap<Integer,NotarizationType> valueMap = new HashMap<Integer,NotarizationType>();

        private NotarizationType(String key,Integer value){
            this.key = key;
            this.value = value;
        }

        // 静态代码块
        static{
            for (NotarizationType item:NotarizationType.values()){
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

        // 前台传进来的值通过这个方法来转换为NotarizationType类型
        public static NotarizationType getByValue(int value) {
            NotarizationType result = valueMap.get(value);
            if(result == null) {
                throw new IllegalArgumentException("No element matches " + value);
            }
            return result;
        }



}
