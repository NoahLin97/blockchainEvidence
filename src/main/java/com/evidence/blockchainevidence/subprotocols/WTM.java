/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.subprotocols;

import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;

import java.math.BigInteger;

/**
 * /**
 *
 * @author Yang Yang
 */
public class WTM {
	//input
	CipherPub a = new CipherPub();
	CipherPub b1 = new CipherPub();
	CipherPub b2 = new CipherPub();
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;
	int num;
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
	WTF SK3;
	SMD SK4;
	
	CipherPub u1 = new CipherPub();
	CipherPub u2 = new CipherPub();
	long t1, t2, t3, t4;

	public WTM(CipherPub _VA, CipherPub _VB1, CipherPub _VB2, int _num, int _numchar, BigInteger _pub, PaillierT _paillier) {
		a = _VA;
		b1 = _VB1;
		b2 = _VB2;
		num = _num;
		numchar = _numchar;
		paillier = _paillier;
		pub = _pub;
	}

	public WTM(CipherPub _VA, CipherPub _VB1, CipherPub _VB2, int _num, int _numchar, PaillierT _paillier) {
		a = _VA;
		b1 = _VB1;
		b2 = _VB2;
		num = _num;
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
		
		SK2 = new KET(SK1.FIN[0],b1,paillier);
		t1 = System.currentTimeMillis();
		SK2.StepOne();
		t2 = System.currentTimeMillis();
		SK2.StepTwo();
		t3 = System.currentTimeMillis();
		SK2.StepThree();
		t4 = System.currentTimeMillis();
		u1 = SK2.FIN;
		
		CCC = CCC + SK2.CCC;
		timeCP = timeCP + (t2 - t1) + (t4 - t3);
		timeCSP = timeCSP + (t3 - t2);
		
		SK3 = new WTF(SK1.FIN[1],b2,num,paillier);
		SK3.StepOne();
		u2 = SK3.FIN;
		
		CCC = CCC + SK3.CCC;
		timeCP = timeCP + SK3.timeCP;
		timeCSP = timeCSP + SK3.timeCSP;
		
		SK4 = new SMD(u1,u2,paillier);
		t1 = System.currentTimeMillis();
		SK4.StepOne();
		t2 = System.currentTimeMillis();
		SK4.StepTwo();
		t3 = System.currentTimeMillis();
		SK4.StepThree();
		t4 = System.currentTimeMillis();
		FIN = SK4.FIN;
		
		CCC = CCC + SK4.CCC;
		timeCP = timeCP + (t2 - t1) + (t4 - t3);
		timeCSP = timeCSP + (t3 - t2);
		
		timeTotal = timeCP + timeCSP;
	}
}
