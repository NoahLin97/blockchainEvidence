package com.evidence.blockchainevidence.subprotocols;


import java.math.BigInteger;
import com.evidence.blockchainevidence.PaillierT.*;

// Secure Multiple Keyword Fuzzy Search Protocol Across Domains
public class MKS {

    //input
    CipherPub0 [] TT;
    CipherPub0 [] QQ;
    PaillierT paillier = null;
    BigInteger pub = BigInteger.ZERO;

    //output
    public CipherPub FIN = new CipherPub();

    public long CCC = 0;
    public long timeCP = 0;
    public long timeCSP = 0;
    public long timeTotal = 0;

    //temporary variable
    CipherPub[] U;
    CipherPub[] S;
    CipherPub[] S1;

    KET SK1;
    SMD SK2;


    int i,j;
    long t1, t2, t3, t4;


    public MKS(CipherPub0 []  _VA, CipherPub0 [] _VB, PaillierT _paillier) {
        TT =  _VA;
        QQ =  _VB;
        paillier=_paillier;
        pub = paillier.Hsigma;
    }

    public MKS(CipherPub0 [] _VA, CipherPub0 [] _VB, BigInteger _pub, PaillierT _paillier) {
        TT =  _VA;
        QQ =  _VB;
        paillier=_paillier;
        pub = _pub;
    }

    public void StepOne() {

        U = new CipherPub [TT.length];
        S = new CipherPub [TT.length];
        S1 = new CipherPub [TT.length];

        for (i=0; i<TT.length; i++)
        {
            U[i] = new CipherPub();
            S[i] = new CipherPub();
            S1[i] = new CipherPub();
        }


        FIN = paillier.Encryption(BigInteger.ZERO, pub);

        for (j=0; j<QQ.length; j++)
        {
            for (i=0; i<TT.length; i++)
            {

                SK1 = new KET(TT[i].EEK, QQ[j].EEK, paillier);
                t1 = System.currentTimeMillis();
                SK1.StepOne();
                t2 = System.currentTimeMillis();
                SK1.StepTwo();
                t3 = System.currentTimeMillis();
                SK1.StepThree();
                t4 = System.currentTimeMillis();
                U[i] = SK1.FIN;

                CCC = CCC + SK1.CCC;
                timeCP = timeCP + (t2 - t1) + (t4 - t3);
                timeCSP = timeCSP + (t3 - t2);

                SK2 = new SMD(TT[i].EES, QQ[j].EES, paillier);
                t1 = System.currentTimeMillis();
                SK2.StepOne();
                t2 = System.currentTimeMillis();
                SK2.StepTwo();
                t3 = System.currentTimeMillis();
                SK2.StepThree();
                t4 = System.currentTimeMillis();
                S1[i] = SK2.FIN;

//				CCC = CCC + SK2.CCC;
//				timeCP = timeCP + (t2 - t1) + (t4 - t3);
//				timeCSP = timeCSP + (t3 - t2);

                SK2 = new SMD(U[i], S1[i], paillier);
                t1 = System.currentTimeMillis();
                SK2.StepOne();
                t2 = System.currentTimeMillis();
                SK2.StepTwo();
                t3 = System.currentTimeMillis();
                SK2.StepThree();
                t4 = System.currentTimeMillis();
                S[i] = SK2.FIN;

//				CCC = CCC + SK2.CCC;
//				timeCP = timeCP + (t2 - t1) + (t4 - t3);
//				timeCSP = timeCSP + (t3 - t2);

                t1 = System.currentTimeMillis();
                FIN.T1 = FIN.T1.multiply(S[i].T1);
                FIN.T2 = FIN.T2.multiply(S[i].T2);
                FIN.PUB = pub;
                t2 = System.currentTimeMillis();

//				timeCP = timeCP + (t2 - t1);
                timeTotal = timeCP + timeCSP;
            }
        }
    }














}
