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
public class KMP {
	//input
	CipherPub X = new CipherPub();
	CipherPub Y1 = new CipherPub();
	CipherPub Y2 = new CipherPub();
	CipherPub Y3 = new CipherPub();
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;
	int num1;
	int num2;
	int numchar1;
	int numchar2;
	int flag;
	
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
	
	WFM SK4;
	WFB SK5;
	WMM SK6;
	WMB SK7;
	
	CipherPub EZERO;	
	long t1, t2, t3, t4;

	public KMP(CipherPub _VA, CipherPub _VB1, CipherPub _VB2, CipherPub _VB3, 
			int _num1, int _num2, int _numchar1, int _numchar2, int _flag, 
			BigInteger _pub, PaillierT _paillier) {
		X = _VA;
		Y1 = _VB1;
		Y2 = _VB2;
		Y3 = _VB3;
		num1 = _num1;
		num2 = _num2;
		numchar1 = _numchar1;
		numchar2 = _numchar2;
		flag = _flag;
		paillier = _paillier;
		pub = _pub;
	}

	public KMP(CipherPub _VA, CipherPub _VB1, CipherPub _VB2, CipherPub _VB3, 
			int _num1, int _num2, int _numchar1, int _numchar2, int _flag,
			PaillierT _paillier) {
		X = _VA;
		Y1 = _VB1;
		Y2 = _VB2;
		Y3 = _VB3;
		num1 = _num1;
		num2 = _num2;
		numchar1 = _numchar1;
		numchar2 = _numchar2;
		flag = _flag;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}

	public void StepOne() {
		EZERO = paillier.Encryption(BigInteger.ZERO, pub);	
		
		FIN = paillier.Encryption(BigInteger.ZERO, pub);
		
		if(flag == 0){
			
			System.out.print("KMP: No *. Protocol: KET \n");
			
			SK0 = new KET(X, Y1, paillier);
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
		else if(flag == 1){
			if(num1 >0 & num2 ==-1 & numchar1 ==-1 & numchar2 ==-1){
				
				System.out.print("KMP: Front.  Protocol: WTF \n");
				
				SK1 = new WTF(X, Y1, num1, paillier);
				SK1.StepOne();
				FIN = SK1.FIN;
				
				CCC = SK1.CCC;
				timeCP = SK1.timeCP;
				timeCSP = SK1.timeCSP;
				timeTotal = timeCP + timeCSP;
			}
			else if(num1 ==-1 & num2 ==-1 & numchar1 >0 & numchar2 ==-1){
				
				System.out.print("KMP: Front.  Protocol: WTB \n");
				
				SK2 = new WTB(X, Y1, numchar1, paillier);
				SK2.StepOne();
				FIN = SK2.FIN;
				
				CCC = SK2.CCC;
				timeCP = SK2.timeCP;
				timeCSP = SK2.timeCSP;
				timeTotal = timeCP + timeCSP;
			}
			else if(num1 >0 & num2 ==-1 & numchar1 >0 & numchar2 ==-1){
				
				System.out.print("KMP: Middle.  Protocol: WTM \n");
				
				SK3 = new WTM(X, Y1, Y2, num1, numchar1, paillier);
				SK3.StepOne();
				FIN = SK3.FIN;
				
				CCC = SK3.CCC;
				timeCP = SK3.timeCP;
				timeCSP = SK3.timeCSP;
				timeTotal = timeCP + timeCSP;
			}
		}
		else if(flag == 2){
			if(num1 >0 & num2 >0 & numchar1 >0 & numchar2 ==-1){
				
				System.out.print("KMP: Front and Middle.  Protocol: WFM \n");
				
				SK4 = new WFM(X, Y1, Y2, num1, num2, numchar1, paillier);
				SK4.StepOne();
				FIN = SK4.FIN;
				
				CCC = SK4.CCC;
				timeCP = SK4.timeCP;
				timeCSP = SK4.timeCSP;
				timeTotal = timeCP + timeCSP;
			}
			else if(num1 >0 & num2 ==-1 & numchar1 >0 & numchar2 ==-1){
				
				System.out.print("KMP: Front and Back.  Protocol: WFB \n");
				
				SK5 = new WFB(X, Y1, num1, numchar1, paillier);
				SK5.StepOne();
				FIN = SK5.FIN;
				
				CCC = SK5.CCC;
				timeCP = SK5.timeCP;
				timeCSP = SK5.timeCSP;
				timeTotal = timeCP + timeCSP;
			}
			else if(num1 >0 & num2 >0 & numchar1 >0 & numchar2 >0){
				
				System.out.print("KMP: Middle and Middle.  Protocol: WMM \n");
				
				SK6 = new WMM(X, Y1, Y2, Y3, num1, num2, numchar1, numchar2, paillier);
				SK6.StepOne();
				FIN = SK6.FIN;
				
				CCC = SK6.CCC;
				timeCP = SK6.timeCP;
				timeCSP = SK6.timeCSP;
				timeTotal = timeCP + timeCSP;
			}
			else if(num1 >0 & num2 ==-1 & numchar1 >0 & numchar2 >0){
				
				System.out.print("KMP: Middle and Back.  Protocol: WMB \n");
				
				SK7 = new WMB(X, Y1, Y2, num1, numchar1, numchar2, paillier);
				SK7.StepOne();
				FIN = SK7.FIN;
				
				CCC = SK7.CCC;
				timeCP = SK7.timeCP;
				timeCSP = SK7.timeCSP;
				timeTotal = timeCP + timeCSP;
			}
		}
	}
}
