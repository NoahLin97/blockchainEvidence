package com.evidence.blockchainevidence.subprotocols;

import java.math.BigInteger;
import java.util.Random;

import com.evidence.blockchainevidence.PaillierT.*;

public class SAD2 {

    public CipherPub  a11 = new CipherPub();
    public CipherPub  b11 = new CipherPub();
    public CipherPub  a22 = new CipherPub();
    public CipherPub  b22 = new CipherPub();
    public BigInteger pub = BigInteger.ZERO;
    public PaillierT paillier = null;
    public BigInteger RR1= BigInteger.ZERO;
    public BigInteger RR2 = BigInteger.ZERO;
    public BigInteger mm11 = BigInteger.ZERO;
    public BigInteger mm12 = BigInteger.ZERO;
    public BigInteger mm21 = BigInteger.ZERO;
    public BigInteger mm22 = BigInteger.ZERO;
    public CipherPub M1 = new CipherPub();
    public CipherPub M2 = new CipherPub();
    public BigInteger RR3= BigInteger.ZERO;
    public CipherPub ERR1= new CipherPub();
    public CipherPub ERR3 = new CipherPub();
    public CipherPub ERR2 = new CipherPub();
    public CipherPub EA1 = new CipherPub();
    public CipherPub EB1 = new CipherPub();
    public CipherPub EA2 = new CipherPub();
    public CipherPub EB2 = new CipherPub();
    public Ciphertext EERR3 = new Ciphertext();
    public BigInteger EONE= BigInteger.ONE;
    public CipherPub FIN1 = new CipherPub();
    public CipherPub FIN2 = new CipherPub();
    public  Ciphertext1 m11 = new Ciphertext1();
    public  Ciphertext1 m12 = new Ciphertext1();
    public  Ciphertext1 m21 = new Ciphertext1();
    public  Ciphertext1 m22 = new Ciphertext1();
    public  long CCC = 0;

    public SAD2(CipherPub  _VA1, CipherPub _VB1, CipherPub  _VA2, CipherPub _VB2, PaillierT _paillier) {
        a11 =  _VA1;
        b11 =  _VB1;
        a22 =  _VA2;
        b22 =  _VB2;
        paillier=_paillier;
        pub = paillier.Hsigma;
    }

    public SAD2(CipherPub  _VA1, CipherPub _VB1, CipherPub  _VA2, CipherPub _VB2, BigInteger _pub, PaillierT _paillier) {
        a11 =  _VA1;
        b11 =  _VB1;
        a22 =  _VA2;
        b22 =  _VB2;
        paillier=_paillier;
        pub = _pub;
    }

    public void StepOne (){

        RR1 = new BigInteger(200,  new Random());
        RR2 = new BigInteger(200,  new Random());

        ERR1=paillier.Encryption(RR1, a11.PUB);
        ERR2=paillier.Encryption(RR2, b11.PUB);

        EA1.T1 = a11.T1.multiply(ERR1.T1);
        EA1.T2 = a11.T2.multiply(ERR1.T2);
        EA1.PUB = a11.PUB;

        EB1.T1 = b11.T1.multiply(ERR2.T1);
        EB1.T2 = b11.T2.multiply(ERR2.T2);
        EB1.PUB = b11.PUB;

        EA2.T1 = a22.T1.multiply(ERR1.T1);
        EA2.T2 = a22.T2.multiply(ERR1.T2);
        EA2.PUB = a22.PUB;

        EB2.T1 = b22.T1.multiply(ERR2.T1);
        EB2.T2 = b22.T2.multiply(ERR2.T2);
        EB2.PUB = b22.PUB;

        m11=paillier.AddPDec1(EA1);
        m12=paillier.AddPDec1(EB1);

        m21=paillier.AddPDec1(EA2);
        m22=paillier.AddPDec1(EB2);

        CCC = CCC + m11.T1.bitLength() + m11.T2.bitLength()+m11.T3.bitLength();
        CCC = CCC + m12.T1.bitLength() + m12.T2.bitLength() +m12.T3.bitLength();
        CCC = CCC + m21.T1.bitLength() + m21.T2.bitLength()+m21.T3.bitLength();
        CCC = CCC + m22.T1.bitLength() + m22.T2.bitLength() +m22.T3.bitLength();
    }

    public void StepTwo (){
        mm11 = paillier.AddPDec2(m11);
        mm12 = paillier.AddPDec2(m12);
        mm11 = mm11.add(mm12);
        M1 = paillier.Encryption(mm11, pub);

        mm21 = paillier.AddPDec2(m21);
        mm22 = paillier.AddPDec2(m22);
        mm21 = mm21.add(mm22);
        M2 = paillier.Encryption(mm21, pub);

        CCC = CCC + M1.T1.bitLength() + M1.T2.bitLength();
        CCC = CCC + M2.T1.bitLength() + M2.T2.bitLength();
    }

    public void StepThree (){
        RR3 = RR1.add(RR2);
        ERR3=paillier.Encryption(RR3, pub);
        EERR3.T1=ERR3.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        EERR3.T2=ERR3.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);

        FIN1.T1 = M1.T1.multiply(EERR3.T1);
        FIN1.T2 = M1.T2.multiply(EERR3.T2);
        FIN1.PUB = pub;

        FIN2.T1 = M2.T1.multiply(EERR3.T1);
        FIN2.T2 = M2.T2.multiply(EERR3.T2);
        FIN2.PUB = pub;
    }







}
