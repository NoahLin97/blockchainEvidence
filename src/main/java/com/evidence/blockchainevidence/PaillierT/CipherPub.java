package com.evidence.blockchainevidence.PaillierT;

import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;

public class CipherPub {

    public BigInteger T1;
    public BigInteger T2;
    public BigInteger PUB;

    public CipherPub() {
    }

    public CipherPub(String scp) {
        JSONObject jsonObj = JSONObject.parseObject(scp);
        T1=jsonObj.getBigInteger("T1");
        T2=jsonObj.getBigInteger("T2");
        PUB=jsonObj.getBigInteger("PUB");
    }

    public String toString(){
        JSONObject cp=new JSONObject();
        cp.put("T1",T1);
        cp.put("T2",T2);
        cp.put("PUB",PUB);

       return cp.toString();
    }


}
