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
public class WMM {
	//input
	CipherPub X = new CipherPub();
	CipherPub Y1 = new CipherPub();
	CipherPub Y2 = new CipherPub();
	CipherPub Y3 = new CipherPub();
	int num1;
	int num2;
	int numchar1;
	int numchar2;
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
	SMD SK4;
	
	CipherPub EZERO;	
	CipherPub[] s;	
	CipherPub[] t;	
	CipherPub temp = new CipherPub();	
	CipherPub u1 = new CipherPub();	
	CipherPub X1 = new CipherPub();	
	CipherPub X2 = new CipherPub();	
	CipherPub X3 = new CipherPub();	
	CipherPub X4 = new CipherPub();	
	CipherPub X5 = new CipherPub();	
	CipherPub X6 = new CipherPub();	
	CipherPub X7 = new CipherPub();	
	CipherPub X8 = new CipherPub();	
	int i,j;
	long t1, t2, t3, t4;

	public WMM(CipherPub _VA, CipherPub _VB1, CipherPub _VB2, CipherPub _VB3, 
			int _num1, int _num2, int _numchar1, int _numchar2, 
			BigInteger _pub, PaillierT _paillier) {
		X = _VA;
		Y1 = _VB1;
		Y2 = _VB2;
		Y3 = _VB3;
		num1 = _num1;
		num2 = _num2;
		numchar1 = _numchar1;
		numchar2 = _numchar2;
		paillier = _paillier;
		pub = _pub;
	}

	public WMM(CipherPub _VA, CipherPub _VB1, CipherPub _VB2, CipherPub _VB3, 
			int _num1, int _num2, int _numchar1, int _numchar2,
			 PaillierT _paillier) {
		X = _VA;
		Y1 = _VB1;
		Y2 = _VB2;
		Y3 = _VB3;
		num1 = _num1;
		num2 = _num2;
		numchar1 = _numchar1;
		numchar2 = _numchar2;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}

	public void StepOne() {

		s = new CipherPub [num1+1];
		for (i=0; i<num1+1; i++)
		{
			s[i] = new CipherPub();
		}
		
		t = new CipherPub [num2+1];
		for (i=0; i<num2+1; i++)
		{
			t[i] = new CipherPub();
		}
		
		
		EZERO = paillier.Encryption(BigInteger.ZERO, pub);	
		FIN = paillier.Encryption(BigInteger.ZERO, pub);	
		
		SK1 = new SCP(X,8*numchar1,paillier);
		SK1.StepOne();
		X1 = SK1.FIN[0];
		X2 = SK1.FIN[1];
		
		timeCP = timeCP + SK1.timeCP;
		timeCSP = timeCSP + SK1.timeCSP;
		CCC = CCC + SK1.CCC;
		
		
		SK2 = new KET(X1,Y1,paillier);
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
		
		for(i=0; i<=num1; i++){
			SK1 = new SCP(X2,8*i,paillier);
			SK1.StepOne();
			X3 = SK1.FIN[0];
			X4 = SK1.FIN[1];
			
			timeCP = timeCP + SK1.timeCP;
			timeCSP = timeCSP + SK1.timeCSP;
			CCC = CCC + SK1.CCC;
			
			SK1 = new SCP(X4,8*numchar2,paillier);
			SK1.StepOne();
			X5 = SK1.FIN[0];
			X6 = SK1.FIN[1];
			
			timeCP = timeCP + SK1.timeCP;
			timeCSP = timeCSP + SK1.timeCSP;
			CCC = CCC + SK1.CCC;
			
			
			SK2 = new KET(X5,Y2,paillier);
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
			
			for(j=0; j<=num2; j++){
				SK1 = new SCP(X6,8*j,paillier);
				SK1.StepOne();
				X7 = SK1.FIN[0];
				X8 = SK1.FIN[1];
				
				timeCP = timeCP + SK1.timeCP;
				timeCSP = timeCSP + SK1.timeCSP;
				CCC = CCC + SK1.CCC;
				
				
				SK2 = new KET(X8,Y3,paillier);
				t1 = System.currentTimeMillis();
				SK2.StepOne();
				t2 = System.currentTimeMillis();
				SK2.StepTwo();
				t3 = System.currentTimeMillis();
				SK2.StepThree();
				t4 = System.currentTimeMillis();
				t[j] = SK2.FIN;
				
				CCC = CCC + SK2.CCC;
				timeCP = timeCP + (t2 - t1) + (t4 - t3);
				timeCSP = timeCSP + (t3 - t2);
				
				
				SK4 = new SMD(s[i],t[j],paillier);
				t1 = System.currentTimeMillis();
				SK4.StepOne();
				t2 = System.currentTimeMillis();
				SK4.StepTwo();
				t3 = System.currentTimeMillis();
				SK4.StepThree();
				t4 = System.currentTimeMillis();
				temp = SK4.FIN;
				
				CCC = CCC + SK4.CCC;
				timeCP = timeCP + (t2 - t1) + (t4 - t3);
				timeCSP = timeCSP + (t3 - t2);
				
				SK4 = new SMD(temp,u1,paillier);
				t1 = System.currentTimeMillis();
				SK4.StepOne();
				t2 = System.currentTimeMillis();
				SK4.StepTwo();
				t3 = System.currentTimeMillis();
				SK4.StepThree();
				t4 = System.currentTimeMillis();
				temp = SK4.FIN;
				
				CCC = CCC + SK4.CCC;
				timeCP = timeCP + (t2 - t1) + (t4 - t3);
				timeCSP = timeCSP + (t3 - t2);
				
				FIN.T1 = FIN.T1.multiply(temp.T1);
				FIN.T2 = FIN.T2.multiply(temp.T2);
				FIN.PUB = pub;
			}
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
