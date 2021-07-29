package com.evidence.blockchainevidence.PaillierT;

import java.math.BigInteger;

public class Enc {

    //input
    String KK_S;
    BigInteger SS_S;
    String [] KK_M;
    BigInteger [] SS_M;
    PaillierT paillier = null;
    BigInteger pub = BigInteger.ZERO;

    //output
    CipherPub0 FIN_S = new CipherPub0();
    CipherPub0 [] FIN_M;

    //time and CCC
    public long CCC = 0;
    public long timeCP = 0;
    public long timeCSP = 0;
    public long timeTotal = 0;
    public long t1, t2, t3, t4;

    //temporary variables
    int i,j;
    Str2CipherPub SK1;

    //_VA: Single keyword
    //_VB: Single relevant score
    public Enc(String _VA, BigInteger _VB, BigInteger _pub, PaillierT _paillier) {
        KK_S =  _VA;
        SS_S =  _VB;
        paillier=_paillier;
        pub = _pub;
    }

    //_VA: Sets of keywords
    //_VB: Sets of relevant scores
    //_VA.length should equal to _VB.length
    public Enc(String [] _VA, BigInteger [] _VB, BigInteger _pub, PaillierT _paillier) {
        KK_M =  _VA;
        SS_M =  _VB;
        paillier=_paillier;
        pub = _pub;

        FIN_M = new CipherPub0[KK_M.length];

        for (i=0; i< KK_M.length; i++)
        {
            FIN_M[i] = new CipherPub0();
            FIN_M[i].EEK = new CipherPub();
        }
    }

    public void Single() {
        SK1 = new Str2CipherPub (KK_S, pub, paillier);
        SK1.StepOne();
        FIN_S.EEK = SK1.FIN;
        CCC = CCC + SK1.CCC;
        timeCP = timeCP + SK1.timeCP;
        timeCSP = timeCSP + SK1.timeCSP;

        t1 = System.currentTimeMillis();
        FIN_S.EES = paillier.Encryption(SS_S, pub);
        t2 = System.currentTimeMillis();
        timeCP = timeCP + (t2 - t1);
        timeTotal = timeCP + timeCSP;
    }

    public void Multiple() {

        for(i=0; i< KK_M.length; i++)
        {
            SK1 = new Str2CipherPub (KK_M[i], pub, paillier);
            SK1.StepOne();
            FIN_M[i].EEK = SK1.FIN;
            CCC = CCC + SK1.CCC;
            timeCP = timeCP + SK1.timeCP;
            timeCSP = timeCSP + SK1.timeCSP;

            t1 = System.currentTimeMillis();
            FIN_M[i].EES = paillier.Encryption(SS_M[i], pub);
            t2 = System.currentTimeMillis();
            timeCP = timeCP + (t2 - t1);
        }

        timeTotal = timeCP + timeCSP;
    }


}
