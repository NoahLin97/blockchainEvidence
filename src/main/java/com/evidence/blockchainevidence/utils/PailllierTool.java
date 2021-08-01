package com.evidence.blockchainevidence.utils;

import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.helib.SLT;

import java.math.BigInteger;

public class PailllierTool {
    public static void main(String[] args){


        PaillierT paillier = new PaillierT(1024, 64);

        CipherPub qw1=paillier.Encryption(BigInteger.valueOf(91),paillier.H[0]);
        CipherPub kw=paillier.Encryption(BigInteger.valueOf(103),paillier.H[1]);
        CipherPub qw2=paillier.Encryption(BigInteger.valueOf(125),paillier.H[2]);

        SLT SK1 = new SLT(qw1,kw,paillier);
        SLT SK2 = new SLT(kw,qw2,paillier);

        SK1.StepOne();
        SK1.StepTwo();
        SK1.StepThree();

        SK2.StepOne();
        SK2.StepTwo();
        SK2.StepThree();

        System.out.println(paillier.SDecryption(SK1.FIN));
        System.out.println(paillier.SDecryption(SK2.FIN));

    }




}
