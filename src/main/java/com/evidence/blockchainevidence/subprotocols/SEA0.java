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
public class SEA0 {
	//input
	String S;
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;

	
	//output
	public CipherPub Y1 = new CipherPub();
	public CipherPub Y2 = new CipherPub();
	public int num;
	public int numchar;
	public int FIN;			
	//???????FIN=0????????????*???FIN=1????????????*??
	
	public long timeCP = 0; 
	public long timeCSP = 0; 
	public long timeTotal = 0; 
	public long CCC = 0;
	
	//temporary variables
	K2C8 SK0;
	WTF SK1;
	WTB SK2;
	WTM SK3;

	CipherPub EZERO;
	int i;
	long t1, t2, t3, t4;
	
	int NUM = 3;		//??????*????????????????

	public SEA0(String _S, int _num, BigInteger _pub, PaillierT _paillier) {
		S = _S;
		num = _num;
		paillier = _paillier;
		pub = _pub;
	}

	public SEA0(String _S, int _num, PaillierT _paillier) {
		S = _S;
		num = _num;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}
	
	public SEA0(String _S, BigInteger _pub, PaillierT _paillier) {
		S = _S;
		num = NUM;
		paillier = _paillier;
		pub = _pub;
	}

	public SEA0(String _S, PaillierT _paillier) {
		S = _S;
		num = NUM;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}

	public void StepOne() {
		
		EZERO = paillier.Encryption(BigInteger.ZERO, pub);
		
		//?????????*??
		if (S.indexOf("*")==-1){
			FIN = 0;	
			
			SK0 = new K2C8(S, pub, paillier);
			SK0.StepOne();
			Y1 = SK0.FIN;
			
			Y2 = EZERO;
			num = -1;
			numchar = -1;
		}	
		//???????*?????????
		else if(S.indexOf("*")==0){
			FIN = 1;
			
			String[] SubS = S.split("[*]");
			
			SK0 = new K2C8(SubS[1], pub, paillier);
			SK0.StepOne();
			Y1 = SK0.FIN;
			
			Y2 = EZERO;
			numchar = -1;
		}
		//???????*?????????
		else if(S.indexOf("*")==S.length()-1){
			FIN = 1;
			
			String[] SubS = S.split("[*]");
			
			SK0 = new K2C8(SubS[0], pub, paillier);
			SK0.StepOne();
			Y1 = SK0.FIN;
			
			Y2 = EZERO;
			num = -1;
			numchar = S.length()-1;
		}
		//???????*?????м?
		else{
			FIN = 1;
			
			String[] SubS = S.split("[*]");
			
			SK0 = new K2C8(SubS[0], pub, paillier);
			SK0.StepOne();
			Y1 = SK0.FIN;
			
			SK0 = new K2C8(SubS[1], pub, paillier);
			SK0.StepOne();
			Y2 = SK0.FIN;
			
			numchar = S.indexOf("*");
		}
	}
}
