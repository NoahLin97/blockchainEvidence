package com.evidence.blockchainevidence.se;

import java.math.BigInteger;
import java.util.Random;

import com.evidence.blockchainevidence.PaillierT.*;

public class SAD {

    public CipherPub  a11 = new CipherPub();
    public CipherPub  b11 = new CipherPub();
    public BigInteger pub = BigInteger.ZERO;
    public PaillierT paillier = null;
    public BigInteger RR1= BigInteger.ZERO;
    public BigInteger RR2 = BigInteger.ZERO;
    public BigInteger m1 = BigInteger.ZERO;
    public BigInteger m2 = BigInteger.ZERO;
    public BigInteger RR3= BigInteger.ZERO;
    public CipherPub ERR1= new CipherPub();
    public CipherPub ERR3 = new CipherPub();
    public CipherPub ERR2 = new CipherPub();
    public CipherPub EA= new CipherPub();
    public CipherPub EB = new CipherPub();
    public CipherPub m4 = new CipherPub();
    public Ciphertext EERR3 = new Ciphertext();
    public BigInteger EONE= BigInteger.ONE;
    public CipherPub FIN = new CipherPub();
    public  Ciphertext1 m11 = new Ciphertext1();
    public  Ciphertext1 m12 = new Ciphertext1();

    public long CCC = 0;
    public long timeCP = 0;
    public long timeCSP = 0;
    public long timeTotal = 0;

    long t1, t2, t3, t4;

    public SAD(CipherPub  _VA, CipherPub _VB, PaillierT _paillier) {
        a11 =  _VA;
        b11 =  _VB;
        paillier=_paillier;
        pub = paillier.Hsigma;
    }

    public SAD(CipherPub  _VA, CipherPub _VB, BigInteger _pub, PaillierT _paillier) {
        a11 =  _VA;
        b11 =  _VB;
        paillier=_paillier;
        pub = _pub;
    }

    public void StepOne (){

        RR1 = new BigInteger(200,  new Random());
        RR2 = new BigInteger(200,  new Random());

        ERR1=paillier.Encryption(RR1, a11.PUB);
        ERR2=paillier.Encryption(RR2, b11.PUB);

        EA.T1 = a11.T1.multiply(ERR1.T1);
        EA.T2 = a11.T2.multiply(ERR1.T2);
        EA.PUB = a11.PUB;

        EB.T1 = b11.T1.multiply(ERR2.T1);
        EB.T2 = b11.T2.multiply(ERR2.T2);
        EB.PUB = b11.PUB;

        m11=paillier.AddPDec1(EA);
        m12=paillier.AddPDec1(EB);

        CCC = CCC + m11.T1.bitLength() + m11.T2.bitLength()+m11.T3.bitLength();
        CCC = CCC + m12.T1.bitLength() + m12.T2.bitLength() +m12.T3.bitLength();
    }

    public void StepTwo (){
        m1 = paillier.AddPDec2(m11);
        m2 = paillier.AddPDec2(m12);
        m1 = m1.add(m2);
        m4 = paillier.Encryption(m1, pub);

        CCC = CCC + m4.T1.bitLength() + m4.T2.bitLength();
    }

    public void StepThree (){
        RR3 = RR1.add(RR2);
        ERR3=paillier.Encryption(RR3, pub);
        EERR3.T1=ERR3.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        EERR3.T2=ERR3.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);

        FIN.T1 = m4.T1.multiply(EERR3.T1);
        FIN.T2 = m4.T2.multiply(EERR3.T2);
        FIN.PUB = pub;
    }







}
