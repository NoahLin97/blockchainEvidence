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
 *
 * @author Yang Yang
 */
public class MBE {
	public CipherPub a = new CipherPub();
	public int size;
	public PaillierT paillier = null;
	
	public BigInteger EONE= BigInteger.ONE;
	public BigInteger POW;
	public BigInteger RR = BigInteger.ZERO;
	public BigInteger RR1 = BigInteger.ZERO;
	public CipherPub ERR = new CipherPub();
	public CipherPub ERR1 = new CipherPub();
	public CipherPub ERR2 = new CipherPub();
	
	public CipherPub a11 = new CipherPub();
	public CipherPub EA = new CipherPub();
	public CipherPub EB = new CipherPub();
	public CipherPub U = new CipherPub();
	public CipherPub U1 = new CipherPub();
	
	public Ciphertext1 Y = new Ciphertext1();
	public BigInteger y = BigInteger.ZERO;
	public BigInteger y1 = BigInteger.ZERO;
	public CipherPub Y1 = new CipherPub();
	
	public CipherPub FIN = new CipherPub();
		
	public long CCC = 0;
	public long TB = 0;

	//_I should smaller than N
	public MBE(CipherPub _VA, int _I, PaillierT _paillier) {
		a = _VA;
		size = _I;
		paillier = _paillier;
	}

	public void StepOne() {

		RR = new BigInteger(200, new Random());
		ERR = paillier.Encryption(RR, a.PUB);

		EA.T1 = a.T1.multiply(ERR.T1);
		EA.T2 = a.T2.multiply(ERR.T2);
		EA.PUB = a.PUB;

		Y = paillier.AddPDec1(EA);

		CCC = CCC + Y.T1.bitLength() + Y.T2.bitLength() + Y.T3.bitLength();
	}

	public void StepTwo() {
		y = paillier.AddPDec2(Y);
		
		POW = BigInteger.valueOf(2).modPow(BigInteger.valueOf(size), paillier.n);
		y1 = y.mod(POW).add(POW);		
		Y1 = paillier.Encryption(y1, a.PUB);
		
		CCC = CCC + Y1.T1.bitLength() + Y1.T2.bitLength();
	}

	public void StepThree() {
		RR1 = RR.mod(POW);
		ERR1 = paillier.Encryption(RR1, a.PUB);
		
		ERR2.T1 = ERR1.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
		ERR2.T2 = ERR1.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
		ERR2.PUB = a.PUB;
		
		EB = paillier.Encryption(POW, a.PUB);
		
		a11.T1 = Y1.T1.multiply(ERR2.T1);
		a11.T2 = Y1.T2.multiply(ERR2.T2);
		a11.PUB = a.PUB;
		
		LT SK11 = new LT(a11, EB, paillier);
		
		SK11.StepOne();
		SK11.StepTwo();
		SK11.StepThree();

		U = SK11.FIN;
		
		CCC = CCC + SK11.CCC;
		
		U1.T1 = U.T1.modPow(paillier.n.subtract(POW), paillier.nsquare);
		U1.T2 = U.T2.modPow(paillier.n.subtract(POW), paillier.nsquare);
		U1.PUB = a.PUB;
		
		FIN.T1 = a11.T1.multiply(U1.T1);
		FIN.T2 = a11.T2.multiply(U1.T2);
		FIN.PUB = a.PUB;	
	}
}
