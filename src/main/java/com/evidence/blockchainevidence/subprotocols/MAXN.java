/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.subprotocols;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.CipherPub2;
import com.evidence.blockchainevidence.PaillierT.PaillierT;

import java.math.BigInteger;

/**
/**
 *
 * @author Yang Yang
 */
public class MAXN {
	//input
	CipherPub2[] TT;
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;
	
	//output
	public CipherPub2 TMAX = new CipherPub2();
	
	public long CCC = 0;
	public long timeCP = 0; 
	public long timeCSP = 0; 
	public long timeTotal = 0;	
	
	//temporary variable	
	
	CipherPub2 [] SA;
	CipherPub2 [] SB;
	
	MAX SK1;
	
	int i,j;
	int temp;
	int SAsize, SBsize;	
	
	long t1, t2, t3, t4;
	
	public MAXN(CipherPub2 []  _VA, PaillierT _paillier) {
	    TT  =  _VA;
	    paillier=_paillier;
	    pub = paillier.Hsigma;
	}     
    
    public void StepOne (){ 
    	
    	temp = (int) Math.ceil(Math.log(TT.length)/Math.log(2));
    	
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
    	
		//SA初始化
		if(SBsize%2 == 0){
			SAsize = SBsize/2;
		}
		else if(SBsize%2 == 1){
			SAsize = (SBsize+1)/2;
		}
    	
    	SA = new CipherPub2[SAsize];
    	for(i=0; i<SAsize; i++){
    		SA[i] = new CipherPub2();
    		SA[i].EI = new CipherPub();
    		SA[i].EID = new CipherPub();
    		SA[i].EK = new CipherPub();
    	}
    	
    	//开始for大循环，找最大值
    	for(i=1; i<=temp; i++){
    		
        	if(SBsize%2 == 0){
	    		for(j=0; j<SAsize; j++)
	    		{
	    			SK1 = new MAX(SB[2*j], SB[2*j+1], paillier);
	    			SK1.StepOne();
	    			SK1.StepTwo();
	    			SK1.StepThree();
	    			SA[j] = SK1.TMAX;
	    			
	            	CCC = SK1.CCC;
	            	timeCP = timeCP + SK1.timeCP;
	            	timeCSP = timeCSP + SK1.timeCSP;
	    		}			
			}
        	else if(SBsize%2 == 1){
	    		for(j=0; j<SAsize-1; j++)
	    		{
	    			SK1 = new MAX(SB[2*j], SB[2*j+1], paillier);
	    			SK1.StepOne();
	    			SK1.StepTwo();
	    			SK1.StepThree();
	    			SA[j] = SK1.TMAX;
	    			
	            	CCC = SK1.CCC;
	            	timeCP = timeCP + SK1.timeCP;
	            	timeCSP = timeCSP + SK1.timeCSP;
	    		}			
				SA[SAsize-1] = SB[SBsize-1];
			}
        	
        	if(i != temp){
	        	SBsize = SAsize;  				//SB=SA
	    		//SB初始化
	        	SB = new CipherPub2[SBsize];
	        	for(j=0; j<SBsize; j++){
	        		SB[j] = new CipherPub2();
	        		SB[j].EI = new CipherPub();
	        		SB[j].EID = new CipherPub();
	        		SB[j].EK = new CipherPub();
	        	}
	        	
	        	//SB=SA
	        	for(j=0; j<SBsize; j++){
	        		SB[j].EI = SA[j].EI;
	        		SB[j].EID = SA[j].EID;
	        		SB[j].EK = SA[j].EK;
	        	}
	        	
	    		//SA初始化
	    		if(SBsize%2 == 0){
	    			SAsize = SBsize/2;
	    		}
	    		else if(SBsize%2 == 1){
	    			SAsize = (SBsize+1)/2;
	    		}
    		
    		
    			SA = new CipherPub2[SAsize];
            	for(j=0; j<SAsize; j++){
            		SA[j] = new CipherPub2();
            		SA[j].EI = new CipherPub();
            		SA[j].EID = new CipherPub();
            		SA[j].EK = new CipherPub();
            	}    			
    		}
        	
        	TMAX = SA[0];
    	}
    	timeTotal = timeCP + timeCSP;
    }
}
