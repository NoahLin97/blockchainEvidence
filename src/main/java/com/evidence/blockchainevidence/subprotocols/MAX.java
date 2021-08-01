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
public class MAX {
	//input
	CipherPub2 TA = new CipherPub2();
	CipherPub2 TB = new CipherPub2();
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;
	
	//output
	public CipherPub2 TMAX = new CipherPub2();
	
	public long CCC = 0;
	public long timeCP = 0; 
	public long timeCSP = 0; 
	public long timeTotal = 0;	
	
	//temporary variable	
	CipherPub IAA = new CipherPub();			//I_A'
	CipherPub IBB = new CipherPub();			//I_B'
	
	BigInteger ZERO= BigInteger.ZERO;
	BigInteger EONE= BigInteger.ONE;
	BigInteger TWO = new BigInteger("2");
	CipherPub  EUone = new CipherPub();
	CipherPub EZERO = new CipherPub();
	
	BigInteger R1; 
	BigInteger R2; 
	BigInteger R3; 
	BigInteger R4; 
	
	CipherPub  ER2 = new CipherPub();
	CipherPub  ER3 = new CipherPub();
	CipherPub  ER4 = new CipherPub();
	
	CipherPub  C1 = new CipherPub();
	CipherPub  C2 = new CipherPub();
	CipherPub  C3 = new CipherPub();
	CipherPub  C4 = new CipherPub();
	CipherPub  C5 = new CipherPub();
	CipherPub  C6 = new CipherPub();
	CipherPub  C7 = new CipherPub();
	
	Ciphertext1 DO_C1 = new Ciphertext1();
	BigInteger DT_C1 = BigInteger.ZERO;
	
	SAD SK1;
	
	CipherPub  EIDA_INV = new CipherPub();	
	CipherPub  EIDB_INV = new CipherPub();
	CipherPub  EKA_INV = new CipherPub();
	CipherPub  EKB_INV = new CipherPub();
	
	CipherPub ALPHA = new CipherPub();
	BigInteger l = BigInteger.ZERO;
	int s =  0;
	
	long t1, t2, t3, t4;
	
	
          
	//_VA和_VB的公钥必须相同
	public MAX(CipherPub2  _VA, CipherPub2 _VB, BigInteger _pub, PaillierT _paillier) {
	    TA  =  _VA;
	    TB  =  _VB;
	    paillier=_paillier;
	    pub = _pub;
	}  
 
	//_VA和_VB的公钥必须相同
	public MAX(CipherPub2  _VA, CipherPub2 _VB,   PaillierT _paillier) {
	    TA  =  _VA;
	    TB  =  _VB;
	    paillier=_paillier;
	    pub = paillier.Hsigma;
	}     
    
    public void StepOne (){    
    	
    	t1 = System.currentTimeMillis(); 
    	
    	TMAX.EI = new CipherPub();
    	TMAX.EID = new CipherPub();
    	TMAX.EK = new CipherPub();    	
    	
	    EUone = paillier.Encryption(EONE, pub);
		EZERO = paillier.Encryption(ZERO, pub);     
      
		IAA.T1 = (TA.EI.T1.modPow(TWO, paillier.nsquare)).multiply(EUone.T1).mod(paillier.nsquare);
		IAA.T2 = (TA.EI.T2.modPow(TWO, paillier.nsquare)).multiply(EUone.T1).mod(paillier.nsquare);
		IAA.PUB = pub;
  
		IBB.T1 = TB.EI.T1.modPow(TWO, paillier.nsquare);
		IBB.T2 = TB.EI.T2.modPow(TWO, paillier.nsquare);
		IBB.PUB = pub;
    
		R1 = new BigInteger(200,  new Random());
		R2 = new BigInteger(200,  new Random());
		R3 = new BigInteger(200,  new Random());
		R4 = new BigInteger(200,  new Random());
		
		ER2 = paillier.Encryption(R2, pub);
		ER3 = paillier.Encryption(R3, pub);
		ER4 = paillier.Encryption(R4, pub);
      
		Random rand = new Random();
        s = rand.nextInt(100000000)%2;
        
        t2 = System.currentTimeMillis();
        timeCP = timeCP + (t2-t1);
        
        
        
        if(s == 1)
        { 
        	t1 = System.currentTimeMillis();
        	
        	C1.T1 = IAA.T1.modPow(R1, paillier.nsquare).multiply(IBB.T1.modPow(paillier.n.subtract(R1), paillier.nsquare));
        	C1.T2 = IAA.T2.modPow(R1, paillier.nsquare).multiply(IBB.T2.modPow(paillier.n.subtract(R1), paillier.nsquare));
        	C1.PUB = pub;
        	
        	C2.T1 = TB.EI.T1.multiply(TA.EI.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare)).multiply(ER2.T1);
        	C2.T2 = TB.EI.T2.multiply(TA.EI.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare)).multiply(ER2.T2);
        	C2.PUB = pub;
        	
