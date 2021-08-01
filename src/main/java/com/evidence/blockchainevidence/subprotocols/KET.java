package com.evidence.blockchainevidence.subprotocols;

import java.math.BigInteger;
import java.util.Random;
import com.evidence.blockchainevidence.PaillierT.*;

public class KET {

    //input
    CipherPub a = new CipherPub();
    CipherPub b = new CipherPub();
    BigInteger pub = BigInteger.ZERO;
    PaillierT paillier = null;

    //output
    public CipherPub FIN = new CipherPub();
    public long CCC = 0;

    //temporary variables
    CipherPub a11 = new CipherPub();
    CipherPub b11 = new CipherPub();
    CipherPub a12 = new CipherPub();
    CipherPub b12 = new CipherPub();
    CipherPub a13 = new CipherPub();
    CipherPub b13 = new CipherPub();
    CipherPub a14 = new CipherPub();
    CipherPub b14 = new CipherPub();

    CipherPub a21 = new CipherPub();
    CipherPub b21 = new CipherPub();
    CipherPub a22 = new CipherPub();
    CipherPub b22 = new CipherPub();
    CipherPub a23 = new CipherPub();
    CipherPub b23 = new CipherPub();
    CipherPub a24 = new CipherPub();
    CipherPub b24 = new CipherPub();

    CipherPub l11 = new CipherPub();
    CipherPub l12 = new CipherPub();
    CipherPub l21 = new CipherPub();
    CipherPub l22 = new CipherPub();
    CipherPub U1 = new CipherPub();
    CipherPub U2 = new CipherPub();
    CipherPub EZERO = new CipherPub();
    CipherPub EEoneA = new CipherPub();
    CipherPub EEoneB = new CipherPub();
    CipherPub EUone = new CipherPub();

    CipherPub FIN1 = new CipherPub();
    CipherPub FIN2 = new CipherPub();

    Ciphertext1 m11 = new Ciphertext1();
    Ciphertext1 m22 = new Ciphertext1();

    BigInteger RR1 = BigInteger.ZERO;
    BigInteger RR2 = BigInteger.ZERO;
    BigInteger EONE = BigInteger.ONE;
    BigInteger ZERO = BigInteger.ZERO;
    BigInteger TWO = BigInteger.ZERO;
    BigInteger l = BigInteger.ZERO;
    BigInteger m1 = BigInteger.ZERO;
    BigInteger m2 = BigInteger.ZERO;

    int s1 = 0;
    int s2 = 0;

    public KET(CipherPub _VA, CipherPub _VB, BigInteger _pub, PaillierT _paillier) {
        a = _VA;
        b = _VB;
        paillier = _paillier;
        pub = _pub;
    }

    public KET(CipherPub _VA, CipherPub _VB, PaillierT _paillier) {
        a = _VA;
        b = _VB;
        paillier = _paillier;
        pub = paillier.Hsigma;
    }

