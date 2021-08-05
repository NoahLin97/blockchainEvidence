package com.evidence.blockchainevidence.utils;

public class GlobalParams {

    //加密的字段
    public static String[] userencstr={"idCard"};
    public static String[] userencnum={"remains","storageSpace","hasUsedStorage"};
    public static String[] notarystr={"idCard"};
    public static String[] autmanstr={"idCard"};
    public static String[] manstr={"idCard"};
    public static String[] tranencnum={"userRemains","transactionMoney","storageSize"};
    public static String[] tranencstr={"evidenceName","transactionPeople"};
    public static String[] eviencstr={"evidenceName"};
    public static String[] eviencnum={"fileSize","notarizationMoney"};
    public static String[] statencnum={"notarizationTotalMoney"};


    //枚举类型对应的名称，每类最多7种
    public static String[] orgtype={"type1","type2","type3"};
    public static String[] notarizationTypes={"房产证公证","驾驶证公证","学历公证"};
    public static Integer[] notarizationMoneys={100,200,300};
    public static String[] transactionTypes={"充值","转赠","提现","购买存储空间","申请司法公证"};
    public static String[] evidenceTypes={"网页取证","录音取证","视频取证","书面取证"};

    public static String[] sexs={"男","女"};
    public static String[] transactionStatuses={"未支付","已支付"};
    public static String[] notarizationStatuses={"未公证","等待公证","公证审核中","公证成功","公证失败"};
}
