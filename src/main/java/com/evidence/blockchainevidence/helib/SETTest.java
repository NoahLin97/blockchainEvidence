/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.helib;

import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.subprotocols.K2C16;

/**
 *
 * @author YangYang
 */
public class SETTest {

	public static void main(String[] args) {
//		PaillierT paillier = new PaillierT(512, 64);
//		PaillierT paillier = new PaillierT(768, 64);
		PaillierT paillier = new PaillierT(1024, 64);
//		PaillierT paillier = new PaillierT(1280, 64);
//		PaillierT paillier = new PaillierT(1536, 64);
//		PaillierT paillier = new PaillierT(1792, 64);
//		PaillierT paillier = new PaillierT(2048, 64);

		// time and CCC
		long timeCP, timeCSP, timeTotal;
		long CCC = 0;
		timeCP = 0;
		timeCSP = 0;
		timeTotal = 0;

		// input
		String A;
		String B;

		// output
		CipherPub FIN;

		// temporary variable
		CipherPub EA;
		CipherPub EB;

		K2C16 SK1;
		SET SK2;

		int i, j;
		long t1, t2, t3, t4;

//		A = "キーワード";
//		B = "关KW5";
		
		A = "キーワード";
		B = "キーワード";
		
//		A = "키워드";
//		B = "키워드";



		SK1 = new K2C16(A, paillier.H[1], paillier);
		SK1.StepOne();
		EA = SK1.FIN;

		SK1 = new K2C16(B, paillier.H[2], paillier);
		SK1.StepOne();
		EB = SK1.FIN;



		//SET,生成时输入KW，QW；然后依次StepOne和StepTwo，最后从FIN拿结果；FIN可以拿去pallier里解密

		SK2 = new SET(EA, EB, paillier);
		t1 = System.currentTimeMillis();
		SK2.StepOne();
		t2 = System.currentTimeMillis();
		SK2.StepTwo();
		t3 = System.currentTimeMillis();
		SK2.StepThree();
		t4 = System.currentTimeMillis();
		FIN = SK2.FIN;

		CCC = SK2.CCC;
		timeCP = timeCP + (t2 - t1) + (t4 - t3);
		timeCSP = timeCSP + (t3 - t2);
		timeTotal = timeCP + timeCSP;
		
		System.out.println("FIN="+paillier.SDecryption(FIN)+"\n");

		System.out.println(timeCP);
		System.out.println(timeCSP);
		System.out.println(timeTotal);
		System.out.println(CCC);
	}
}