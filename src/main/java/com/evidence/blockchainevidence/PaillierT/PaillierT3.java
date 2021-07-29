package com.evidence.blockchainevidence.PaillierT;

import java.math.*;
import java.util.*;

public class PaillierT3 {

    public static final int alpha = 2;
    public static final int beta = 3;
    public BigInteger p,  q,  lambda, x, x1, x2, h, a ,g1,KKK,KK1,S, Xsigma, Hsigma;
    //  public  BigInteger   lambda1;
    public BigInteger []   lambda1 =  new    BigInteger [alpha] ;
    public BigInteger []   X =  new    BigInteger [beta];
    public BigInteger []   H =  new    BigInteger [beta] ;
    //  public BigInteger []   X1 =  new    BigInteger [beta] ;
    //  public BigInteger []   X2 =  new    BigInteger [beta] ;

    // n = p*q, where p and q are two large primes.
    public BigInteger n;

    // nsquare = n*n
    public BigInteger nsquare;

    // a random integer in Z*_{n^2} where gcd (L(g^lambda mod n^2), n) = 1.
    private BigInteger g;

    // number of bits of modulus
    private int bitLength;

    public PaillierT3(int bitLengthVal, int certainty) {
        KeyGeneration(bitLengthVal, certainty);
    }

    /**
     * Constructs an instance of the Paillier cryptosystem with 512 bits of modulus and at least 1-2^(-64) certainty of primes generation.
     */
    public PaillierT3() {
        KeyGeneration(1024, 64);
    }

