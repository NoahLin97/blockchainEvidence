/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.subprotocols;

import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.helib.SLT;

import java.math.BigInteger;

/**
 * /**
 *
 * @author Yang Yang
 */
public class WTB {
	//input
	CipherPub a = new CipherPub();
	CipherPub b = new CipherPub();
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;
	int numchar;
	
	//output
	public CipherPub FIN = new CipherPub();
	public long timeCP = 0; 
	public long timeCSP = 0; 
	public long timeTotal = 0; 
	public long CCC = 0;
	
	//temporary variables
	SCP SK1;
	KET SK2;
	SLT SK3;
	
	long t1, t2, t3, t4;

	public WTB(CipherPub _VA, CipherPub _VB, int _numchar, BigInteger _pub, PaillierT _paillier) {
		a = _VA;
		b = _VB;
		numchar = _numchar;
		paillier = _paillier;
		pub = _pub;
	}

	public WTB(CipherPub _VA, CipherPub _VB, int _numchar, PaillierT _paillier) {
		a = _VA;
		b = _VB;
		numchar = _numchar;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}

	public void StepOne() {

		
		
		FIN = paillier.Encryption(BigInteger.ZERO, pub);	
		
		
		SK1 = new SCP(a,8*numchar,paillier);
		SK1.StepOne();
		timeCP = timeCP + SK1.timeCP;
		timeCSP = timeCSP + SK1.timeCSP;
		CCC = CCC + SK1.CCC;
		
		
		SK2 = new KET(SK1.FIN[0],b,paillier);
		t1 = System.currentTimeMillis();
		SK2.StepOne();
		t2 = System.currentTimeMillis();
		SK2.StepTwo();
		t3 = System.currentTimeMillis();
		SK2.StepThree();
		t4 = System.currentTimeMillis();
		FIN = SK2.FIN;
		
		CCC = CCC + SK2.CCC;
		timeCP = timeCP + (t2 - t1) + (t4 - t3);
		timeCSP = timeCSP + (t3 - t2);
		timeTotal = timeCP + timeCSP;
	}
}