    public void StepOne() {

        TWO = new BigInteger("2");

        EEoneA = paillier.Encryption(EONE, a.PUB);
        EEoneB = paillier.Encryption(EONE, b.PUB);
        EUone = paillier.Encryption(EONE, pub);
        EZERO = paillier.Encryption(ZERO, pub);

        a12.T1 = (a.T1.modPow(TWO, paillier.nsquare)).multiply(EEoneA.T1).mod(paillier.nsquare);
        a12.T2 = (a.T2.modPow(TWO, paillier.nsquare)).multiply(EEoneA.T2).mod(paillier.nsquare);
        a12.PUB = a.PUB;

        b12.T1 = b.T1.modPow(TWO, paillier.nsquare);
        b12.T2 = b.T2.modPow(TWO, paillier.nsquare);
        b12.PUB = b.PUB;

        a22.T1 = a.T1.modPow(TWO, paillier.nsquare);
        a22.T2 = a.T2.modPow(TWO, paillier.nsquare);
        a22.PUB = a.PUB;

        b22.T1 = (b.T1.modPow(TWO, paillier.nsquare)).multiply(EEoneB.T1).mod(paillier.nsquare);
        b22.T2 = (b.T2.modPow(TWO, paillier.nsquare)).multiply(EEoneB.T2).mod(paillier.nsquare);
        b22.PUB = b.PUB;

        RR1 = new BigInteger(200, new Random());
        RR2 = new BigInteger(200, new Random());

        Random rand = new Random();
        s1 = rand.nextInt(100000000) % 2;
        s2 = rand.nextInt(100000000) % 2;

        if (s1 == 1 & s2 == 1){

            a13.T1 = a12.T1.modPow(RR1, paillier.nsquare);
            a13.T2 = a12.T2.modPow(RR1, paillier.nsquare);
            a13.PUB = a12.PUB;

            b14.T1 = b12.T1.modPow(paillier.n.subtract(RR1), paillier.nsquare);
            b14.T2 = b12.T2.modPow(paillier.n.subtract(RR1), paillier.nsquare);
            b14.PUB = b12.PUB;

            a24.T1 = a22.T1.modPow(paillier.n.subtract(RR2), paillier.nsquare);
            a24.T2 = a22.T2.modPow(paillier.n.subtract(RR2), paillier.nsquare);
            a24.PUB = a22.PUB;

            b23.T1 = b22.T1.modPow(RR2, paillier.nsquare);
            b23.T2 = b22.T2.modPow(RR2, paillier.nsquare);
            b23.PUB = b22.PUB;

            SAD2 SK111 = new SAD2 (a13, b14, a24, b23, pub, paillier);
            SK111.StepOne();
            SK111.StepTwo();
            SK111.StepThree();

            l11 = SK111.FIN1;
            l22 = SK111.FIN2;
            CCC = CCC + SK111.CCC;

        }else if (s1 == 1 & s2 == 0){

            a13.T1 = a12.T1.modPow(RR1, paillier.nsquare);
            a13.T2 = a12.T2.modPow(RR1, paillier.nsquare);
            a13.PUB = a12.PUB;

            b14.T1 = b12.T1.modPow(paillier.n.subtract(RR1), paillier.nsquare);
            b14.T2 = b12.T2.modPow(paillier.n.subtract(RR1), paillier.nsquare);
            b14.PUB = b12.PUB;

            a23.T1 = a22.T1.modPow(RR2, paillier.nsquare);
            a23.T2 = a22.T2.modPow(RR2, paillier.nsquare);
            a23.PUB = a22.PUB;

            b24.T1 = b22.T1.modPow(paillier.n.subtract(RR2), paillier.nsquare);
            b24.T2 = b22.T2.modPow(paillier.n.subtract(RR2), paillier.nsquare);
            b24.PUB = b22.PUB;

            SAD2 SK111 = new SAD2 (a13, b14, a23, b24, pub, paillier);
            SK111.StepOne();
            SK111.StepTwo();
            SK111.StepThree();

            l11 = SK111.FIN1;
            l22 = SK111.FIN2;
            CCC = CCC + SK111.CCC;

        }else if (s1 == 0 & s2 == 1){

            a14.T1 = a12.T1.modPow(paillier.n.subtract(RR1), paillier.nsquare);
            a14.T2 = a12.T2.modPow(paillier.n.subtract(RR1), paillier.nsquare);
            a14.PUB = a12.PUB;

            b13.T1 = b12.T1.modPow(RR1, paillier.nsquare);
            b13.T2 = b12.T2.modPow(RR1, paillier.nsquare);
            b13.PUB = b12.PUB;

            a24.T1 = a22.T1.modPow(paillier.n.subtract(RR2), paillier.nsquare);
            a24.T2 = a22.T2.modPow(paillier.n.subtract(RR2), paillier.nsquare);
            a24.PUB = a22.PUB;

            b23.T1 = b22.T1.modPow(RR2, paillier.nsquare);
            b23.T2 = b22.T2.modPow(RR2, paillier.nsquare);
            b23.PUB = b22.PUB;

            SAD2 SK111 = new SAD2 (a14, b13, a24, b23, pub, paillier);
            SK111.StepOne();
            SK111.StepTwo();
            SK111.StepThree();

            l11 = SK111.FIN1;
            l22 = SK111.FIN2;
            CCC = CCC + SK111.CCC;

        }else{

            a14.T1 = a12.T1.modPow(paillier.n.subtract(RR1), paillier.nsquare);
            a14.T2 = a12.T2.modPow(paillier.n.subtract(RR1), paillier.nsquare);
            a14.PUB = a12.PUB;

            b13.T1 = b12.T1.modPow(RR1, paillier.nsquare);
            b13.T2 = b12.T2.modPow(RR1, paillier.nsquare);
            b13.PUB = b12.PUB;

            a23.T1 = a22.T1.modPow(RR2, paillier.nsquare);
            a23.T2 = a22.T2.modPow(RR2, paillier.nsquare);
            a23.PUB = a22.PUB;

            b24.T1 = b22.T1.modPow(paillier.n.subtract(RR2), paillier.nsquare);
            b24.T2 = b22.T2.modPow(paillier.n.subtract(RR2), paillier.nsquare);
            b24.PUB = b22.PUB;

            SAD2 SK111 = new SAD2 (a14, b13, a23, b24, pub, paillier);
            SK111.StepOne();
            SK111.StepTwo();
            SK111.StepThree();

            l11 = SK111.FIN1;
            l22 = SK111.FIN2;
            CCC = CCC + SK111.CCC;
        }


        m11 = paillier.AddPDec1(l11);
        m22 = paillier.AddPDec1(l22);
        CCC = CCC + m11.T1.bitLength() + m11.T2.bitLength() + m11.T3.bitLength();
        CCC = CCC + m22.T1.bitLength() + m22.T2.bitLength() + m22.T3.bitLength();
    }

