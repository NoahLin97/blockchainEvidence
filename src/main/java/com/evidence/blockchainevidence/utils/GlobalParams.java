package com.evidence.blockchainevidence.utils;

public class GlobalParams {

    //加密的字段
    public static String[] userenc={"remains","storageSpace","hasUsedStorage"};
    public static String[] tranenc={"transactionMoney","storageSize"};
    public static String[] evienc={"fileSize","notarizationMoney"};
    public static String[] statenc={"notarizationTotalMoney"};


    //枚举类型对应的名称，每类最多7种
    public static String[] orgtype={"type1","type2","type3"};
    public static String[] notype={"房产证公证","驾驶证公证","学历公证"};
    public static Integer[] nofee={100,200,300};
    public static String[] trantype={"充值","转赠","提现","购买存储空间","申请司法公证"};
    public static String[] evitype={"网页取证","录音取证","视频取证","书面取证"};

    public static String[] sexs={"男","女"};
}
