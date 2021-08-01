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
 *
 * @author Yang Yang
 */
public class SCP {
	//input
	public CipherPub a = new CipherPub();
	public int size;
	public PaillierT paillier = null;
	

	//output
	public CipherPub [] FIN = new CipherPub[2];
	
	public long timeCP = 0; 
	public long timeCSP = 0; 
	public long timeTotal = 0; 
	public long CCC = 0;
	

	//temp
	int i=0;
	
	BigInteger EONE= BigInteger.ONE;
	BigInteger POW;
	CipherPub T = new CipherPub();
	CipherPub Z = new CipherPub();
	long TB = 0;
	long t1, t2, t3, t4;
	
	

	//_I should smaller than N
	//_I: the bit numbers in X_1
	
	public SCP(CipherPub _VA, int _I, PaillierT _paillier) {
		a = _VA;
		size = _I;		
		paillier = _paillier;
	}

	public void StepOne() {			
		FIN[0] = new CipherPub();
		FIN[1] = new CipherPub();
		
		POW = BigInteger.valueOf(2).modPow(BigInteger.valueOf(size), paillier.n);
		POW = POW.modInverse(paillier.n);		

		T.T1 = a.T1;
		T.T2 = a.T2;
		T.PUB = a.PUB;
		
	
		MBE SK11 = new MBE(T, size, paillier);
		
		t1 = System.currentTimeMillis();
		SK11.StepOne();
		t2 = System.currentTimeMillis();
		SK11.StepTwo();
		t3 = System.currentTimeMillis();
		SK11.StepThree();
		t4 = System.currentTimeMillis();
								
		FIN[0] = SK11.FIN;
					
		Z.T1 = T.T1.multiply(FIN[0].T1.modPow(paillier.n.subtract(EONE), paillier.nsquare));
		Z.T2 = T.T2.multiply(FIN[0].T2.modPow(paillier.n.subtract(EONE), paillier.nsquare));
		Z.PUB = a.PUB;
		
		FIN[1].T1 = Z.T1.modPow(POW, paillier.nsquare);
		FIN[1].T2 = Z.T2.modPow(POW, paillier.nsquare);
		FIN[1].PUB = a.PUB;
		
		timeCP = timeCP + (t2 - t1) + (t4 - t3);
		timeCSP = timeCSP + (t3 - t2);
		timeTotal = timeCP + timeCSP;
		CCC = CCC + SK11.CCC;
	}
}
