package com.evidence.blockchainevidence.utils;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.helib.SEA;
import com.evidence.blockchainevidence.helib.SLT;
import com.evidence.blockchainevidence.subprotocols.*;

import java.math.BigInteger;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PailllierTool {
    public static void main(String[] args){


        String evi1="血迹";
        String tp1="3d817ab092c64930bb877538902a7cb6";
        PaillierT paillier = new PaillierT(PaillierT.param);
        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);



        K2C8 SK00 = new K2C8(tp1, pk, paillier);
        SK00.StepOne();
        String skw=SK00.FIN.toString();
        System.out.println(skw);

        K2C8 SK01 = new K2C8(tp1, pk, paillier);
        SK01.StepOne();
        String skw1=SK01.FIN.toString();
        System.out.println(skw1);

        K2C16 SK10 = new K2C16(evi1, pk, paillier);
        SK10.StepOne();
        String skw2=SK10.FIN.toString();
        System.out.println(skw2);

        K2C16 SK11 = new K2C16(evi1, pk, paillier);
        SK11.StepOne();
        String skw3=SK11.FIN.toString();
        System.out.println(skw3);



        KET ket = new KET(new CipherPub(skw),new CipherPub(skw1),paillier);
        ket.StepOne();
        ket.StepTwo();
        ket.StepThree();
        System.out.println(paillier.SDecryption(ket.FIN));

        ket = new KET(new CipherPub(skw2),new CipherPub(skw3),paillier);
        ket.StepOne();
        ket.StepTwo();
        ket.StepThree();
        System.out.println(paillier.SDecryption(ket.FIN));


        System.out.println(K2C8.parseString(paillier.SDecryption(new CipherPub(skw)),paillier));
        System.out.println(K2C8.parseString(paillier.SDecryption(new CipherPub(skw1)),paillier));
        System.out.println(K2C16.parseString(paillier.SDecryption(new CipherPub(skw2)),paillier));
        System.out.println(K2C16.parseString(paillier.SDecryption(new CipherPub(skw3)),paillier));



        }


}
