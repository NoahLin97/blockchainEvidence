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
public class KMP0 {
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
	KET SK0;
	WTF SK1;
	WTB SK2;
	WTM SK3;
	
	CipherPub EZERO;	
	long t1, t2, t3, t4;

	public KMP0(CipherPub _VA, CipherPub _VB1, CipherPub _VB2, int _num, int _numchar, BigInteger _pub, PaillierT _paillier) {
		a = _VA;
		b1 = _VB1;
		b2 = _VB2;
		num = _num;
		numchar = _numchar;
		paillier = _paillier;
		pub = _pub;
	}

	public KMP0(CipherPub _VA, CipherPub _VB1, CipherPub _VB2, int _num, int _numchar, PaillierT _paillier) {
		a = _VA;
		b1 = _VB1;
		b2 = _VB2;
		num = _num;
		numchar = _numchar;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}

	public void StepOne() {
		EZERO = paillier.Encryption(BigInteger.ZERO, pub);	
		
		FIN = paillier.Encryption(BigInteger.ZERO, pub);
		
		if(num ==-1 & numchar ==-1){
			SK0 = new KET(a, b1, paillier);
			t1 = System.currentTimeMillis();
			SK0.StepOne();
			t2 = System.currentTimeMillis();
			SK0.StepTwo();
			t3 = System.currentTimeMillis();
			SK0.StepThree();
			t4 = System.currentTimeMillis();
			FIN = SK0.FIN;
			
			CCC = SK0.CCC;
			timeCP = timeCP + (t2 - t1) + (t4 - t3);
			timeCSP = timeCSP + (t3 - t2);
			timeTotal = timeCP + timeCSP;
		}
		else if(num >0 & numchar ==-1){
			SK1 = new WTF(a, b1, num, paillier);
			SK1.StepOne();
			FIN = SK1.FIN;
			
			CCC = SK1.CCC;
			timeCP = SK1.timeCP;
			timeCSP = SK1.timeCSP;
			timeTotal = timeCP + timeCSP;
		}
		else if(num ==-1 & numchar >0){
			SK2 = new WTB(a, b1, numchar, paillier);
			SK2.StepOne();
			FIN = SK2.FIN;
			
			CCC = SK2.CCC;
			timeCP = SK2.timeCP;
			timeCSP = SK2.timeCSP;
			timeTotal = timeCP + timeCSP;
		}
		else if(num >0 & numchar >0 ){
			SK3 = new WTM(a, b1, b2, num, numchar, paillier);
			SK3.StepOne();
			FIN = SK3.FIN;
			
			CCC = SK3.CCC;
			timeCP = SK3.timeCP;
			timeCSP = SK3.timeCSP;
			timeTotal = timeCP + timeCSP;
		}
	}
}