        	EIDA_INV.T1 = TA.EID.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        	EIDA_INV.T2 = TA.EID.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        	EIDA_INV.PUB = pub;
        	
            t2 = System.currentTimeMillis();
            timeCP = timeCP + (t2-t1);
        	
        	SK1 = new SAD(TB.EID, EIDA_INV, paillier);
        	t1 = System.currentTimeMillis();
        	SK1.StepOne();
        	t2 = System.currentTimeMillis();
        	SK1.StepTwo();
        	t3 = System.currentTimeMillis();
        	SK1.StepThree();
        	t4 = System.currentTimeMillis();
        	C3 = SK1.FIN;
        	
        	CCC = SK1.CCC;
        	timeCP = timeCP + (t2-t1) + (t4-t3);
        	timeCSP = timeCSP + (t3-t2);
        	
        	t1 = System.currentTimeMillis();
        	C3.T1 = C3.T1.multiply(ER3.T1);
        	C3.T2 = C3.T2.multiply(ER3.T2);
        	C3.PUB = pub;
        	
        	EKA_INV.T1 = TA.EK.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        	EKA_INV.T2 = TA.EK.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        	EKA_INV.PUB = pub;
        	t2 = System.currentTimeMillis();
        	timeCP = timeCP + (t2-t1);
        	
        	SK1 = new SAD(TB.EK, EKA_INV, paillier);
        	t1 = System.currentTimeMillis();
        	SK1.StepOne();
        	t2 = System.currentTimeMillis();
        	SK1.StepTwo();
        	t3 = System.currentTimeMillis();
        	SK1.StepThree();
        	t4 = System.currentTimeMillis();
        	C4 = SK1.FIN;
        	
        	CCC = SK1.CCC;
        	timeCP = timeCP + (t2-t1) + (t4-t3);
        	timeCSP = timeCSP + (t3-t2);
        	
