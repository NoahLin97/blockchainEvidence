package com.evidence.blockchainevidence.utils;

import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.helib.SEA;
import com.evidence.blockchainevidence.subprotocols.K2C8;
import com.evidence.blockchainevidence.subprotocols.KMP;

import java.math.BigInteger;
import java.util.Random;

public class PailllierTool {
    public static void main(String[] args){

        //匹配数字时一个*最多匹配3个字符
        //不行的
//        String qw="*9579";
//        String kw="13290729579";
        //不行的
//        String qw="*@qq.com";
//        String kw="572767335@qq.com";

        //ok的
//        String qw="5*";
//        String kw="572767335@qq.com";

        //不行
//        String qw="57276733*.com";
//        String kw="572767335@qq.com";

        String qw="privacy*";
        String kw="privacy";
        System.out.println(qw);
        System.out.println(kw);
//获取参数
//        String qw=params.get("qw").toString();
//        String kw=params.get("kw").toString();
        System.out.println(qw);
        System.out.println(kw);

        //Paillier初始化,我这里为了确保参数一致，把它放到类里面了，其实应该保存在加密文件里的
        PaillierT paillier = new PaillierT(PaillierT.param);


        //为新用户生成公私钥
        BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
        BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);



        //加密字符串,得到string形式的密文，这个是用来存数据库的
        K2C8 SK0 = new K2C8(kw, pk, paillier);
        SK0.StepOne();
        String skw=SK0.FIN.toString();

        //从数据库读取密文后，转成CipherPub进行同态计算
        CipherPub ckw= new CipherPub(skw);

        //判断qw与kw是否匹配
        SEA SK1 = new SEA(qw, paillier);
        SK1.StepOne();
        KMP SK2 = new KMP(ckw, SK1.Y1, SK1.Y2, SK1.Y3, SK1.num1, SK1.num2,
                SK1.numchar1, SK1.numchar2, SK1.flag, paillier);
        SK2.StepOne();
        CipherPub cans=SK2.FIN;


        //解密匹配结果
        BigInteger ans=paillier.SDecryption(cans);

        System.out.println("匹配结果为："+ans);

        //解密kw的密文ckw
        String kw2=K2C8.parseString(paillier.SDecryption(ckw),paillier);
        System.out.println("解密结果为："+kw2);





//
//
//        String X = "privacy";
//        CipherPub EX;
//        SK0 = new K2C8(X, paillier.H[1], paillier);
//        SK0.StepOne();
//        EX = SK0.FIN;
//
////			S = "privacy";		//no *
////			S = "*privacy"; 	//"Front"
////			S = "pr*vacy"; 		//"Middle"
////			S = "privacy*"; 	//"Back"
////			S = "*ri*cy";		//"Front + Middle"
////			S = "*riva*";   	//"Front + Back"  **************
//        String S = "pr*v*cy";   	//"Middle + Middle"
////			S = "pr*va*";   	//"Middle + Back"
//
//        SK1 = new SEA(S, paillier);
//        SK1.StepOne();
//
//
//        SK2 = new KMP(EX, SK1.Y1, SK1.Y2, SK1.Y3, SK1.num1, SK1.num2,
//                SK1.numchar1, SK1.numchar2, SK1.flag, paillier);
//        SK2.StepOne();
//
//
//        System.out.print("U="+paillier.SDecryption(SK2.FIN)+"\n\n");

    }


}