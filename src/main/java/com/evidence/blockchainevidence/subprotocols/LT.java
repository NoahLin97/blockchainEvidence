/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.subprotocols;

import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.Ciphertext1;
import com.evidence.blockchainevidence.PaillierT.PaillierT;

import java.math.BigInteger;
import java.util.Random;

/**
 * /**
 *
 * @author Yang Yang
 */

//greater than
public class LT {
	public CipherPub a = new CipherPub();
	public CipherPub b = new CipherPub();
	public CipherPub a11 = new CipherPub();
	public CipherPub b11 = new CipherPub();
	public CipherPub a12 = new CipherPub();
	public CipherPub b12 = new CipherPub();
	public CipherPub a13 = new CipherPub();
	public CipherPub b13 = new CipherPub();
	public CipherPub l1 = new CipherPub();
	public CipherPub l2 = new CipherPub();
	public CipherPub U = new CipherPub();
	public CipherPub EZERO = new CipherPub();
	public CipherPub EEone = new CipherPub();
	public CipherPub FIN = new CipherPub();
	public Ciphertext1 m11 = new Ciphertext1();

	public BigInteger pub = BigInteger.ZERO;
	public PaillierT paillier = null;
	public BigInteger RR1 = BigInteger.ZERO;
	public BigInteger EONE = BigInteger.ONE;
	public BigInteger ZERO = BigInteger.ZERO;
	public BigInteger TWO = BigInteger.ZERO;
	public BigInteger l = BigInteger.ZERO;
	public BigInteger m1 = BigInteger.ZERO;
	public int s = 0;
	public long CCC = 0;

	
	//Require _VA and _VB have the same Pub
	public LT(CipherPub _VA, CipherPub _VB, PaillierT _paillier) {
		a = _VA;
		b = _VB;
		paillier = _paillier;
		pub = a.PUB;
	}

	public void StepOne() {

		TWO = new BigInteger("2");
		EEone = paillier.Encryption(EONE, pub);
		EZERO = paillier.Encryption(ZERO, pub);

		a12.T1 = (a.T1.modPow(TWO, paillier.nsquare)).multiply(EEone.T1).mod(paillier.nsquare);
		a12.T2 = (a.T2.modPow(TWO, paillier.nsquare)).multiply(EEone.T2).mod(paillier.nsquare);
		;
		a12.PUB = pub;

		b12.T1 = b.T1.modPow(TWO, paillier.nsquare);
		b12.T2 = b.T2.modPow(TWO, paillier.nsquare);
		b12.PUB = pub;

		RR1 = new BigInteger(200, new Random());

		Random rand = new Random();
		s = rand.nextInt(100000000) % 2;

		if (s == 1) {
			b13.T1 = b12.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
			b13.T2 = b12.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
			b13.PUB = pub;

			l2.T1 = a12.T1.multiply(b13.T1);
			l2.T2 = a12.T2.multiply(b13.T2);
			l2.PUB = pub;

			l1.T1 = (l2.T1).modPow(RR1, paillier.nsquare);
			l1.T2 = (l2.T2).modPow(RR1, paillier.nsquare);
			l1.PUB = pub;
		} else {
			a13.T1 = a12.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
			a13.T2 = a12.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
			a13.PUB = a12.PUB;

			l2.T1 = a13.T1.multiply(b12.T1);
			l2.T2 = a13.T2.multiply(b12.T2);
			l2.PUB = pub;

			l1.T1 = (l2.T1).modPow(RR1, paillier.nsquare);
			l1.T2 = (l2.T2).modPow(RR1, paillier.nsquare);
			l1.PUB = pub;
		}

		m11 = paillier.AddPDec1(l1);
		CCC = CCC + m11.T1.bitLength() + m11.T2.bitLength() + m11.T3.bitLength();
	}

	public void StepTwo() {

		m1 = paillier.AddPDec2(m11);

		l = new BigInteger(paillier.n.bitLength() / 2, new Random());
		

		if (m1.compareTo(l) == 1) {
			U = paillier.Encryption(ZERO, pub);
		} else if (m1.compareTo(l) == -1) {
			U = paillier.Encryption(EONE, pub);
		} else {
			U = paillier.Encryption(paillier.n.subtract(EONE), pub);
		}

		CCC = CCC + U.T1.bitLength() + U.T2.bitLength();
	}

	public void StepThree() {

		if (s == 1) {
			FIN = paillier.Refreash(U);
		}

		if (s == 0) {
			FIN.T1 = EEone.T1.multiply((U.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare))).mod(paillier.nsquare);
			FIN.T2 = EEone.T2.multiply((U.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare))).mod(paillier.nsquare);
			FIN.PUB = pub;
		}

	}

}
