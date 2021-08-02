package com.evidence.blockchainevidence.utils;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.helib.SLT;

import java.math.BigInteger;

public class PailllierTool {
    public static void main(String[] args){


        PaillierT paillier = new PaillierT(1024, 64);


        System.out.println(paillier.toString());



    }




}
