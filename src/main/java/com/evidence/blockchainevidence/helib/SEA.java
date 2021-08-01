/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evidence.blockchainevidence.helib;

import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.subprotocols.K2C8;
import com.evidence.blockchainevidence.subprotocols.WTB;
import com.evidence.blockchainevidence.subprotocols.WTF;
import com.evidence.blockchainevidence.subprotocols.WTM;

import java.math.BigInteger;

/**
 * /**
 *
 * @author Yang Yang
 */
public class SEA {
	//input
	String S;
	BigInteger pub = BigInteger.ZERO;
	PaillierT paillier = null;

	
	//output
	public CipherPub Y1 = new CipherPub();
	public CipherPub Y2 = new CipherPub();
	public CipherPub Y3 = new CipherPub();
	public int num1;
	public int num2;
	public int numchar1;
	public int numchar2;
	
	public int flag;			//flag = 0,1,2 表示*个数
	
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
	
	int count = 0;			//字符串中含有*的个数
	
	
	int NUM1 = 3;		//系统默认的*可以取代的字符个数
	int NUM2 = 3;		//系统默认的*可以取代的字符个数

	public SEA(String _S, int _num1, int _num2, BigInteger _pub, PaillierT _paillier) {
		S = _S;
		num1 = _num1;
		num2 = _num2;
		paillier = _paillier;
		pub = _pub;
	}

	public SEA(String _S, int _num1, int _num2, PaillierT _paillier) {
		S = _S;
		num1 = _num1;
		num2 = _num2;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}
	
	public SEA(String _S, BigInteger _pub, PaillierT _paillier) {
		S = _S;
		num1 = NUM1;
		num2 = NUM2;
		paillier = _paillier;
		pub = _pub;
	}

	public SEA(String _S, PaillierT _paillier) {
		S = _S;
		num1 = NUM1;
		num2 = NUM2;
		paillier = _paillier;
		pub = paillier.Hsigma;
	}

	public void StepOne() {
		
		EZERO = paillier.Encryption(BigInteger.ZERO, pub);
		
		int index[] = new int[S.length()];		//存储*在字符串中的下标
		
		for(int i=0;i<S.length();i++){
			if (S.charAt(i) =='*'){
				count++;
				index[count-1] = i;
			}
		}
		
		//字符串中无*号
		if (count==0){
			
			flag =0;
			
			System.out.print("SEA: No *. \n");
			
			num1 = -1;
			num2 = -1;
			numchar1 = -1;
			numchar2 = -1;
			
			SK0 = new K2C8(S, pub, paillier);
			SK0.StepOne();
			Y1 = SK0.FIN;
			
			Y2 = EZERO;
			Y3 = EZERO;

		}	
		//字符串中有1个*号
		else if (count==1){
			
			flag =1;
			
			System.out.print("SEA: One *. \n");
			
			//字符串中*号在最前面
			if(S.indexOf("*")==0){
				
				System.out.print("SEA: Front. \n");
				
				String[] SubS = S.split("[*]");
				
				num2 = -1;
				numchar1 = -1;
				numchar2 = -1;
				
				SK0 = new K2C8(SubS[1], pub, paillier);
				SK0.StepOne();
				Y1 = SK0.FIN;
				
				Y2 = EZERO;
				Y3 = EZERO;
			}
			//字符串中*号在最后面
			else if(S.indexOf("*")==S.length()-1){
				
				System.out.print("SEA: Back. \n");
				
				String[] SubS = S.split("[*]");
				
				num1 = -1;
				num2 = -1;
				numchar1 = SubS[0].length();
				numchar2 = -1;
				
				SK0 = new K2C8(SubS[0], pub, paillier);
				SK0.StepOne();
				Y1 = SK0.FIN;
				
				Y2 = EZERO;
				Y3 = EZERO;
			}
			//字符串中*号在中间
			else{
				
				System.out.print("SEA: Middle. \n");
				
				String[] SubS = S.split("[*]");
				
				num2 = -1;
				numchar1 = SubS[0].length();
				numchar2 = -1;
				
				SK0 = new K2C8(SubS[0], pub, paillier);
				SK0.StepOne();
				Y1 = SK0.FIN;
				
				SK0 = new K2C8(SubS[1], pub, paillier);
				SK0.StepOne();
				Y2 = SK0.FIN;
				
				Y3 = EZERO;
				
			}
		}
		//字符串中有2个*号
		else if (count==2){
			
			flag =2;
			
			System.out.print("SEA: Two *. \n");
			
			if(index[0]==0 & index[1]!=S.length()-1){
				
				System.out.print("Front + Middle. \n");
				
				String[] SubS = S.split("[*]");
				
				numchar1 = SubS[1].length();
				numchar2 = -1;
		
				
				SK0 = new K2C8(SubS[1], pub, paillier);
				SK0.StepOne();
				Y1 = SK0.FIN;
				
				SK0 = new K2C8(SubS[2], pub, paillier);
				SK0.StepOne();
				Y2 = SK0.FIN;
				
				Y3 = EZERO;
			}
			else if(index[0]==0 & index[1]==S.length()-1){
				
				System.out.print("SEA: Front + Back. \n");
				
				String[] SubS = S.split("[*]");

				num2 = -1;
				numchar1 = SubS[1].length();
				numchar2 = -1;
				
				SK0 = new K2C8(SubS[1], pub, paillier);
				SK0.StepOne();
				Y1 = SK0.FIN;
				
				Y2 = EZERO;
				Y3 = EZERO;
			}
			else if(index[0]!=0 & index[1]!=S.length()-1){
				
				System.out.print("SEA: Middle + Middle. \n");
				
				String[] SubS = S.split("[*]");
				
				numchar1 = SubS[0].length();
				numchar2 = SubS[1].length();
				
				SK0 = new K2C8(SubS[0], pub, paillier);
				SK0.StepOne();
				Y1 = SK0.FIN;
				
				SK0 = new K2C8(SubS[1], pub, paillier);
				SK0.StepOne();
				Y2 = SK0.FIN;
				
				
				SK0 = new K2C8(SubS[2], pub, paillier);
				SK0.StepOne();
				Y3 = SK0.FIN;
			}
			else if(index[0]!=0 & index[1]==S.length()-1){
				
				System.out.print("SEA: Middle + Back. \n");
				
				String[] SubS = S.split("[*]");
				
				num2 = -1;
				numchar1 = SubS[0].length();
				numchar2 = SubS[1].length();
				
				SK0 = new K2C8(SubS[0], pub, paillier);
				SK0.StepOne();
				Y1 = SK0.FIN;
				
				SK0 = new K2C8(SubS[1], pub, paillier);
				SK0.StepOne();
				Y2 = SK0.FIN;
				
				Y3 = EZERO;
			}
		}
	}
}
