package com.evidence.blockchainevidence.subprotocols;

import java.math.BigInteger;
import com.evidence.blockchainevidence.PaillierT.*;

public class K2C8 {

    // K2C: Secure keyword to ciphertext algorithm

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

    public K2C8(String _S, BigInteger _pub, PaillierT _paillier){
        S=_S;
        pub = _pub;
        paillier = _paillier;
    }

    public void StepOne() {
        BigInteger B = BigInteger.ZERO;

        BigInteger temp1;
        BigInteger temp2;

        t1 = System.currentTimeMillis();
        for (int i = 0; i < S.length(); i++) {
            temp1 = BigInteger.valueOf(2).modPow(BigInteger.valueOf(8*i), paillier.n);
            temp2 = BigInteger.valueOf((int) (byte)S.charAt(i)).multiply(temp1);
            B = B.add(temp2);

//    		System.out.print(S.charAt(i)+"\n");
//    		System.out.print("0x"+Integer.toHexString((byte) S.charAt(i))+"\n");
//    		System.out.print((int) (byte)S.charAt(i)+"\n");
//    		System.out.print("temp1="+temp1+"\n");
//    		System.out.print("temp2="+temp2+"\n");
        }

//    	System.out.print("B="+B+"\n");

        FIN = paillier.Encryption(B, pub);
        t2 = System.currentTimeMillis();

        timeCP = timeCP + (t2 - t1);
        timeTotal = timeCP + timeCSP;
    }



}
