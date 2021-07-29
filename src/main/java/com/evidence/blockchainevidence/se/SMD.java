package com.evidence.blockchainevidence.se;

import java.math.BigInteger;
import java.util.Random;

import com.evidence.blockchainevidence.PaillierT.*;

public class SMD {

    public CipherPub  a11 = new CipherPub();
    public CipherPub  b11 = new CipherPub();
    public BigInteger pub = BigInteger.ZERO;
    public PaillierT paillier = null;
    public BigInteger RR1= BigInteger.ZERO;
    public BigInteger RR2 = BigInteger.ZERO;
    public BigInteger RR3= BigInteger.ZERO;
    public BigInteger RRR1= BigInteger.ZERO;
    public BigInteger RRR2 = BigInteger.ZERO;
    public BigInteger m1 = BigInteger.ZERO;
    public BigInteger m2 = BigInteger.ZERO;
    public BigInteger m3 = BigInteger.ZERO;
    public BigInteger m4 = BigInteger.ZERO;

    public CipherPub H1 = new CipherPub();
    public CipherPub H3 = new CipherPub();
    public CipherPub H4 = new CipherPub();

    public CipherPub ERR1= new CipherPub();
    public CipherPub ERR2 = new CipherPub();
    public CipherPub ERR3 = new CipherPub();

    public CipherPub ERRR1= new CipherPub();
    public CipherPub ERRR2 = new CipherPub();

    public CipherPub EA= new CipherPub();
    public CipherPub EB = new CipherPub();
    public CipherPub ES= new CipherPub();
    public CipherPub ET = new CipherPub();

    public BigInteger EONE= BigInteger.ONE;
    public CipherPub FIN = new CipherPub();
    public BigInteger FIN2 = BigInteger.ZERO;
    public  Ciphertext1 m11 = new Ciphertext1();
    public  Ciphertext1 m12 = new Ciphertext1();
    public  Ciphertext1 m13 = new Ciphertext1();
    public  Ciphertext1 m14 = new Ciphertext1();
    public  long CCC = 0;

    public SMD(CipherPub  _VA, CipherPub _VB, PaillierT _paillier) {
        a11 =  _VA;
        b11 =  _VB;
        paillier=_paillier;
        pub = paillier.Hsigma;
    }

    public SMD(CipherPub  _VA, CipherPub _VB, BigInteger _pub, PaillierT _paillier) {
        a11 =  _VA;
        b11 =  _VB;
        paillier=_paillier;
        pub = _pub;
    }

    public void StepOne (){


        RR1 = new BigInteger(200,  new Random());
        RR2 = new BigInteger(200,  new Random());

        RRR1 = new BigInteger(200,  new Random());
        RRR2 = new BigInteger(200,  new Random());

        ERR1=paillier.Encryption(RR1, a11.PUB);
        ERR2=paillier.Encryption(RR2, b11.PUB);

        ERRR1=paillier.Encryption(RRR1, a11.PUB);
        ERRR2=paillier.Encryption(RRR2, b11.PUB);

        EA.T1 = a11.T1.multiply(ERR1.T1);
        EA.T2 = a11.T2.multiply(ERR1.T2);
        EA.PUB = a11.PUB;

        EB.T1 = b11.T1.multiply(ERR2.T1);
        EB.T2 = b11.T2.multiply(ERR2.T2);
        EB.PUB = b11.PUB;


        ES.T1 = a11.T1.modPow(paillier.n.subtract(RR2), paillier.nsquare);
        ES.T2 =a11.T2.modPow(paillier.n.subtract(RR2), paillier.nsquare);
        ES.T1 = ERRR1.T1.multiply(ES.T1);
        ES.T2 = ERRR1.T2.multiply(ES.T2);
        ES.PUB = a11.PUB;



        ET.T1 = b11.T1.modPow(paillier.n.subtract(RR1), paillier.nsquare);
        ET.T2 = b11.T2.modPow(paillier.n.subtract(RR1), paillier.nsquare);
        ET.T1 = ERRR2.T1.multiply(ET.T1);
        ET.T2 = ERRR2.T2.multiply(ET.T1);
        ET.PUB = b11.PUB;


        m11=paillier.AddPDec1(EA);
        m12=paillier.AddPDec1(EB);
        m13=paillier.AddPDec1(ES);
        m14=paillier.AddPDec1(ET);

        CCC = CCC + m11.T1.bitLength() + m11.T2.bitLength()+m11.T3.bitLength();
        CCC = CCC + m12.T1.bitLength() + m12.T2.bitLength() +m12.T3.bitLength();
        CCC = CCC + m13.T1.bitLength() + m13.T2.bitLength() +m13.T3.bitLength();
        CCC = CCC + m14.T1.bitLength() + m14.T2.bitLength() +m14.T3.bitLength();
    }

    public void StepTwo (){
        m1 = paillier.AddPDec2(m11);
        m2 = paillier.AddPDec2(m12);
        m3 = paillier.AddPDec2(m13);         	//S
        m4 = paillier.AddPDec2(m14);         	//T
        m1 = m1.multiply(m2);
        H1 = paillier.Encryption(m1, pub);
        H3 = paillier.Encryption(m3, pub);		//S
        H4 = paillier.Encryption(m4, pub);		//T

        CCC = CCC + H1.T1.bitLength() + H1.T2.bitLength();
        CCC = CCC + H3.T1.bitLength() + H3.T2.bitLength();
        CCC = CCC + H4.T1.bitLength() + H4.T2.bitLength();
    }

    public void StepThree (){
        RR3 = RR1.multiply(RR2);
        ERR3 = paillier.Encryption(RR3, pub);
        ERR3.T1 = ERR3.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);		//S_4
        ERR3.T2 = ERR3.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);

        ERRR1.T1 = ERRR1.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);	//S_5
        ERRR1.T2 = ERRR1.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        ERRR2.T1 = ERRR2.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);	//S_6
        ERRR2.T2 = ERRR2.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);

        FIN.T1 = H1.T1.multiply(H3.T1);			//S_3
        FIN.T2 = H1.T2.multiply(H3.T2);
        FIN.T1 = FIN.T1.multiply(H4.T1);		//T_3
        FIN.T2 = FIN.T2.multiply(H4.T2);
        FIN.T1 = FIN.T1.multiply(ERR3.T1);		//S_4
        FIN.T2 = FIN.T2.multiply(ERR3.T2);
        FIN.T1 = FIN.T1.multiply(ERRR1.T1);		//S_5
        FIN.T2 = FIN.T2.multiply(ERRR1.T2);
        FIN.T1 = FIN.T1.multiply(ERRR2.T1);		//S_6
        FIN.T2 = FIN.T2.multiply(ERRR2.T2);
        FIN.PUB = pub;
        FIN2= paillier.SDecryption(FIN);
    }








}
