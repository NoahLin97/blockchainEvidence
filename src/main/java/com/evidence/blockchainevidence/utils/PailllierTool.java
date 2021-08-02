package com.evidence.blockchainevidence.utils;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.helib.SLT;

import java.math.BigInteger;
import java.util.Random;

public class PailllierTool {
    public static void main(String[] args){


        PaillierT paillier = new PaillierT(PaillierT.param);
        BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
        String s=sk.toString();

        System.out.println(s.length());



    }




}
