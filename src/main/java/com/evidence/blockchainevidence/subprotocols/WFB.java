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
public class WFB {
	//input
	CipherPub X = new CipherPub();
	CipherPub Y1 = new CipherPub();
	int num1;
	int numchar1;
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;
	
	
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
	
	CipherPub EZERO;	
	CipherPub[] s;	
	CipherPub X1 = new CipherPub();	
	CipherPub X2 = new CipherPub();	
	CipherPub X3 = new CipherPub();	
	CipherPub X4 = new CipherPub();	
	int i;
	long t1, t2, t3, t4;

	public WFB(CipherPub _VA, CipherPub _VB1,
			int _num1, int _numchar1, BigInteger _pub, PaillierT _paillier) {
		X = _VA;
		Y1 = _VB1;
		num1 = _num1;
		numchar1 = _numchar1;
		paillier = _paillier;
		pub = _pub;
	}

	public WFB(CipherPub _VA, CipherPub _VB1,  
			int _num1, int _numchar1, PaillierT _paillier) {
		X = _VA;
		Y1 = _VB1;
		num1 = _num1;
		numchar1 = _numchar1;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}

	public void StepOne() {

		s = new CipherPub [num1+1];
		for (i=0; i<num1+1; i++)
		{
			s[i] = new CipherPub();
		}
		
		
		
		EZERO = paillier.Encryption(BigInteger.ZERO, pub);	
		
		FIN = paillier.Encryption(BigInteger.ZERO, pub);	
		
		
		for(i=0; i<=num1; i++){
			SK1 = new SCP(X,8*i,paillier);
			SK1.StepOne();
			X1 = SK1.FIN[0];
			X2 = SK1.FIN[1];
			
			timeCP = timeCP + SK1.timeCP;
			timeCSP = timeCSP + SK1.timeCSP;
			CCC = CCC + SK1.CCC;
			
			SK1 = new SCP(X2,8*numchar1,paillier);
			SK1.StepOne();
			X3 = SK1.FIN[0];
			X4 = SK1.FIN[1];
			
			timeCP = timeCP + SK1.timeCP;
			timeCSP = timeCSP + SK1.timeCSP;
			CCC = CCC + SK1.CCC;
			
			
			SK2 = new KET(X3,Y1,paillier);
			t1 = System.currentTimeMillis();
			SK2.StepOne();
			t2 = System.currentTimeMillis();
			SK2.StepTwo();
			t3 = System.currentTimeMillis();
			SK2.StepThree();
			t4 = System.currentTimeMillis();
			s[i] = SK2.FIN;
			
			CCC = CCC + SK2.CCC;
			timeCP = timeCP + (t2 - t1) + (t4 - t3);
			timeCSP = timeCSP + (t3 - t2);
			
			FIN.T1 = FIN.T1.multiply(s[i].T1);
			FIN.T2 = FIN.T2.multiply(s[i].T2);
			FIN.PUB = pub;
		}
		
		
		
		SK3 = new SLT(EZERO, FIN, paillier);
		t1 = System.currentTimeMillis();
		SK3.StepOne();
		t2 = System.currentTimeMillis();
		SK3.StepTwo();
		t3 = System.currentTimeMillis();
		SK3.StepThree();
		t4 = System.currentTimeMillis();
		FIN = SK3.FIN;
		
		CCC = CCC + SK3.CCC;
		timeCP = timeCP + (t2 - t1) + (t4 - t3);
		timeCSP = timeCSP + (t3 - t2);
		
		timeTotal = timeCP + timeCSP;
	}
}
