package com.evidence.blockchainevidence.PaillierT;

import java.math.BigInteger;

public class Str2CipherPub {

    // input
    String S;
    BigInteger pub = BigInteger.ZERO;
    PaillierT paillier = null;

    // output
    public CipherPub FIN = new CipherPub();

    public long CCC = 0;
    public long timeCP = 0;
    public long timeCSP = 0;
    public long timeTotal = 0;

    //temporary variables
    long t1, t2;

    public Str2CipherPub(String _S, BigInteger _pub, PaillierT _paillier){
        S=_S;
        pub = _pub;
        paillier = _paillier;
    }

    public void StepOne() {
        BigInteger B = BigInteger.ZERO;
        CipherPub E;

        BigInteger temp1;
        BigInteger temp2;

        t1 = System.currentTimeMillis();
        for (int i = 0; i < S.length(); i++) {
            temp1 = BigInteger.valueOf(2).modPow(BigInteger.valueOf(16*i), paillier.n);
            temp2 = BigInteger.valueOf((int) S.charAt(i)).multiply(temp1);
            B = B.add(temp2);
        }

//    	System.out.print("B="+B+"\n");

        FIN = paillier.Encryption(B, pub);
        t2 = System.currentTimeMillis();

        timeCP = timeCP + (t2 - t1);
        timeTotal = timeCP + timeCSP;
    }







}
