/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.helib;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.Ciphertext1;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.subprotocols.SAD;

import java.math.BigInteger;
import java.util.Random;
/**
/**
 *
 * @author Yang Yang
 */
//Greater than
public class SGT {
	public CipherPub a = new CipherPub();
	public CipherPub  b = new CipherPub();
	public CipherPub  a11 = new CipherPub();
	public CipherPub  b11 = new CipherPub();
	public CipherPub  a12 = new CipherPub();
	public CipherPub  b12 = new CipherPub();
	public CipherPub  a13 = new CipherPub();
	public CipherPub  b13 = new CipherPub();
	public CipherPub  l1 = new CipherPub();
	public CipherPub  l2 = new CipherPub();
	public CipherPub U = new CipherPub();
	public CipherPub EZERO = new CipherPub();
	public CipherPub  EEone = new CipherPub();
	public CipherPub  EUone = new CipherPub();
	public CipherPub  FIN = new CipherPub();
	public Ciphertext1 m11 = new Ciphertext1();
   
	public BigInteger pub = BigInteger.ZERO;
	public PaillierT paillier = null;
	public BigInteger RR1= BigInteger.ZERO;
	public BigInteger EONE= BigInteger.ONE;
	public BigInteger ZERO= BigInteger.ZERO;
	public BigInteger TWO = BigInteger.ZERO;
	public BigInteger l = BigInteger.ZERO;
	public BigInteger m1 = BigInteger.ZERO;
	public int s =  0;
	public long CCC =  0;
          
	public SGT(CipherPub  _VA, CipherPub _VB, BigInteger _pub, PaillierT _paillier) {
		b = _VA;		//deliberately change the position of a and b 
		a = _VB;
	    paillier=_paillier;
	    pub = _pub;
	}  
 
	public SGT(CipherPub  _VA, CipherPub _VB,   PaillierT _paillier) {
		b = _VA;		//deliberately change the position of a and b 
		a = _VB;
	    paillier=_paillier;
	    pub = paillier.Hsigma;
	}     
    
    public void StepOne (){
    
		TWO = new   BigInteger("2");
		EEone = paillier.Encryption(EONE, a.PUB);
	    EUone = paillier.Encryption(EONE, pub);
		EZERO = paillier.Encryption(ZERO, pub);     
      
		a12.T1 = (a.T1.modPow(TWO, paillier.nsquare)).multiply(EEone.T1).mod(paillier.nsquare);
		a12.T2 = (a.T2.modPow(TWO, paillier.nsquare)).multiply(EEone.T2).mod(paillier.nsquare);;
		a12.PUB = a.PUB;
  
		b12.T1 = b.T1.modPow(TWO, paillier.nsquare);
		b12.T2 = b.T2.modPow(TWO, paillier.nsquare);
		b12.PUB = b.PUB;
    
		RR1 = new BigInteger(200,  new Random());
      
		Random rand = new Random();
        s = rand.nextInt(100000000)%2;
        
        if(s == 1)
        { 
            b13.T1 = b12.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
            b13.T2 = b12.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
            b13.PUB = b12.PUB;
            SAD SK11  = new SAD(a12,b13, pub, paillier);
            SK11.StepOne();
            SK11.StepTwo();
            SK11.StepThree();
       
            l2 = SK11.FIN;
            
            CCC = CCC+ SK11.CCC;  
            
            
            l1.T1 = (l2.T1).modPow(RR1, paillier.nsquare);
            l1.T2 = (l2.T2).modPow(RR1, paillier.nsquare);
            l1.PUB = pub;
        }
        else
        {
            a13.T1 = a12.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare);
            a13.T2 = a12.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare);
            a13.PUB = a12.PUB;
            
             SAD SK11  = new SAD(a13,b12, pub, paillier);
             SK11.StepOne();
             SK11.StepTwo();
             SK11.StepThree();
       
             l2 = SK11.FIN;
            
             CCC = CCC+ SK11.CCC; 
            
            
            
             l1.T1 = (l2.T1).modPow(RR1, paillier.nsquare);
             l1.T2 = (l2.T2).modPow(RR1, paillier.nsquare);
             l1.PUB = pub;
        }
      
        
        m11=paillier.AddPDec1(l1);
        CCC = CCC + m11.T1.bitLength()+m11.T2.bitLength()+m11.T3.bitLength();
    }
   
    public void StepTwo (){
    
        m1 = paillier.AddPDec2(m11);        

		l = new BigInteger(paillier.n.bitLength()/2,  new Random());;
        
        if (m1.compareTo(l) == 1)
        {
        	U = paillier.Encryption(EONE, pub);
        }
        else if (m1.compareTo(l) == -1)
        {
        	U = paillier.Encryption(ZERO, pub);
        }
        else
        {
        	U = paillier.Encryption(paillier.n.subtract(EONE), pub);
        }
        
        CCC = CCC + U.T1.bitLength()+U.T2.bitLength();
    }
   
	public void StepThree (){
        
       if(s == 1)
       {
    	   FIN = paillier.Refreash(U); 
       }
  
       if(s == 0)
       {
    	   FIN.T1 = EUone.T1.multiply((U.T1.modPow(paillier.n.subtract(EONE), paillier.nsquare))).mod(paillier.nsquare); 
    	   FIN.T2 = EUone.T2.multiply((U.T2.modPow(paillier.n.subtract(EONE), paillier.nsquare))).mod(paillier.nsquare); 
    	   FIN.PUB = pub;
       }   
       
	}
    
}