    public void StepTwo() {

        m1 = paillier.AddPDec2(m11);
        m2 = paillier.AddPDec2(m22);

        l = new BigInteger(paillier.n.bitLength() / 2, new Random());

        if (m1.compareTo(l) == 1) {
            U1 = paillier.Encryption(ZERO, pub);
        } else if (m1.compareTo(l) == -1) {
            U1 = paillier.Encryption(EONE, pub);
        } else {
            U1 = paillier.Encryption(paillier.n.subtract(EONE), pub);
        }

        if (m2.compareTo(l) == 1) {
            U2 = paillier.Encryption(ZERO, pub);
        } else if (m2.compareTo(l) == -1) {
            U2 = paillier.Encryption(EONE, pub);
        } else {
            U2 = paillier.Encryption(paillier.n.subtract(EONE), pub);
        }

        CCC = CCC + U1.T1.bitLength() + U1.T2.bitLength();
        CCC = CCC + U2.T1.bitLength() + U2.T2.bitLength();
    }

    public void StepThree() {

        if (s1 == 1) {
            FIN1 = U1;
        } else {
            FIN1.T1 = EUone.T1.multiply((U1.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare))).mod(paillier.nsquare);
            FIN1.T2 = EUone.T2.multiply((U1.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare))).mod(paillier.nsquare);
            FIN1.PUB = pub;
        }

        if (s2 == 1) {
            FIN2 = U2;
        } else {
            FIN2.T1 = EUone.T1.multiply((U2.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare))).mod(paillier.nsquare);
            FIN2.T2 = EUone.T2.multiply((U2.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare))).mod(paillier.nsquare);
            FIN2.PUB = pub;
        }

        SM SK11 = new SM(FIN1, FIN2, pub, paillier);

        SK11.StepOne();
        SK11.StepTwo();
        SK11.StepThree();

        FIN = SK11.FIN;
        CCC = CCC+ SK11.CCC;
    }






}
