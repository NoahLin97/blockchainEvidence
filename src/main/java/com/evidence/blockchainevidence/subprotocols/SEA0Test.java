/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.subprotocols;

import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;

/**
 *
 * @author YangYang
 */
public class SEA0Test { 

	public static void main(String[] args) {
//		PaillierT paillier = new PaillierT(512, 64);
//		PaillierT paillier = new PaillierT(768, 64);
		PaillierT paillier = new PaillierT(1024, 64);
//		PaillierT paillier = new PaillierT(1280, 64);
//		PaillierT paillier = new PaillierT(1536, 64);
//		PaillierT paillier = new PaillierT(1792, 64);
//		PaillierT paillier = new PaillierT(2048, 64);

		int N = 1; // loop times and then get the average value
		int i,j;

		CipherPub EX;
		CipherPub EB1;
		CipherPub EB2;
		long timeCP, timeCSP, timeTotal;
		long CCC = 0;

		timeCP = 0;
		timeCSP = 0;
		timeTotal = 0;
		
		K2C8 SK0;
		SEA0 SK1;
		KMP0 SK2;
		

		for (i = 1; i <= N; i++) {
			
			String X = "privacy";
			SK0 = new K2C8(X, paillier.H[1], paillier);
			SK0.StepOne();
			EX = SK0.FIN;
			
			String S = "p*vacy";
			SK1 = new SEA0(S, paillier);
			SK1.StepOne();
			

			SK2 = new KMP0(EX, SK1.Y1, SK1.Y2, SK1.num, SK1.numchar, paillier);
			SK2.StepOne();
			
			
			System.out.print("U="+paillier.SDecryption(SK2.FIN)+"\n\n");
			
			timeCP = timeCP + SK2.timeCP;
			timeCSP = timeCSP + SK2.timeCSP;
			timeTotal = timeTotal + SK2.timeTotal;
			CCC = CCC + SK2.CCC;
		}

		timeCP = timeCP/N; 			//求时间平均
		timeCSP = timeCSP/N; 		//求时间平均
		timeTotal = timeTotal/N; 	//求时间平均
		CCC = CCC/N; 			//求通信量平均
		
		System.out.println(timeCP);
		System.out.println(timeCSP);
		System.out.println(timeTotal);
		System.out.println(CCC);

	}
}