    public void KeyGeneration(int bitLengthVal, int certainty) {
        bitLength = bitLengthVal;
        /*Constructs two randomly generated positive BigIntegers that are probably prime, with the specified bitLength and certainty.*/
        p = new BigInteger(bitLength / 2, certainty, new Random());
        q = new BigInteger(bitLength / 2, certainty, new Random());

        a =    new BigInteger(bitLength / 2, certainty, new Random());

        n = p.multiply(q);
        nsquare = n.multiply(n);
        g1 = new BigInteger("2");
        g = BigInteger.ZERO.subtract(a.modPow(g1.multiply(n), nsquare)).mod(nsquare);



        x = new BigInteger(bitLength / 2, certainty, new Random());
        Xsigma = BigInteger.ZERO;


        x1 = new BigInteger(bitLength / 4, certainty, new Random());
        lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(
                p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
        KK1 = lambda.multiply(nsquare);
        KKK = lambda.modInverse(nsquare);
        S = lambda.multiply(KKK).mod(KK1);

        lambda1[alpha-1]= S;
        for(int ii =0; ii<(alpha-1);ii++)
        { lambda1[ii]= new BigInteger(bitLength  , certainty, new Random());

            lambda1[alpha-1]= lambda1[alpha-1].subtract(lambda1[ii]);
        }

        for(int ii =0 ; ii < beta ; ii++)
        {
            X[ii] = new BigInteger(bitLength -12, certainty, new Random());

            H[ii] = g.modPow(X[ii], nsquare);

            Xsigma = Xsigma.add(X[ii]);
        }

        Hsigma = g.modPow(Xsigma, nsquare);

        x2 = x.subtract(x1);

        h = g.modPow(x, nsquare);


        /* check whether g is good.*/
        //  if (g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) {
        //      System.out.println("g is not good. Choose g again.");
        //     System.exit(1);
        //  }
    }

    public CipherPub Encryption(BigInteger m, BigInteger h) {

        BigInteger r = new BigInteger(bitLength , new Random());


        CipherPub cc = new CipherPub();
        cc.T1 = (BigInteger.ONE.add(m.multiply(n)).mod(nsquare)).multiply(h.modPow(r, nsquare)).mod(nsquare);
        cc.T2 = g.modPow(r, nsquare);
        cc.PUB = h;
        //     System.out.println("xxxxx=="+m.multiply(n).multiply(lambda).add(BigInteger.ONE));
        return  cc;
    }

    public Ciphertext Encryption(BigInteger m) {
        BigInteger r = new BigInteger(bitLength, new Random());


        Ciphertext cc = new Ciphertext();
        cc.T1 = (BigInteger.ONE.add(m.multiply(n)).mod(nsquare)).multiply(h.modPow(r, nsquare)).mod(nsquare);
        cc.T2 = g.modPow(r, nsquare);
        //     System.out.println("xxxxx=="+m.multiply(n).multiply(lambda).add(BigInteger.ONE));
        return  cc;

    }

    public BigInteger WDecryption(Ciphertext c) {
        BigInteger u = c.T1.multiply( ((c.T2.modPow(x, nsquare)).modInverse(nsquare)));
        //    System.out.println("T1=="+c.T1);
        //    System.out.println("u=="+u);

        return u.subtract(BigInteger.ONE).divide(n).mod(n);
    }

    public BigInteger WDecryption(CipherPub c, BigInteger x) {
        BigInteger u = c.T1.multiply( ((c.T2.modPow(x, nsquare)).modInverse(nsquare)));
        //    System.out.println("T1=="+c.T1);
        //    System.out.println("u=="+u);

        return u.subtract(BigInteger.ONE).divide(n).mod(n);
    }

    public BigInteger[]  PSDecryption1(Ciphertext c) {
        BigInteger [] cc = new BigInteger [alpha];

        cc[0]  = c.T1.modPow(lambda1[0], nsquare);

        for(int ii =1; ii<(alpha);ii++)
        { cc[ii]  = c.T1.modPow(lambda1[ii], nsquare);

        }

        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));

        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);

        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));

        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }

    public BigInteger  DDecryption1(BigInteger[]  c) {

        BigInteger TT =  BigInteger.ONE;


        for(int ii =0; ii<(alpha);ii++)
        { TT = TT.multiply(c[ii]);
        }


        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));

        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);

        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));

        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return (TT.subtract(BigInteger.ONE).divide(n)).mod(n);
    }

    public BigInteger  WPDecryption1(BigInteger c, BigInteger x1) {
        BigInteger  cc =   BigInteger.ZERO ;
        //   BigInteger r = new BigInteger(bitLength , new Random());
        //      cc.T1 = c.T1.multiply((h.modPow(r, nsquare))).mod(nsquare);
        cc = c.modPow(x1, nsquare);



        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));

        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);

        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));

        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }

    public BigInteger  WPDecryption1(CipherPub c, BigInteger x1) {
        BigInteger  cc =   BigInteger.ZERO ;
        //   BigInteger r = new BigInteger(bitLength , new Random());
        //      cc.T1 = c.T1.multiply((h.modPow(r, nsquare))).mod(nsquare);
        cc = c.T2.modPow(x1, nsquare);



        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));

        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);

        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));

        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }

    public Ciphertext1 WPDecryption1(Ciphertext c) {
        Ciphertext1 cc = new Ciphertext1();
        BigInteger r = new BigInteger(bitLength , new Random());
        cc.T1 = c.T1.multiply((h.modPow(r, nsquare))).mod(nsquare);
        cc.T2 = c.T2.multiply((g.modPow(r, nsquare))).mod(nsquare);
        cc.T3 = cc.T2.modPow(x1, nsquare);


        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));

        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);

        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));

        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }

    public BigInteger WPDecryption2(CipherPub c, BigInteger [] ccc, int NUM, BigInteger x1) {

        BigInteger TT =  BigInteger.ONE;


        //   BigInteger r = new BigInteger(bitLength , new Random());
        //      cc.T1 = c.T1.multiply((h.modPow(r, nsquare))).mod(nsquare);
        TT = c.T2.modPow(x1, nsquare);

        for(int ii =0; ii < NUM-1; ii++)
        { TT = TT.multiply(ccc[ii]).modInverse(nsquare);
        }

        BigInteger u = c.T1.multiply( TT.modInverse(nsquare));
        //    System.out.println("T1=="+c.T1);
        //    System.out.println("u=="+u);

        return u.subtract(BigInteger.ONE).divide(n).mod(n);
        //   BigInteger u1 =    c.T3.multiply(c.T2.modPow(x2, nsquare)).mod(nsquare);

        //        BigInteger u = c.T1.multiply( ((c.T2.modPow(x2, nsquare)).modInverse(nsquare)));

        //  BigInteger u = c.T1.multiply(u1.modInverse(nsquare));


//    System.out.println("T1=="+c.T1);
        //    System.out.println("u=="+u);

        //    return TT.subtract(BigInteger.ONE).divide(n).mod(n);
    }
    public BigInteger WPDecryption2(Ciphertext c, BigInteger [] ccc, int NUM) {

        BigInteger TT =  BigInteger.ONE;


        for(int ii =0; ii < NUM; ii++)
        { TT = TT.multiply(ccc[ii]).modInverse(nsquare);
        }



        //   BigInteger u1 =    c.T3.multiply(c.T2.modPow(x2, nsquare)).mod(nsquare);

        //        BigInteger u = c.T1.multiply( ((c.T2.modPow(x2, nsquare)).modInverse(nsquare)));

        //  BigInteger u = c.T1.multiply(u1.modInverse(nsquare));


//    System.out.println("T1=="+c.T1);
        //    System.out.println("u=="+u);

        return TT.subtract(BigInteger.ONE).divide(n).mod(n);
    }

    public BigInteger WPDecryption2(Ciphertext1 c) {


        BigInteger u1 =    c.T3.multiply(c.T2.modPow(x2, nsquare)).mod(nsquare);
        BigInteger u = c.T1.multiply(u1.modInverse(nsquare));


//    System.out.println("T1=="+c.T1);
        //    System.out.println("u=="+u);

        return u.subtract(BigInteger.ONE).divide(n).mod(n);
    }

    public BigInteger SDecryption(Ciphertext c) {

        BigInteger u1 = lambda.modInverse(n);
        //    System.out.println("xxxxx=="+u);
        //    System.out.println("g=="+g.modPow(lambda, nsquare));
        return  c.T1.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u1).mod(n);
    }

    public BigInteger SDecryption(CipherPub c) {
        //  BigInteger u = c.T1.modPow(lambda, nsquare);
        BigInteger u1 = lambda.modInverse(n);
        //    System.out.println("xxxxx=="+u);
        //    System.out.println("g=="+g.modPow(lambda, nsquare));
        return  c.T1.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u1).mod(n);
    }



    public Ciphertext1   AddPDec1(Ciphertext c,  BigInteger hp) {
        Ciphertext1   cc =     new Ciphertext1  ();
        BigInteger r = new BigInteger(bitLength , new Random());
        cc.T1 = (c.T1.multiply((hp.modPow(r, nsquare))).mod(nsquare));
        cc.T2 = (c.T2.multiply((g.modPow(r, nsquare))).mod(nsquare));

        cc.T3 = cc.T1.modPow(lambda1[0], nsquare);



        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));
        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);
        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));
        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }

    public Ciphertext1   AddPDec1(CipherPub c) {
        Ciphertext1   cc =     new Ciphertext1  ();
        BigInteger r = new BigInteger(bitLength , new Random());
        cc.T1 = (c.T1.multiply((c.PUB.modPow(r, nsquare))).mod(nsquare));
        cc.T2 = (c.T2.multiply((g.modPow(r, nsquare))).mod(nsquare));

        cc.T3 = cc.T1.modPow(lambda1[0], nsquare);



        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));
        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);
        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));
        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }

    public Ciphertext1   AddPDec1(Ciphertext c) {
        Ciphertext1   cc =     new Ciphertext1  ();
        BigInteger r = new BigInteger(bitLength , new Random());
        cc.T1 = (c.T1.multiply((Hsigma.modPow(r, nsquare))).mod(nsquare));
        cc.T2 = (c.T2.multiply((g.modPow(r, nsquare))).mod(nsquare));

        cc.T3 = cc.T1.modPow(lambda1[0], nsquare);



        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));
        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);
        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));
        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }


    public BigInteger   AddPDec2(Ciphertext1 c) {
        BigInteger   cc =     BigInteger.ZERO; ;


        cc = (c.T1.modPow(lambda1[1], nsquare)).multiply(c.T3).mod(nsquare);



        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));
        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);
        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));
        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc.subtract(BigInteger.ONE).divide(n).mod(n);
    }


    public Ciphertext   Refreash(Ciphertext c, BigInteger hp) {
        Ciphertext    cc =     new Ciphertext   ();
        BigInteger r = new BigInteger(bitLength , new Random());
        cc.T1 = (c.T1.multiply((hp.modPow(r, nsquare))).mod(nsquare));
        cc.T2 = (c.T2.multiply((g.modPow(r, nsquare))).mod(nsquare));

        //  cc.T3 = cc.T1.modPow(lambda1[0], nsquare);



        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));
        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);
        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));
        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }


    public CipherPub   Refreash(CipherPub c) {
        CipherPub    cc =     new CipherPub   ();
        BigInteger r = new BigInteger(bitLength , new Random());
        cc.T1 = (c.T1.multiply((c.PUB.modPow(r, nsquare))).mod(nsquare));
        cc.T2 = (c.T2.multiply((g.modPow(r, nsquare))).mod(nsquare));
        cc.PUB = c.PUB;
        //  cc.T3 = cc.T1.modPow(lambda1[0], nsquare);



        //      BigInteger ccccc = cc.T1.multiply( ((cc.T2.modPow(x, nsquare)).modInverse(nsquare)));
        //       BigInteger cccccc = cc.T3.multiply(cc.T2.modPow(x2, nsquare)).mod(nsquare);
        //      BigInteger ccccck =  ((cc.T2.modPow((x1.add(x2)), nsquare)));
        //      System.out.println("ccccc=="+ ccccc);
        //      System.out.println("cccccc=="+ cccccc);
        //       System.out.println("ccccck=="+ ccccck);
        return cc;
    }


}
