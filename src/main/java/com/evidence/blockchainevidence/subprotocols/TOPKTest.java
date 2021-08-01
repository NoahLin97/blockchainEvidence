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
 *
 * @author YangYang
 */
public class TOPKTest { 

	public static void main(String[] args) {
		PaillierT paillier = new PaillierT(512, 64);
//		PaillierT paillier = new PaillierT(768, 64);
//		PaillierT paillier = new PaillierT(1024, 64);
//		PaillierT paillier = new PaillierT(1280, 64);
//		PaillierT paillier = new PaillierT(1536, 64);
//		PaillierT paillier = new PaillierT(1792, 64);
//		PaillierT paillier = new PaillierT(2048, 64);
		
		//time and CCC
		long timeCP, timeCSP, timeTotal;
		long CCC = 0;
		timeCP = 0;
		timeCSP = 0;
		timeTotal = 0;
		
		//input
		CipherPub2[] TT;
		
		//output
		CipherPub2 [] TMAX;
		
		//temporary variable
		TOPK SK1;
		BigInteger [] I;
		BigInteger [] ID;
		BigInteger [] K;
		
		int i;

		int N = 10;		//N=TT.length
		int KK = 3;		//Top-K
		
		//≥ı ºªØ
		TT = new CipherPub2[N];
		I = new BigInteger[N];
		ID= new BigInteger[N];
		K = new BigInteger[N];
		
		
		for(i=0; i<TT.length; i++){			
			TT[i] = new CipherPub2();
			TT[i].EI = new CipherPub();
			TT[i].EID = new CipherPub();
			TT[i].EK = new CipherPub();
			
			I[i] = BigInteger.ZERO;
			ID[i] = BigInteger.ZERO;
			K[i] = BigInteger.ZERO;
		}
		

		TMAX = new CipherPub2[KK];
		for(i=0; i<KK; i++){			
			TMAX[i] = new CipherPub2();
			TMAX[i].EI = new CipherPub();
			TMAX[i].EID = new CipherPub();
			TMAX[i].EK = new CipherPub();
		}
		
		
		//origin data
		I[0] = BigInteger.valueOf(91);
		ID[0] = BigInteger.valueOf(2);
		K[0] = BigInteger.valueOf(3);
		
		I[1] = BigInteger.valueOf(11);
		ID[1] = BigInteger.valueOf(12);
		K[1] = BigInteger.valueOf(13);
		
		I[2] = BigInteger.valueOf(101);
		ID[2] = BigInteger.valueOf(22);
		K[2] = BigInteger.valueOf(23);
		
		I[3] = BigInteger.valueOf(31);
		ID[3] = BigInteger.valueOf(32);
		K[3] = BigInteger.valueOf(33);
		
		I[4] = BigInteger.valueOf(211);
		ID[4] = BigInteger.valueOf(42);
		K[4] = BigInteger.valueOf(43);
		
		I[5] = BigInteger.valueOf(51);
		ID[5] = BigInteger.valueOf(52);
		K[5] = BigInteger.valueOf(53);
		
		I[6] = BigInteger.valueOf(61);
		ID[6] = BigInteger.valueOf(62);
		K[6] = BigInteger.valueOf(63);
		
		I[7] = BigInteger.valueOf(71);
		ID[7] = BigInteger.valueOf(72);
		K[7] = BigInteger.valueOf(73);
		
		I[8] = BigInteger.valueOf(81);
		ID[8] = BigInteger.valueOf(82);
		K[8] = BigInteger.valueOf(83);
		
		I[9] = BigInteger.valueOf(91);
		ID[9] = BigInteger.valueOf(92);
		K[9] = BigInteger.valueOf(93);
		
    	
		//encrypt 
		for(i=0; i<TT.length; i++){
			if(i%2 == 0){
				TT[i].EI = paillier.Encryption(I[i], paillier.Hsigma);
				TT[i].EID = paillier.Encryption(ID[i], paillier.H[1]);
				TT[i].EK = paillier.Encryption(K[i], paillier.H[1]);
			}else if(i%2 == 1){
				TT[i].EI = paillier.Encryption(I[i], paillier.Hsigma);
				TT[i].EID = paillier.Encryption(ID[i], paillier.H[2]);
				TT[i].EK = paillier.Encryption(K[i], paillier.H[2]);
			}
		}
		
		//MAXN
		SK1 = new TOPK(TT, KK, paillier);
		SK1.StepOne();
		TMAX = SK1.TMAX;	
		CCC = CCC + SK1.CCC;
		timeCP = timeCP + SK1.timeCP;
		timeCSP = timeCSP + SK1.timeCSP;

		
		//print result
		for(i=0; i<KK; i++){
			System.out.println("TMAX["+i+"].EI="+paillier.SDecryption(TMAX[i].EI));
			System.out.println("TMAX["+i+"].EID="+paillier.SDecryption(TMAX[i].EID));
			System.out.println("TMAX["+i+"].EK="+paillier.SDecryption(TMAX[i].EK)+"\n");
		}
		
		
		
		timeTotal = timeCP + timeCSP;
		System.out.println((float)timeCP/1000);			//s
		System.out.println((float)timeCSP/1000);		//s
		System.out.println((float)timeTotal/1000);		//s
		System.out.println((float)CCC/8000);			//KB
	}
}