package com.evidence.blockchainevidence.subprotocols;

import java.math.BigInteger;
import java.util.Random;
import com.evidence.blockchainevidence.PaillierT.*;

public class SM {

    public CipherPub  a11 = new CipherPub();
    public CipherPub  b11 = new CipherPub();
    public BigInteger pub = BigInteger.ZERO;
    public PaillierT paillier = null;
    public BigInteger RR1= BigInteger.ZERO;
    public BigInteger RR2 = BigInteger.ZERO;
    public BigInteger RR3= BigInteger.ZERO;
    public BigInteger m1 = BigInteger.ZERO;
    public BigInteger m2 = BigInteger.ZERO;

    public CipherPub H = new CipherPub();

    public CipherPub ERR1= new CipherPub();
    public CipherPub ERR2 = new CipherPub();
    public CipherPub ERR3 = new CipherPub();

    public CipherPub S1= new CipherPub();
    public CipherPub S2 = new CipherPub();
    public CipherPub S3 = new CipherPub();

    public CipherPub EA= new CipherPub();
    public CipherPub EB = new CipherPub();

    public BigInteger EONE= BigInteger.ONE;
    public CipherPub FIN = new CipherPub();
    public  Ciphertext1 m11 = new Ciphertext1();
    public  Ciphertext1 m12 = new Ciphertext1();
    public  long CCC = 0;

    //Require _VA and _VB have the same Pub
    public SM(CipherPub  _VA, CipherPub _VB, PaillierT _paillier) {
        a11 =  _VA;
        b11 =  _VB;
        paillier=_paillier;
        pub = a11.PUB;
    }


    //Require _VA and _VB have the same Pub
    public SM(CipherPub  _VA, CipherPub _VB, BigInteger _pub, PaillierT _paillier) {
        a11 =  _VA;
        b11 =  _VB;
        paillier=_paillier;
        pub = _pub;
    }

    public void StepOne (){

        RR1 = new BigInteger(200,  new Random());
        RR2 = new BigInteger(200,  new Random());

        ERR1=paillier.Encryption(RR1, pub);
        ERR2=paillier.Encryption(RR2, pub);

        EA.T1 = a11.T1.multiply(ERR1.T1);
        EA.T2 = a11.T2.multiply(ERR1.T2);
        EA.PUB = a11.PUB;

        EB.T1 = b11.T1.multiply(ERR2.T1);
        EB.T2 = b11.T2.multiply(ERR2.T2);
        EB.PUB = b11.PUB;


        m11=paillier.AddPDec1(EA);
        m12=paillier.AddPDec1(EB);

        CCC = CCC + m11.T1.bitLength() + m11.T2.bitLength()+ m11.T3.bitLength();
        CCC = CCC + m12.T1.bitLength() + m12.T2.bitLength() + m12.T3.bitLength();
    }

    public void StepTwo (){
        m1 = paillier.AddPDec2(m11);
        m2 = paillier.AddPDec2(m12);
        m1 = m1.multiply(m2);
        H = paillier.Encryption(m1, pub);

        CCC = CCC + H.T1.bitLength() + H.T2.bitLength();
    }

    public void StepThree (){
        S1.T1 = a11.T1.modPow(paillier.n.subtract(RR2), paillier.nsquare);		//S_1
        S1.T2 = a11.T2.modPow(paillier.n.subtract(RR2), paillier.nsquare);
        S1.PUB = pub;

        S2.T1 = b11.T1.modPow(paillier.n.subtract(RR1), paillier.nsquare);		//S_2
        S2.T2 = b11.T2.modPow(paillier.n.subtract(RR1), paillier.nsquare);
        S2.PUB = pub;

        RR3 = RR1.multiply(RR2);
        ERR3 = paillier.Encryption(RR3, pub);
        S3.T1 = ERR3.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);	//S_3
        S3.T2 = ERR3.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        S3.PUB = pub;

        FIN.T1 = H.T1.multiply(S1.T1);			//S_1
        FIN.T2 = H.T2.multiply(S1.T2);
        FIN.T1 = FIN.T1.multiply(S2.T1);		//S_2
        FIN.T2 = FIN.T2.multiply(S2.T2);
        FIN.T1 = FIN.T1.multiply(S3.T1);		//S_3
        FIN.T2 = FIN.T2.multiply(S3.T2);
        FIN.PUB = pub;
    }








}