        	t1 = System.currentTimeMillis();
        	C4.T1 = C4.T1.multiply(ER4.T1);
        	C4.T2 = C4.T2.multiply(ER4.T2);
        	C4.PUB = pub;    
        	t2 = System.currentTimeMillis();
        	timeCP = timeCP + (t2-t1);
        }
        else
        {
        	t1 = System.currentTimeMillis();
        	
           	C1.T1 = IBB.T1.modPow(R1, paillier.nsquare).multiply(IAA.T1.modPow(paillier.n.subtract(R1), paillier.nsquare));
        	C1.T2 = IBB.T2.modPow(R1, paillier.nsquare).multiply(IAA.T2.modPow(paillier.n.subtract(R1), paillier.nsquare));
        	C1.PUB = pub;
        	
        	C2.T1 = TA.EI.T1.multiply(TB.EI.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare)).multiply(ER2.T1);
        	C2.T2 = TA.EI.T2.multiply(TB.EI.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare)).multiply(ER2.T2);
        	C2.PUB = pub;
        	
        	EIDB_INV.T1 = TB.EID.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        	EIDB_INV.T2 = TB.EID.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        	EIDB_INV.PUB = pub;
        	
        	t2 = System.currentTimeMillis();
        	timeCP = timeCP + (t2-t1);
        	
        	SK1 = new SAD(TA.EID, EIDB_INV, paillier);
        	t1 = System.currentTimeMillis();
        	SK1.StepOne();
        	t2 = System.currentTimeMillis();
        	SK1.StepTwo();
        	t3 = System.currentTimeMillis();
        	SK1.StepThree();
        	t4 = System.currentTimeMillis();
        	C3 = SK1.FIN;
        	
        	CCC = SK1.CCC;
        	timeCP = timeCP + (t2-t1) + (t4-t3);
        	timeCSP = timeCSP + (t3-t2);
        	
        	t1 = System.currentTimeMillis();
        	
        	C3.T1 = C3.T1.multiply(ER3.T1);
        	C3.T2 = C3.T2.multiply(ER3.T2);
        	C3.PUB = pub;
        	
        	EKB_INV.T1 = TB.EK.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        	EKB_INV.T2 = TB.EK.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
        	EKB_INV.PUB = pub;
        	
        	t2 = System.currentTimeMillis();
        	timeCP = timeCP + (t2-t1);
        	
        	SK1 = new SAD(TA.EK, EKB_INV, paillier);
        	t1 = System.currentTimeMillis();
        	SK1.StepOne();
        	t2 = System.currentTimeMillis();
        	SK1.StepTwo();
        	t3 = System.currentTimeMillis();
        	SK1.StepThree();
        	t4 = System.currentTimeMillis();
        	C4 = SK1.FIN;
        	
        	CCC = SK1.CCC;
        	timeCP = timeCP + (t2-t1) + (t4-t3);
        	timeCSP = timeCSP + (t3-t2);
        	
        	t1 = System.currentTimeMillis();
        	
        	C4.T1 = C4.T1.multiply(ER4.T1);
        	C4.T2 = C4.T2.multiply(ER4.T2);
        	C4.PUB = pub;          	
        	
        	t2 = System.currentTimeMillis();
        	timeCP = timeCP + (t2-t1);
        }
      
        t1 = System.currentTimeMillis();
        
        DO_C1 = paillier.AddPDec1(C1);
        
        t2 = System.currentTimeMillis();
    	timeCP = timeCP + (t2-t1);
        CCC = CCC + DO_C1.T1.bitLength()+DO_C1.T2.bitLength()+DO_C1.T3.bitLength();
        
        timeTotal = timeCP + timeCSP;
    }
   
    public void StepTwo (){
    
    	t1 = System.currentTimeMillis();
    	
    	DT_C1 = paillier.AddPDec2(DO_C1);        

		l = new BigInteger(paillier.n.bitLength()/2,  new Random());;
        
        if (DT_C1.compareTo(l) == -1)
        {
        	ALPHA = paillier.Encryption(ZERO, pub);
        	C5 = paillier.Encryption(ZERO, pub);
        	C6 = paillier.Encryption(ZERO, pub);
        	C7 = paillier.Encryption(ZERO, pub);
        }
        else if (DT_C1.compareTo(l) == 1)
        {
        	ALPHA = paillier.Encryption(EONE, pub);
        	C5 = paillier.Refreash(C2);
        	C6 = paillier.Refreash(C3);
        	C7 = paillier.Refreash(C4);
        }
        else
        {
        	ALPHA = paillier.Encryption(paillier.n.subtract(EONE), pub);
        }
        
        t2 = System.currentTimeMillis();
    	timeCSP = timeCSP + (t2-t1);
        
        CCC = CCC + ALPHA.T1.bitLength()+ALPHA.T2.bitLength();
        CCC = CCC + C5.T1.bitLength()+C5.T2.bitLength();
        CCC = CCC + C6.T1.bitLength()+C6.T2.bitLength();
        CCC = CCC + C7.T1.bitLength()+C7.T2.bitLength();
        
        timeTotal = timeCP + timeCSP;
    }
   
	public void StepThree (){
			
       if(s == 1)
       {
    	   	t1 = System.currentTimeMillis();
    	   
    	   	TMAX.EI.T1 = TA.EI.T1.multiply(C5.T1).multiply(ALPHA.T1.modPow(paillier.n.subtract(R2), paillier.nsquare));
    	   	TMAX.EI.T2 = TA.EI.T2.multiply(C5.T2).multiply(ALPHA.T2.modPow(paillier.n.subtract(R2), paillier.nsquare));
    	   	TMAX.EI.PUB = pub;
    	   
    	   	t2 = System.currentTimeMillis();
    	   	timeCP = timeCP + (t2-t1);
    	   
    	   	SK1 = new SAD(TA.EID, C6, paillier);
	       	t1 = System.currentTimeMillis();
	       	SK1.StepOne();
	       	t2 = System.currentTimeMillis();
	       	SK1.StepTwo();
	       	t3 = System.currentTimeMillis();
	       	SK1.StepThree();
	       	t4 = System.currentTimeMillis();
	       	TMAX.EID = SK1.FIN;
        	
        	CCC = SK1.CCC;
        	timeCP = timeCP + (t2-t1) + (t4-t3);
        	timeCSP = timeCSP + (t3-t2);
       	
        	t1 = System.currentTimeMillis();
	       	TMAX.EID.T1 = TMAX.EID.T1.multiply(ALPHA.T1.modPow(paillier.n.subtract(R3), paillier.nsquare));
	       	TMAX.EID.T2 = TMAX.EID.T2.multiply(ALPHA.T2.modPow(paillier.n.subtract(R3), paillier.nsquare));
	       	TMAX.EID.PUB = pub;
    	   	t2 = System.currentTimeMillis();
    	   	timeCP = timeCP + (t2-t1);
       	   
	       	SK1 = new SAD(TA.EK, C7, paillier);
	       	t1 = System.currentTimeMillis();
	       	SK1.StepOne();
	       	t2 = System.currentTimeMillis();
	       	SK1.StepTwo();
	       	t3 = System.currentTimeMillis();
	       	SK1.StepThree();
	       	t4 = System.currentTimeMillis();
	       	TMAX.EK = SK1.FIN;
        	
        	CCC = SK1.CCC;
        	timeCP = timeCP + (t2-t1) + (t4-t3);
        	timeCSP = timeCSP + (t3-t2);
       	
        	t1 = System.currentTimeMillis();
        	
        	timeCP = timeCP + (t2-t1);
	       	TMAX.EK.T1 = TMAX.EK.T1.multiply(ALPHA.T1.modPow(paillier.n.subtract(R4), paillier.nsquare));
	       	TMAX.EK.T2 = TMAX.EK.T2.multiply(ALPHA.T2.modPow(paillier.n.subtract(R4), paillier.nsquare));
	       	TMAX.EK.PUB = pub;
        	
        	t2 = System.currentTimeMillis();
        	timeCP = timeCP + (t2-t1);
       }
  
       if(s == 0)
       {
    	   	t1 = System.currentTimeMillis();
       	
    	   	TMAX.EI.T1 = TB.EI.T1.multiply(C5.T1).multiply(ALPHA.T1.modPow(paillier.n.subtract(R2), paillier.nsquare));
    	   	TMAX.EI.T2 = TB.EI.T2.multiply(C5.T2).multiply(ALPHA.T2.modPow(paillier.n.subtract(R2), paillier.nsquare));
    	   	TMAX.EI.PUB = pub;
       	
	       	t2 = System.currentTimeMillis();
	   	   	timeCP = timeCP + (t2-t1);
    	   
    	   	SK1 = new SAD(TB.EID, C6, paillier);
	       	t1 = System.currentTimeMillis();
	       	SK1.StepOne();
	       	t2 = System.currentTimeMillis();
	       	SK1.StepTwo();
	       	t3 = System.currentTimeMillis();
	       	SK1.StepThree();
	       	t4 = System.currentTimeMillis();
	       	TMAX.EID = SK1.FIN;
        	
        	CCC = SK1.CCC;
        	timeCP = timeCP + (t2-t1) + (t4-t3);
        	timeCSP = timeCSP + (t3-t2);
       	
        	t1 = System.currentTimeMillis();
           	
        	TMAX.EID.T1 = TMAX.EID.T1.multiply(ALPHA.T1.modPow(paillier.n.subtract(R3), paillier.nsquare));
	       	TMAX.EID.T2 = TMAX.EID.T2.multiply(ALPHA.T2.modPow(paillier.n.subtract(R3), paillier.nsquare));
	       	TMAX.EID.PUB = pub;
           	
	       	t2 = System.currentTimeMillis();
	   	   	timeCP = timeCP + (t2-t1);

	   	   	
	       	SK1 = new SAD(TB.EK, C7, paillier);
	       	t1 = System.currentTimeMillis();
	       	SK1.StepOne();
	       	t2 = System.currentTimeMillis();
	       	SK1.StepTwo();
	       	t3 = System.currentTimeMillis();
	       	SK1.StepThree();
	       	t4 = System.currentTimeMillis();
	       	TMAX.EK = SK1.FIN;
        	
        	CCC = SK1.CCC;
        	timeCP = timeCP + (t2-t1) + (t4-t3);
        	timeCSP = timeCSP + (t3-t2);
       	
        	t1 = System.currentTimeMillis();
           	
        	TMAX.EK.T1 = TMAX.EK.T1.multiply(ALPHA.T1.modPow(paillier.n.subtract(R4), paillier.nsquare));
	       	TMAX.EK.T2 = TMAX.EK.T2.multiply(ALPHA.T2.modPow(paillier.n.subtract(R4), paillier.nsquare));
	       	TMAX.EK.PUB = pub;
           	
	       	t2 = System.currentTimeMillis();
	   	   	timeCP = timeCP + (t2-t1);
	   	   	
	   	   	timeTotal = timeCP + timeCSP;
	       	
       }          
	}    
}
