/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.subprotocols;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.CipherPub2;
import com.evidence.blockchainevidence.PaillierT.Ciphertext1;
import com.evidence.blockchainevidence.PaillierT.PaillierT;

import java.math.BigInteger;
import java.util.Random;
/**
/**
 *
 * @author Yang Yang
 */
public class TOPK {
	//input
	CipherPub2[] TT;
	int KK;
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;
	
	//output
	public CipherPub2 [] TMAX;
	
	public long CCC = 0;
	public long timeCP = 0; 
	public long timeCSP = 0; 
	public long timeTotal = 0;	
	
	//temporary variable	
	
	CipherPub2 [] SB;
	
	MAXN SK1;
	SAD SK2;
	SM SK3;
	
	BigInteger RR;
	CipherPub temp1 = new CipherPub();
	CipherPub temp2 = new CipherPub();
	CipherPub [] V;	
	Ciphertext1[] DO_V;
	BigInteger [] DT_V;
	CipherPub [] A;
	
	long t1, t2, t3, t4;
	
	int i,j;
	
	int SBsize;


	//第二个参数是k
	public TOPK(CipherPub2 []  _VA, int _I, PaillierT _paillier) {
	    TT  =  _VA;
	    KK = _I;
	    paillier=_paillier;
	    pub = paillier.Hsigma;
	}     
    
    public void StepOne (){ 
    	
		//TMAX初始化
    	TMAX = new CipherPub2[KK];
    	for(i=0; i<KK; i++){
    		TMAX[i] = new CipherPub2();
    		TMAX[i].EI = new CipherPub();
    		TMAX[i].EID = new CipherPub();
    		TMAX[i].EK = new CipherPub();
    	}
    	
		//V,A初始化
    	A = new CipherPub[TT.length];
    	V = new CipherPub[TT.length];
    	DO_V = new Ciphertext1[TT.length];
    	DT_V = new BigInteger[TT.length];
    	for(i=0; i<TT.length; i++){
    		A[i] = new CipherPub();
    		V[i] = new CipherPub();
    		DO_V[i] = new Ciphertext1();
    		DT_V[i] = BigInteger.ZERO;
    	}
    	
		SBsize = TT.length;   		
		//SB初始化
    	SB = new CipherPub2[SBsize];
    	for(i=0; i<SBsize; i++){
    		SB[i] = new CipherPub2();
    		SB[i].EI = new CipherPub();
    		SB[i].EID = new CipherPub();
    		SB[i].EK = new CipherPub();
    	}
    	
    	//SB赋初始值
    	for(i=0; i<SBsize; i++){
    		SB[i].EI = TT[i].EI;
    		SB[i].EID = TT[i].EID;
    		SB[i].EK = TT[i].EK;
    	}
    	
    	
    	//开始for大循环，找最大值
    	for(i=1; i<=KK; i++){
    		SK1 = new MAXN(SB, paillier);
    		SK1.StepOne();
    		TMAX[i-1] = SK1.TMAX;
			
        	CCC = SK1.CCC;
        	timeCP = timeCP + SK1.timeCP;
        	timeCSP = timeCSP + SK1.timeCSP;
    		
    		for(j=0; j<SB.length; j++){
    			
    			t1 = System.currentTimeMillis(); 
    	    	
    			RR = new BigInteger(200, new Random());
    			temp1.T1 = TMAX[i-1].EID.T1.modPow(RR, paillier.nsquare);
    			temp1.T2 = TMAX[i-1].EID.T2.modPow(RR, paillier.nsquare);
    			temp1.PUB = pub;
    			
    			temp2.T1 = SB[j].EID.T1.modPow(paillier.n.subtract(RR), paillier.nsquare);
    			temp2.T2 = SB[j].EID.T2.modPow(paillier.n.subtract(RR), paillier.nsquare);
    			temp2.PUB = pub;        
    			
    	        t2 = System.currentTimeMillis();
    	        timeCP = timeCP + (t2-t1);
    			
    			SK2 = new SAD(temp1, temp2, paillier);
    			SK2.StepOne();
    			SK2.StepTwo();
    			SK2.StepThree();
    			V[j] = SK2.FIN;    	
    			
            	CCC = SK2.CCC;
            	timeCP = timeCP + SK2.timeCP;
            	timeCSP = timeCSP + SK2.timeCSP;
    			
            	t1 = System.currentTimeMillis();
            	
    			DO_V[j] = paillier.AddPDec1(V[j]);
    			
    			t2 = System.currentTimeMillis();
    	        timeCP = timeCP + (t2-t1);
    			
    			t1 = System.currentTimeMillis();
    			
    			DT_V[j] = paillier.AddPDec2(DO_V[j]); 
    			
    			t2 = System.currentTimeMillis();
    	        timeCP = timeCSP + (t2-t1);

    			if(DT_V[j].equals(BigInteger.ZERO)){
    				t1 = System.currentTimeMillis();
    				
    				A[j] = paillier.Encryption(BigInteger.ZERO, pub);
    				
    				t2 = System.currentTimeMillis();
        	        timeCP = timeCSP + (t2-t1);
    			} else{
    				t1 = System.currentTimeMillis();
    				
    				A[j] = paillier.Encryption(BigInteger.ONE, pub);
    				
    				t2 = System.currentTimeMillis();
        	        timeCP = timeCSP + (t2-t1);
    			}
    			
    			SK3 = new SM(SB[j].EI, A[j], paillier);
            	t1 = System.currentTimeMillis();
            	SK3.StepOne();
            	t2 = System.currentTimeMillis();
            	SK3.StepTwo();
            	t3 = System.currentTimeMillis();
            	SK3.StepThree();
            	t4 = System.currentTimeMillis();
    			SB[j].EI = SK3.FIN;    
    			
            	CCC = SK3.CCC;
            	timeCP = timeCP + (t2-t1) + (t4-t3);
            	timeCSP = timeCSP + (t3-t2);
    		}
    	}   
    	timeTotal = timeCP + timeCSP;
    }
}
