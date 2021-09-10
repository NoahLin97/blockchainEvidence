
package com.evidence.blockchainevidence.controller;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.entity.*;
import com.evidence.blockchainevidence.helib.SLT;
import com.evidence.blockchainevidence.mapper.AutmanMapper;
import com.evidence.blockchainevidence.mapper.NotarizationTypeMapper;
import com.evidence.blockchainevidence.mapper.NotaryStatisticsMapper;
import com.evidence.blockchainevidence.mapper.OrganizationStatisticsMapper;
import com.evidence.blockchainevidence.subprotocols.K2C16;
import com.evidence.blockchainevidence.subprotocols.K2C8;
import com.evidence.blockchainevidence.subprotocols.KET;
import com.evidence.blockchainevidence.subprotocols.SAD;
import com.evidence.blockchainevidence.utils.ParseRequest;
import com.evidence.blockchainevidence.utils.Sha256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.evidence.blockchainevidence.utils.GlobalParams.*;

@RestController
public class AutmanController {

    @Autowired(required = false)
    AutmanMapper autmanMapper;

    @Autowired
    NotarizationTypeMapper notarizationTypeMapper;

    public static Map<String,Object> eviSelect (JSONObject params, AutmanMapper autmanMapper){
        Map<String,Object> result=new HashMap<>();

        try{


            //直接筛选的
            String notarizationStatus="none";
            if(params.containsKey("notarizationStatus")){
                notarizationStatus=params.get("notarizationStatus").toString();
            }

            String notarizationType="none";
            if(params.containsKey("notarizationType")){
                notarizationType=params.get("notarizationType").toString();
            }

            String paymentStatus="none";
            if(params.containsKey("paymentStatus")){
                paymentStatus=params.get("paymentStatus").toString();
            }

            String evidenceType="none";
            if(params.containsKey("evidenceType")){
                evidenceType=params.get("evidenceType").toString();
            }

            String organizationId="none";
            if(params.containsKey("organizationId")){
                organizationId=params.get("organizationId").toString();
            }

            //要通配的明文字符串



            String usernameWildcard="none";
            if(params.containsKey("usernameWildcard")){
                usernameWildcard=params.get("usernameWildcard").toString();
            }

            //要比大小的date

            String notarizationStartTimeStart="none";
            if(params.containsKey("notarizationStartTimeStart")){
                notarizationStartTimeStart=params.get("notarizationStartTimeStart").toString();
            }

            String notarizationStartTimeEnd="none";
            if(params.containsKey("notarizationStartTimeEnd")){
                notarizationStartTimeEnd=params.get("notarizationStartTimeEnd").toString();
            }

            String notarizationEndTimeStart="none";
            if(params.containsKey("notarizationEndTimeStart")){
                notarizationEndTimeStart=params.get("notarizationEndTimeStart").toString();
            }

            String notarizationEndTimeEnd="none";
            if(params.containsKey("notarizationEndTimeEnd")){
                notarizationEndTimeEnd=params.get("notarizationEndTimeEnd").toString();
            }

            //要比大小的明文


            //要比大小的密文

            Integer notarizationMoneyUpper=-1;
            if(params.containsKey("notarizationMoneyUpper")){
                notarizationMoneyUpper=Integer.parseInt(params.get("notarizationMoneyUpper").toString());
            }

            Integer notarizationMoneyFloor=-1;
            if(params.containsKey("notarizationMoneyFloor")){
                notarizationMoneyFloor=Integer.parseInt(params.get("notarizationMoneyFloor").toString());
            }

            Integer fileSizeUpper=-1;
            if(params.containsKey("fileSizeUpper")){
                fileSizeUpper=Integer.parseInt(params.get("fileSizeUpper").toString());
            }

            Integer fileSizeFloor=-1;
            if(params.containsKey("fileSizeFloor")){
                fileSizeFloor=Integer.parseInt(params.get("fileSizeFloor").toString());
            }

            //要通配的密文
            String evidenceName="none";
            if(params.containsKey("evidenceName")){
                evidenceName=params.get("evidenceName").toString();
            }


            //解密标识符
            Integer decryptFlag=0;
            if(params.containsKey("decryptFlag")){
                decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            }



            //查询数据库
            List<EvidenceEntity> data = autmanMapper.findEvidence("none","none","none",
                    notarizationStatus, notarizationType,
                    paymentStatus, evidenceType, organizationId,
                    evidenceName, usernameWildcard, notarizationStartTimeStart,
                    notarizationStartTimeEnd, notarizationEndTimeStart,
                    notarizationEndTimeEnd);


            PaillierT paillier = new PaillierT(PaillierT.param);
            //在密文下匹配，剔除不合格的数据
            Iterator<EvidenceEntity> iterator = data.iterator();
            while (iterator.hasNext()) {
                EvidenceEntity s = iterator.next();


                //剔除不符合接口要求的数据，这里需要根据接口定制
                //notarizationStatus为"0"的数据不返回
                if(s.getNotarizationStatus().toString().equals("0")){
                    iterator.remove();
                    continue;
                }


                //密文数值比较阶段

                if(notarizationMoneyFloor!=-1){
                    if(!s.getNotarizationMoney().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(notarizationMoneyFloor),pk);
                        CipherPub ckw= new CipherPub(s.getNotarizationMoney());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
                if(notarizationMoneyUpper!=-1){
                    if(!s.getNotarizationMoney().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(notarizationMoneyUpper),pk);
                        CipherPub ckw= new CipherPub(s.getNotarizationMoney());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }




                if(fileSizeFloor!=-1){
                    if(!s.getFileSize().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(fileSizeFloor),pk);
                        CipherPub ckw= new CipherPub(s.getFileSize());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
                if(fileSizeUpper!=-1){
                    if(!s.getFileSize().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(fileSizeUpper),pk);
                        CipherPub ckw= new CipherPub(s.getFileSize());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
                //密文字符通配阶段
                if(!evidenceName.equals("none")){
                    if(!s.getEvidenceName().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);

                        K2C16 k2c16 = new K2C16(evidenceName, pk, paillier);
                        k2c16.StepOne();
                        CipherPub cqw=k2c16.FIN;
                        CipherPub ckw= new CipherPub(s.getEvidenceName());

//                        System.out.println(paillier.SDecryption(ckw));

                        KET ket = new KET(cqw,ckw,paillier);
                        ket.StepOne();
                        ket.StepTwo();
                        ket.StepThree();
                        CipherPub cans=ket.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }



//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                EvidenceEntity s = iterator.next();

                Integer i;
                i=Integer.parseInt(s.getNotarizationStatus().toString());
                s.setNotarizationStatus(notarizationStatuses[i]);

                i=Integer.parseInt(s.getEvidenceType().toString());
                s.setEvidenceType(evidenceTypes[i]);

                if(s.getTransactionStatus()!=null){
                    i=Integer.parseInt(s.getTransactionStatus().toString());
                    s.setTransactionStatus(transactionStatuses[i]);
                }
                if(s.getNotarizationType()!=null){
                    i=Integer.parseInt(s.getNotarizationType().toString());
                    s.setNotarizationType(notarizationTypes[i]);
                }



                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1){
                    //解密数字

                    if(!s.getFileSize().equals("")){
                        s.setFileSize(paillier.SDecryption(new CipherPub(s.getFileSize())).intValue()+"");
                    }
                    if(!s.getNotarizationMoney().equals("")){
                        s.setNotarizationMoney(paillier.SDecryption(new CipherPub(s.getNotarizationMoney())).intValue()+"");
                    }

                    //解密字符
                    if(!s.getEvidenceName().equals("")){
                        s.setEvidenceName(K2C16.parseString(paillier.SDecryption(new CipherPub(s.getEvidenceName())),paillier));
                    }
                }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    public static Map<String,Object> eviSelect2 (JSONObject params, AutmanMapper autmanMapper){
        Map<String,Object> result=new HashMap<>();

        try{


            //直接筛选的
            String notarizationStatus="none";
            if(params.containsKey("notarizationStatus")){
                notarizationStatus=params.get("notarizationStatus").toString();
            }

            String evidenceType="none";
            if(params.containsKey("evidenceType")){
                evidenceType=params.get("evidenceType").toString();
            }

            String evidenceBlockchainId="none";
            if(params.containsKey("evidenceBlockchainId")){
                evidenceBlockchainId=params.get("evidenceBlockchainId").toString();
            }

            String evidenceId="none";
            if(params.containsKey("evidenceId")){
                evidenceId=params.get("evidenceId").toString();
            }

            String userId="none";
            if(params.containsKey("userId")){
                userId=params.get("userId").toString();
            }

            //要通配的明文字符串

            String evidenceName="none";
            if(params.containsKey("evidenceName")){
                evidenceName=params.get("evidenceName").toString();
            }

            String usernameWildcard="none";
            if(params.containsKey("usernameWildcard")){
                usernameWildcard=params.get("usernameWildcard").toString();
            }

            //要比大小的date

            String evidenceTimeStart="none";
            if(params.containsKey("evidenceTimeStart")){
                evidenceTimeStart=params.get("evidenceTimeStart").toString();
            }

            String evidenceTimeEnd="none";
            if(params.containsKey("evidenceTimeEnd")){
                evidenceTimeEnd=params.get("evidenceTimeEnd").toString();
            }

            String blockchainTimeStart="none";
            if(params.containsKey("blockchainTimeStart")){
                blockchainTimeStart=params.get("blockchainTimeStart").toString();
            }

            String blockchainTimeEnd="none";
            if(params.containsKey("notarizationEndTimeEnd")){
                blockchainTimeEnd=params.get("blockchainTimeEnd").toString();
            }

            //要比大小的明文


            //要比大小的密文

            Integer fileSizeUpper=-1;
            if(params.containsKey("fileSizeUpper")){
                fileSizeUpper=Integer.parseInt(params.get("fileSizeUpper").toString());
            }

            Integer fileSizeFloor=-1;
            if(params.containsKey("fileSizeFloor")){
                fileSizeFloor=Integer.parseInt(params.get("fileSizeFloor").toString());
            }

            //要通配的密文



            //解密标识符
            Integer decryptFlag=0;
            if(params.containsKey("decryptFlag")){
                decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            }



            //查询数据库
            List<EvidenceEntity> data = autmanMapper.findEvidence2( evidenceId,  userId,  usernameWildcard,
                     notarizationStatus,  evidenceBlockchainId,
                     evidenceType,
                    evidenceName,  evidenceTimeStart,
                     evidenceTimeEnd,  blockchainTimeStart,
                     blockchainTimeEnd) ;



            //在密文下匹配，剔除不合格的数据
            Iterator<EvidenceEntity> iterator = data.iterator();
            PaillierT paillier = new PaillierT(PaillierT.param);
            while (iterator.hasNext()) {
                EvidenceEntity s = iterator.next();


                //剔除不符合接口要求的数据，这里需要根据接口定制
                //notarizationStatus为"0"的数据不返回
                if(s.getNotarizationStatus().toString().equals("0")){
                    iterator.remove();
                    continue;
                }


                //密文数值比较阶段

                if(fileSizeFloor!=-1){
                    if(!s.getFileSize().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(fileSizeFloor),pk);
                        CipherPub ckw= new CipherPub(s.getFileSize());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
                if(fileSizeUpper!=-1){
                    if(!s.getFileSize().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(fileSizeUpper),pk);
                        CipherPub ckw= new CipherPub(s.getFileSize());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
                //密文字符通配阶段
                if(!evidenceName.equals("none")){
                    if(!s.getEvidenceName().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);

                        K2C16 k2c16 = new K2C16(evidenceName, pk, paillier);
                        k2c16.StepOne();
                        CipherPub cqw=k2c16.FIN;
                        CipherPub ckw= new CipherPub(s.getEvidenceName());

//                        System.out.println(paillier.SDecryption(ckw));

                        KET ket = new KET(cqw,ckw,paillier);
                        ket.StepOne();
                        ket.StepTwo();
                        ket.StepThree();
                        CipherPub cans=ket.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }



//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                EvidenceEntity s = iterator.next();

                Integer i;
                i=Integer.parseInt(s.getNotarizationStatus().toString());
                s.setNotarizationStatus(notarizationStatuses[i]);

                i=Integer.parseInt(s.getEvidenceType().toString());
                s.setEvidenceType(evidenceTypes[i]);

                if(s.getTransactionStatus()!=null){
                    i=Integer.parseInt(s.getTransactionStatus().toString());
                    s.setTransactionStatus(transactionStatuses[i]);
                }
                if(s.getNotarizationType()!=null){
                    i=Integer.parseInt(s.getNotarizationType().toString());
                    s.setNotarizationType(notarizationTypes[i]);
                }



                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1){
//解密数字

                    if(!s.getFileSize().equals("")){
                        s.setFileSize(paillier.SDecryption(new CipherPub(s.getFileSize())).intValue()+"");
                    }
                    if(!s.getNotarizationMoney().equals("")){
                        s.setNotarizationMoney(paillier.SDecryption(new CipherPub(s.getNotarizationMoney())).intValue()+"");
                    }

                    //解密字符
                    if(!s.getEvidenceName().equals("")){
                        s.setEvidenceName(K2C16.parseString(paillier.SDecryption(new CipherPub(s.getEvidenceName())),paillier));
                    }
                }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }

    public static Map<String,Object> userSelect (JSONObject params, AutmanMapper autmanMapper){
        Map<String,Object> result=new HashMap<>();

        try{

            //直接筛选的

            String userId="none";
            if(params.containsKey("userId")){
                userId=params.get("userId").toString();
            }

            String sex="none";
            if(params.containsKey("sex")){
                sex=params.get("sex").toString();
            }

            //要通配的明文字符串

            String usernameWildcard="none";
            if(params.containsKey("usernameWildcard")){
                usernameWildcard=params.get("usernameWildcard").toString();
            }

            String phoneNumberWildcard="none";
            if(params.containsKey("phoneNumberWildcard")){
                phoneNumberWildcard=params.get("phoneNumberWildcard").toString();
            }

            String emailWildcard="none";
            if(params.containsKey("emailWildcard")){
                emailWildcard=params.get("emailWildcard").toString();
            }

            //要比大小的date


            //要比大小的明文


            //要比大小的密文

            Integer remainsFloor=-1;
            if(params.containsKey("remainsFloor")){
                remainsFloor=Integer.parseInt(params.get("remainsFloor").toString());
            }

            Integer remainsUpper=-1;
            if(params.containsKey("remainsUpper")){
                remainsUpper=Integer.parseInt(params.get("remainsUpper").toString());
            }

            Integer storageSpaceFloor=-1;
            if(params.containsKey("storageSpaceFloor")){
                storageSpaceFloor=Integer.parseInt(params.get("storageSpaceFloor").toString());
            }

            Integer storageSpaceUpper=-1;
            if(params.containsKey("storageSpaceUpper")){
                storageSpaceUpper=Integer.parseInt(params.get("storageSpaceUpper").toString());
            }

            Integer hasUsedStorageFloor=-1;
            if(params.containsKey("hasUsedStorageFloor")){
                hasUsedStorageFloor=Integer.parseInt(params.get("hasUsedStorageFloor").toString());
            }

            Integer hasUsedStorageUpper=-1;
            if(params.containsKey("hasUsedStorageUpper")){
                hasUsedStorageUpper=Integer.parseInt(params.get("hasUsedStorageUpper").toString());
            }

            //要通配的密文

            String idCard="none";
            if(params.containsKey("idCard")){
                idCard=params.get("idCard").toString();
            }

            //解密标识符
            Integer decryptFlag=0;
            if(params.containsKey("decryptFlag")){
                decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            }




            //查询数据库
            List<UserEntity> data = autmanMapper.findUser( userId,  usernameWildcard,  phoneNumberWildcard,
                    emailWildcard,  sex);


            //在密文下匹配，剔除不合格的数据
            PaillierT paillier = new PaillierT(PaillierT.param);
            Iterator<UserEntity> iterator = data.iterator();
            while (iterator.hasNext()) {
                UserEntity s = iterator.next();


                //剔除不符合接口要求的数据，这里需要根据接口定制



                //密文数值比较阶段

                if(remainsFloor!=-1){
                    if(!s.getRemains().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(remainsFloor),pk);
                        CipherPub ckw= new CipherPub(s.getRemains());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }

                }
                if(remainsUpper!=-1){
                    if(!s.getRemains().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(remainsUpper),pk);
                        CipherPub ckw= new CipherPub(s.getRemains());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(ckw,cqw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
                if(storageSpaceFloor!=-1){
                    if(!s.getStorageSpace().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(storageSpaceFloor),pk);
                        CipherPub ckw= new CipherPub(s.getStorageSpace());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
                if(storageSpaceUpper!=-1){
                    if(!s.getStorageSpace().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(storageSpaceUpper),pk);
                        CipherPub ckw= new CipherPub(s.getStorageSpace());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(ckw,cqw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }

                if(hasUsedStorageFloor!=-1){
                    if(!s.getHasUsedStorage().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(hasUsedStorageFloor),pk);
                        CipherPub ckw= new CipherPub(s.getHasUsedStorage());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
                if(hasUsedStorageUpper!=-1){
                    if(!s.getHasUsedStorage().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(hasUsedStorageUpper),pk);
                        CipherPub ckw= new CipherPub(s.getHasUsedStorage());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }



                //密文字符通配阶段
                if(!idCard.equals("none")){
                    if(!s.getIdCard().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);

                        K2C8 k2c8 = new K2C8(idCard, pk, paillier);
                        k2c8.StepOne();
                        CipherPub cqw=k2c8.FIN;
                        CipherPub ckw= new CipherPub(s.getIdCard());

//                        System.out.println(paillier.SDecryption(ckw));

                        KET ket = new KET(cqw,ckw,paillier);
                        ket.StepOne();
                        ket.StepTwo();
                        ket.StepThree();
                        CipherPub cans=ket.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }



//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                UserEntity s = iterator.next();

                Integer i=Integer.parseInt(s.getSex().toString());
                s.setSex(sexs[i]);

                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1){

                    //解密数字

                    if(!s.getRemains().equals("")){
                        s.setRemains(paillier.SDecryption(new CipherPub(s.getRemains())).intValue()+"");
                    }
                    if(!s.getStorageSpace().equals("")){
                        s.setStorageSpace(paillier.SDecryption(new CipherPub(s.getStorageSpace())).intValue()+"");
                    }
                    if(!s.getHasUsedStorage().equals("")){
                        s.setHasUsedStorage(paillier.SDecryption(new CipherPub(s.getHasUsedStorage())).intValue()+"");
                    }

                    //解密字符

                    if(!s.getIdCard().equals("")){
                        s.setIdCard(K2C8.parseString(paillier.SDecryption(new CipherPub(s.getIdCard())),paillier));
                    }


                }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    public static Map<String,Object> orgSelect (JSONObject params, AutmanMapper autmanMapper){
        Map<String,Object> result=new HashMap<>();

        try{

            //直接筛选的
            String organizationId=params.get("organizationId").toString();

            //要通配的明文字符串
            String organizationIdNameWildcard=params.get("organizationIdNameWildcard").toString();
            String addressWildcard=params.get("addressWildcard").toString();
            String phoneNumberWildcard=params.get("phoneNumberWildcard").toString();
            String legalPeopleWildcard=params.get("legalPeopleWildcard").toString();
            String emailWildcard=params.get("emailWildcard").toString();
            //要比大小的date


            //要比大小的明文


            //要比大小的密文

            //要通配的密文

            //解密标识符
//                    Integer decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            Integer decryptFlag=0;
            if(params.containsKey("decryptFlag")){
                decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            }
            //查询数据库
            List<OrganizationEntity> data = autmanMapper.findOrganization( organizationId ,  organizationIdNameWildcard,  addressWildcard,
                    phoneNumberWildcard,  legalPeopleWildcard,  emailWildcard);



            //在密文下匹配，剔除不合格的数据
            Iterator<OrganizationEntity> iterator = data.iterator();
            while (iterator.hasNext()) {
                OrganizationEntity s = iterator.next();

                //剔除不符合接口要求的数据，这里需要根据接口定制

                //密文数值比较阶段

                //密文字符通配阶段

//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                OrganizationEntity s = iterator.next();

//                        Integer i=Integer.parseInt(s.getTransactionStatus().toString());
//                        s.setTransactionStatus(transactionStatuses[i]);

                //如果有解密标记，还要把密文替换为明文
//                        if(decryptFlag==1){
//
//                        }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    public static Map<String,Object> notaSelect (JSONObject params, AutmanMapper autmanMapper){
        Map<String,Object> result=new HashMap<>();

        try{


            //直接筛选的

            String notaryId="none";
            if(params.containsKey("notaryId")){
                notaryId=params.get("notaryId").toString();
            }

            String organizationId="none";
            if(params.containsKey("organizationId")){
                organizationId=params.get("organizationId").toString();
            }

            String sex="none";
            if(params.containsKey("sex")){
                sex=params.get("sex").toString();
            }

            String notarizationType="none";
            if(params.containsKey("notarizationType")){
                notarizationType=params.get("notarizationType").toString();
            }

            //要通配的明文字符串

            String notaryNameWildcard="none";
            if(params.containsKey("notaryNameWildcard")){
                notaryNameWildcard=params.get("notaryNameWildcard").toString();
            }

            String phoneNumberWildcard="none";
            if(params.containsKey("phoneNumberWildcard")){
                phoneNumberWildcard=params.get("phoneNumberWildcard").toString();
            }

            String emailWildcard="none";
            if(params.containsKey("emailWildcard")){
                emailWildcard=params.get("emailWildcard").toString();
            }

            String jobNumberWildcard="none";
            if(params.containsKey("jobNumberWildcard")){
                jobNumberWildcard=params.get("jobNumberWildcard").toString();
            }
            //要比大小的date


            //要比大小的明文


            //要比大小的密文

            //要通配的密文

            String idCard="none";
            if(params.containsKey("idCard")){
                idCard=params.get("idCard").toString();
            }
            //解密标识符
//                    Integer decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            Integer decryptFlag=0;
            if(params.containsKey("decryptFlag")){
                decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            }
            //查询数据库
            List<NotaryEntity> data = autmanMapper.findNotary( notaryId ,  notaryNameWildcard,  phoneNumberWildcard,  jobNumberWildcard,
                    emailWildcard,  sex,  organizationId,  notarizationType );


            PaillierT paillier = new PaillierT(PaillierT.param);
            //在密文下匹配，剔除不合格的数据
            Iterator<NotaryEntity> iterator = data.iterator();
            while (iterator.hasNext()) {
                NotaryEntity s = iterator.next();

                //剔除不符合接口要求的数据，这里需要根据接口定制

                //密文数值比较阶段

                //密文字符通配阶段

                if(!idCard.equals("none")){
                    if(!s.getIdCard().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);

                        K2C8 k2c8 = new K2C8(idCard, pk, paillier);
                        k2c8.StepOne();
                        CipherPub cqw=k2c8.FIN;
                        CipherPub ckw= new CipherPub(s.getIdCard());

//                        System.out.println(paillier.SDecryption(ckw));

                        KET ket = new KET(cqw,ckw,paillier);
                        ket.StepOne();
                        ket.StepTwo();
                        ket.StepThree();
                        CipherPub cans=ket.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                NotaryEntity s = iterator.next();

                Integer i;
                i=Integer.parseInt(s.getSex().toString());
                s.setSex(sexs[i]);
                i=Integer.parseInt(s.getNotarizationType().toString());
                s.setNotarizationType(notarizationTypes[i]);

                //如果有解密标记，还要把密文替换为明文
                        if(decryptFlag==1){
                            //解密字符

                            if(!s.getIdCard().equals("")){
                                s.setIdCard(K2C8.parseString(paillier.SDecryption(new CipherPub(s.getIdCard())),paillier));
                            }
                        }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }



    public static Map<String,Object> autmanSelect (JSONObject params, AutmanMapper autmanMapper){
        Map<String,Object> result=new HashMap<>();

        try{


            //直接筛选的
            String autManId="none";
            if(params.containsKey("autManId")){
                autManId=params.get("autManId").toString();
            }

            String organizationId="none";
            if(params.containsKey("organizationId")){
                organizationId=params.get("organizationId").toString();
            }

            String sex="none";
            if(params.containsKey("sex")){
                sex=params.get("sex").toString();
            }
            //要通配的明文字符串
            String autNameWildcard="none";
            if(params.containsKey("autNameWildcard")){
                autNameWildcard=params.get("autNameWildcard").toString();
            }

            String phoneNumberWildcard="none";
            if(params.containsKey("phoneNumberWildcard")){
                phoneNumberWildcard=params.get("phoneNumberWildcard").toString();
            }

            String emailWildcard="none";
            if(params.containsKey("emailWildcard")){
                emailWildcard=params.get("emailWildcard").toString();
            }

            String jobNumberWildcard="none";
            if(params.containsKey("jobNumberWildcard")){
                jobNumberWildcard=params.get("jobNumberWildcard").toString();
            }
            //要比大小的date


            //要比大小的明文


            //要比大小的密文

            //要通配的密文

            String idCard="none";
            if(params.containsKey("idCard")){
                idCard=params.get("idCard").toString();
            }
            //解密标识符
//                    Integer decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            Integer decryptFlag=0;
            if(params.containsKey("decryptFlag")){
                decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            }
            //查询数据库
            List<AutManagerEntity> data = autmanMapper.findAutman( autManId ,  autNameWildcard,  phoneNumberWildcard,  jobNumberWildcard,
                    emailWildcard,  sex,  organizationId);



            //在密文下匹配，剔除不合格的数据
            Iterator<AutManagerEntity> iterator = data.iterator();
            PaillierT paillier = new PaillierT(PaillierT.param);
            while (iterator.hasNext()) {
                AutManagerEntity s = iterator.next();

                //剔除不符合接口要求的数据，这里需要根据接口定制

                //密文数值比较阶段

                //密文字符通配阶段
                if(!idCard.equals("none")){
                    if(!s.getIdCard().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);

                        K2C8 k2c8 = new K2C8(idCard, pk, paillier);
                        k2c8.StepOne();
                        CipherPub cqw=k2c8.FIN;
                        CipherPub ckw= new CipherPub(s.getIdCard());

//                        System.out.println(paillier.SDecryption(ckw));

                        KET ket = new KET(cqw,ckw,paillier);
                        ket.StepOne();
                        ket.StepTwo();
                        ket.StepThree();
                        CipherPub cans=ket.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }
//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                AutManagerEntity s = iterator.next();

                Integer i=Integer.parseInt(s.getSex().toString());
                s.setSex(sexs[i]);

                //如果有解密标记，还要把密文替换为明文
                        if(decryptFlag==1){
                            //解密字符

                            if(!s.getIdCard().equals("")){
                                s.setIdCard(K2C8.parseString(paillier.SDecryption(new CipherPub(s.getIdCard())),paillier));
                            }
                        }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    public static Map<String,Object> transSelect (JSONObject params, AutmanMapper autmanMapper){
        Map<String,Object> result=new HashMap<>();
        PaillierT paillier = new PaillierT(PaillierT.param);
        try{

            //直接筛选的
            String transactionType="none";
            if(params.containsKey("transactionType")){
                transactionType=params.get("transactionType").toString();
            }
            //要通配的明文字符串
            String usernameWildcard="none";
            if(params.containsKey("usernameWildcard")){
                usernameWildcard=params.get("usernameWildcard").toString();
            }
            //要比大小的date
            String transactionTimeStart="none";
            if(params.containsKey("transactionTimeStart")){
                transactionTimeStart=params.get("transactionTimeStart").toString();
            }

            String transactionTimeEnd="none";
            if(params.containsKey("transactionTimeEnd")){
                transactionTimeEnd=params.get("transactionTimeEnd").toString();
            }
            //要比大小的明文


            //要比大小的密文
            Integer transactionMoneyFloor=-1;
            if(params.containsKey("transactionMoneyFloor")){
                transactionMoneyFloor=Integer.parseInt(params.get("transactionMoneyFloor").toString());
            }
            Integer transactionMoneyUpper=-1;
            if(params.containsKey("transactionMoneyUpper")){
                transactionMoneyUpper=Integer.parseInt(params.get("transactionMoneyUpper").toString());
            }
            //要通配的密文

            String transactionPeople="none";
            if(params.containsKey("transactionPeople")){
                transactionPeople=params.get("transactionPeople").toString();
            }

            //解密标识符
            Integer decryptFlag=0;
            if(params.containsKey("decryptFlag")){
                decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());
            }




            //查询数据库
            List<TransactionEntity> data = autmanMapper.findTransaction("none",
                    transactionType,  usernameWildcard,  transactionTimeStart,
                    transactionTimeEnd);



            //在密文下匹配，剔除不合格的数据
            Iterator<TransactionEntity> iterator = data.iterator();
            while (iterator.hasNext()) {
                TransactionEntity s = iterator.next();


                //剔除不符合接口要求的数据，这里需要根据接口定制



                //密文数值比较阶段

                if(transactionMoneyFloor!=-1){
                    if(!s.getTransactionMoney().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(transactionMoneyFloor),pk);
                        CipherPub ckw= new CipherPub(s.getTransactionMoney());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(cqw,ckw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }

                }
                if(transactionMoneyUpper!=-1){
                    if(!s.getTransactionMoney().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);
                        CipherPub cqw=paillier.Encryption(BigInteger.valueOf(transactionMoneyUpper),pk);
                        CipherPub ckw= new CipherPub(s.getTransactionMoney());

//                        System.out.println(paillier.SDecryption(ckw));

                        SLT SK1 = new SLT(ckw,cqw,paillier);

                        SK1.StepOne();
                        SK1.StepTwo();
                        SK1.StepThree();
                        CipherPub cans=SK1.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }


                //密文字符通配阶段
                if(!transactionPeople.equals("none")){
                    if(!s.getTransactionPeople().equals("")){
                        BigInteger pk = paillier.g.modPow(new BigInteger(1024 - 12, 64, new Random()), paillier.nsquare);

                        K2C8 k2c8 = new K2C8(transactionPeople, pk, paillier);
                        k2c8.StepOne();
                        CipherPub cqw=k2c8.FIN;
                        CipherPub ckw= new CipherPub(s.getTransactionPeople());

//                        System.out.println(paillier.SDecryption(ckw));

                        KET ket = new KET(cqw,ckw,paillier);
                        ket.StepOne();
                        ket.StepTwo();
                        ket.StepThree();
                        CipherPub cans=ket.FIN;
                        if(paillier.SDecryption(cans).intValue()!=1){
                            iterator.remove();
                        }

                    }else{
                        iterator.remove();
                    }
                }


//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                TransactionEntity s = iterator.next();

                Integer i ;
                i=Integer.parseInt(s.getTransactionType().toString());
                s.setTransactionType(transactionTypes[i]);

                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1){
                    if(!s.getTransactionMoney().equals("")){
                        s.setTransactionMoney(paillier.SDecryption(new CipherPub(s.getTransactionMoney())).intValue()+"");
                    }
                    if(!s.getUserRemains().equals("")){
                        s.setUserRemains(paillier.SDecryption(new CipherPub(s.getUserRemains())).intValue()+"");
                    }
                    if(!s.getStorageSize().equals("")){
                        s.setStorageSize(paillier.SDecryption(new CipherPub(s.getStorageSize())).intValue()+"");
                    }

                    if(!s.getTransactionPeople().equals("")){
                        s.setTransactionPeople(K2C8.parseString(paillier.SDecryption(new CipherPub(s.getTransactionPeople())),paillier));
                    }

                }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }




    /**
     * 查询申请公证的记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/notarRecord")
    public Object notarRecord (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            if(params.containsKey("notarizationStatus")){
                if(params.get("notarizationStatus").toString().equals("0")){
                    result.put("status",false);
                    result.put("message","公证状态不能为“未公证”！");
                    return result;
                }
            }
            Map<String,Object> rs= eviSelect(params,autmanMapper);
            //去掉状态为“0”的记录，之后要记得写



            return rs;

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }



    /**
     * 查询交易记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/transQuery")
    public Object transQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            return transSelect(params,autmanMapper);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 查询所有用户记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/userQuery")
    public Object userQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            return userSelect(params,autmanMapper);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 查询所有公证员记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/notaQuery")
    public Object notaQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            return notaSelect(params,autmanMapper);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 查询所有公证管理员员记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/autmanQuery")
    public Object autmanQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            return autmanSelect(params,autmanMapper);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 查询所有公证机构记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/orgaQuery")
    public Object orgaQuery  (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            return orgSelect(params,autmanMapper);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 查询所有证据记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/evidenceQuery")
    public Object evidenceQuery  (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            return eviSelect2(params,autmanMapper);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }

    /**
     * 查询公证数量
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/noNumQuery")
    public Object noNumQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            //查询数据库
            int successNum = autmanMapper.totalSuccess();
            int notSuccessNum = autmanMapper.totalNotSuccess();

            JSONObject data=new JSONObject();
            data.put("successNum",successNum);
            data.put("notSuccessNum",notSuccessNum);
            data.put("totalNum",successNum+notSuccessNum);


            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }





    /**
     * 查询所有公证类型
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/noTypeQuery")
    public Object noTypeQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            int l=notarizationTypes.length;

            List<JSONObject> data = new Vector<>();
            for(Integer i=0;i<l;i++){
                JSONObject s=new JSONObject();
                s.put("notarizationType",i.toString());
                s.put("notarizationTypeName",notarizationTypes[i]);
                data.add(s);
            }


            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }




    /**
     * 查询所有交易类型
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/tranTypeQuery")
    public Object tranTypeQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            int l=transactionTypes.length;

            List<JSONObject> data = new Vector<>();
            for(Integer i=0;i<l;i++){
                JSONObject s=new JSONObject();
                s.put("transactionType",i.toString());
                s.put("transactionTypeName",transactionTypes[i]);
                data.add(s);
            }


            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }

    /**
     * 查询所有证据类型
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/eviTypeQuery")
    public Object eviTypeQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            int l=evidenceTypes.length;

            List<JSONObject> data = new Vector<>();
            for(Integer i=0;i<l;i++){
                JSONObject s=new JSONObject();
                s.put("evidenceType",i.toString());
                s.put("evidenceTypeName",evidenceTypes[i]);
                data.add(s);
            }


            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }

    /**
     * 查询所有证据类型
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/notPayQuery")
    public Object notPayQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            int l=notarizationMoneys.length;

            List<JSONObject> data = new Vector<>();
            for(Integer i=0;i<l;i++){
                JSONObject s=new JSONObject();
                s.put("notarizationType",i.toString());
                s.put("notarizationTypeName",notarizationTypes[i]);
                s.put("notarizationMoney",notarizationMoneys[i]);
                data.add(s);
            }


            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }




    /**
     * 公证员统计时间查询
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/notStaTimeQuery")
    public Object notStaTimeQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{

            //查询数据库
            List<String> data = autmanMapper.findNotaTimes();

            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 公证员统计时间查询
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/orgStaTimeQuery ")
    public Object orgStaTimeQuery  (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{

            //查询数据库
            List<String> data = autmanMapper.findOrgTimes();

            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }





    @Autowired(required = false)
    NotaryStatisticsMapper notaryStatisticsMapper;
    /**
     * 公证员统计查询
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/notaStasQue")
    public Object notaStasQue (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);



            //直接筛选的
            String timeFlag=params.get("timeFlag").toString();

            //要通配的明文字符串

            //要比大小的date

            //要比大小的明文

            //要比大小的密文

            //要通配的密文

            //解密标识符
            Integer decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());


            //查询数据库
            List<NotaryStatisticsEntity> data = notaryStatisticsMapper.selectBytimeFlag(timeFlag);



            //在密文下匹配，剔除不合格的数据
            Iterator<NotaryStatisticsEntity> iterator = data.iterator();
            while (iterator.hasNext()) {
                NotaryStatisticsEntity s = iterator.next();


                //剔除不符合接口要求的数据，这里需要根据接口定制



                //密文数值比较阶段



                //密文字符通配阶段




//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                NotaryStatisticsEntity s = iterator.next();
                Integer i=Integer.parseInt(s.getNotarizationType().toString());
                s.setNotarizationType(notarizationTypes[i]);
//                        Integer i=Integer.parseInt(s.getTransactionStatus().toString());
//                        s.setTransactionStatus(transactionStatuses[i]);

                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1 && s.getNotarizationTotalMoney() != null){
                    //解密公证总金额
                    CipherPub tol_money = new CipherPub(s.getNotarizationTotalMoney());
                    PaillierT paillier = new PaillierT(PaillierT.param);
                    BigInteger ans=paillier.SDecryption(tol_money);
                    s.setNotarizationTotalMoney(ans.toString());
                }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }

    /**
     * 公证员统计生成
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/notaStasGen")
    public Object notaStasGen (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            //生成当前时间戳
            Date time = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current = sdf.format(time);

            //数据库操作
            notaryStatisticsMapper.notayStatisticsGen(current);
            List<EvidenceEntity> notaryNotarizationMoney = notaryStatisticsMapper.getNotarizationMoney();

            //计算每个公证员的公证总金额
            Map<String, String> notary_Money = new HashMap<String,String>();
            Iterator<EvidenceEntity> iterator = notaryNotarizationMoney.iterator();
            //在密文状态下计算notary的公证总金额
            while (iterator.hasNext()) {
                EvidenceEntity cur = iterator.next();
                if(notary_Money.get(cur.getNotaryId()) != null){
                    //从数据库读取密文后，转成CipherPub进行同态计算
                    CipherPub cur_money= new CipherPub(cur.getNotarizationMoney());
                    CipherPub total_money= new CipherPub(notary_Money.get(cur.getNotaryId()));
                    //跨域加法协议
                    //Paillier初始化,我这里为了确保参数一致，把它放到类里面了，其实应该保存在加密文件里的
                    PaillierT paillier = new PaillierT(PaillierT.param);
                    SAD SA1 = new SAD(cur_money,total_money,paillier);
                    SA1.StepOne();
                    SA1.StepTwo();
                    SA1.StepThree();
                    CipherPub cans=SA1.FIN;
                    notary_Money.put(cur.getNotaryId(),cans.toString());
//                    System.out.println(cans.toString().length());
                }
                else{
                    notary_Money.put(cur.getNotaryId(),cur.getNotarizationMoney());
                }
            }
            //写回数据库
            for (Map.Entry<String, String> entry : notary_Money.entrySet()) {
                //防止溢出，将总和解密后再重新加密
                PaillierT paillier = new PaillierT(PaillierT.param);
                CipherPub tol= new CipherPub(entry.getValue());
                BigInteger ans=paillier.SDecryption(tol);
                System.out.println("total="+ans);
                //生成临时公私钥
                BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
                BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);
                String tmp=paillier.Encryption(ans,pk).toString();
                notaryStatisticsMapper.setNotarizationTotalMoney(entry.getKey(), tmp, current);
            }


            //填充返回值
            result.put("status",true);
            result.put("message","success");

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 公证员排名查询
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/rankStasQue")
    public Object rankStasQue (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);



            //直接筛选的
            String timeFlag=params.get("timeFlag").toString();
            Integer sort =Integer.parseInt(params.get("sort").toString());

            //要通配的明文字符串

            //要比大小的date

            //要比大小的明文

            //要比大小的密文

            //要通配的密文

            //解密标识符
            Integer decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());


            //查询数据库
            List<NotaryStatisticsEntity> data;
            if(sort == 0){
                data = notaryStatisticsMapper.rankBytimeFlagAndsort(timeFlag, "notarizationCount");
            }else{
                data = notaryStatisticsMapper.rankBytimeFlagAndsort(timeFlag, "notarizationSuccessCount");
            }

            //在密文下匹配，剔除不合格的数据
            Iterator<NotaryStatisticsEntity> iterator = data.iterator();
            while (iterator.hasNext()) {
                NotaryStatisticsEntity s = iterator.next();


                //剔除不符合接口要求的数据，这里需要根据接口定制



                //密文数值比较阶段



                //密文字符通配阶段




//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                NotaryStatisticsEntity s = iterator.next();
                Integer i=Integer.parseInt(s.getNotarizationType().toString());
                s.setNotarizationType(notarizationTypes[i]);
//                        Integer i=Integer.parseInt(s.getTransactionStatus().toString());
//                        s.setTransactionStatus(transactionStatuses[i]);

                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1 && s.getNotarizationTotalMoney() != null) {
                    //解密公证总金额
                    CipherPub tol_money = new CipherPub(s.getNotarizationTotalMoney());
                    PaillierT paillier = new PaillierT(PaillierT.param);
                    BigInteger ans = paillier.SDecryption(tol_money);
                    s.setNotarizationTotalMoney(ans.toString());
                }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }



    @Autowired(required = false)
    OrganizationStatisticsMapper organizationStatisticsMapper;
    /**
     * 公证机构统计生成
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/orgStasGen")
    public Object orgStasGen (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            //数据库操作
            //生成当前时间戳
            Date time = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current = sdf.format(time);

            organizationStatisticsMapper.OrganizationStatisticsGen(current);
            List<EvidenceEntity> organizationNotarizationMoney = organizationStatisticsMapper.getNotarizationMoney();

            //计算每个公证机构的公证总金额
            Map<String, String> organization_Money = new HashMap<String,String>();
            Iterator<EvidenceEntity> iterator = organizationNotarizationMoney.iterator();
            //在密文状态下计算notary的公证总金额
            while (iterator.hasNext()) {
                EvidenceEntity cur = iterator.next();
                if(organization_Money.get(cur.getOrganizationId()) != null){
                    //从数据库读取密文后，转成CipherPub进行同态计算
                    CipherPub cur_money= new CipherPub(cur.getNotarizationMoney());
                    CipherPub total_money= new CipherPub(organization_Money.get(cur.getOrganizationId()));
                    //跨域加法协议
                    //Paillier初始化,我这里为了确保参数一致，把它放到类里面了，其实应该保存在加密文件里的
                    PaillierT paillier = new PaillierT(PaillierT.param);
                    SAD SA1 = new SAD(cur_money,total_money,paillier);
                    SA1.StepOne();
                    SA1.StepTwo();
                    SA1.StepThree();
                    CipherPub cans=SA1.FIN;
                    organization_Money.put(cur.getOrganizationId(),cans.toString());
//                    System.out.println(cans.toString().length());
                }
                else{
                    organization_Money.put(cur.getOrganizationId(),cur.getNotarizationMoney());
                }
            }
            //写回数据库
            for (Map.Entry<String, String> entry : organization_Money.entrySet()) {
                //防止溢出，将总和解密后再重新加密
                PaillierT paillier = new PaillierT(PaillierT.param);
                CipherPub tol= new CipherPub(entry.getValue());
                BigInteger ans=paillier.SDecryption(tol);
                System.out.println("total="+ans);
                //生成临时公私钥
                BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
                BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);
                String tmp=paillier.Encryption(ans,pk).toString();
                organizationStatisticsMapper.setNotarizationTotalMoney(entry.getKey(), tmp, current);
            }

            //填充返回值
            result.put("status",true);
            result.put("message","success");

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 公证机构统计查询
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/orgStasQue")
    public Object orgStasQue (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);



            //直接筛选的
            String timeFlag=params.get("timeFlag").toString();

            //要通配的明文字符串

            //要比大小的date

            //要比大小的明文

            //要比大小的密文

            //要通配的密文

            //解密标识符
            Integer decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());


            //查询数据库
            List<OrganizationStatisticsEntity> data = organizationStatisticsMapper.selectBytimeFlag(timeFlag);



            //在密文下匹配，剔除不合格的数据
            Iterator<OrganizationStatisticsEntity> iterator = data.iterator();
            while (iterator.hasNext()) {
                OrganizationStatisticsEntity s = iterator.next();


                //剔除不符合接口要求的数据，这里需要根据接口定制



                //密文数值比较阶段



                //密文字符通配阶段




//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                OrganizationStatisticsEntity s = iterator.next();
//                Integer i=Integer.parseInt(s.getNotarizationType().toString());
//                s.setNotarizationType(notarizationTypes[i]);

                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1 && s.getNotarizationTotalMoney() != null) {
                    //解密公证总金额
                    CipherPub tol_money = new CipherPub(s.getNotarizationTotalMoney());
                    PaillierT paillier = new PaillierT(PaillierT.param);
                    BigInteger ans = paillier.SDecryption(tol_money);
                    s.setNotarizationTotalMoney(ans.toString());
                }
            }






            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",data);

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }



    /**
     * 机构管理员登录
     * @param req
     * @return 登录成功返回autManId
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/aut/login")
    public Object loginAut(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("autName")){
                result.put("status",false);
                result.put("message","没有给出autName");
                return result;
            }
            if(params.get("autName").toString().equals("none")){
                result.put("status",false);
                result.put("message","autName不能为空");
                return result;
            }

            if(!params.containsKey("password")){
                result.put("status",false);
                result.put("message","没有给出password");
                return result;
            }
            if(params.get("password").toString().equals("none")){
                result.put("status",false);
                result.put("message","password不能为空");
                return result;
            }


            // 获取参数
            String autName = params.get("autName").toString();
            String password = params.get("password").toString();

            // 将密码用sha-256加密
            password = Sha256.SHA(password);
            System.out.println("SHA-256后密码为：" + password);

            // 判断用户名和密码对是否存在数据库中
            AutManagerEntity u1 = null;
            u1 = autmanMapper.selectByNameAndPwd(autName,password);

            if(u1 != null){
                result.put("status",true);
                result.put("message","登录成功!");
                // 登录成功返回autManId
                result.put("autManId",u1.getAutManId());
            }
            else{
                result.put("status",false);
                result.put("message","登录失败!");
            }



        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 机构管理员注册
     * @param req
     * @return 注册成功返回autManId
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/aut/regist")
    public Object registerAut(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{
            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("autName")){
                result.put("status",false);
                result.put("message","没有给出autName");
                return result;
            }
            if(params.get("autName").toString().equals("none")){
                result.put("status",false);
                result.put("message","autName不能为空");
                return result;
            }

            if(!params.containsKey("password")){
                result.put("status",false);
                result.put("message","没有给出password");
                return result;
            }
            if(params.get("password").toString().equals("none")){
                result.put("status",false);
                result.put("message","password不能为空");
                return result;
            }

            if(!params.containsKey("phoneNumber")){
                result.put("status",false);
                result.put("message","没有给出phoneNumber");
                return result;
            }
            if(params.get("phoneNumber").toString().equals("none")){
                result.put("status",false);
                result.put("message","phoneNumber不能为空");
                return result;
            }

            if(!params.containsKey("idCard")){
                result.put("status",false);
                result.put("message","没有给出idCard");
                return result;
            }
            if(params.get("idCard").toString().equals("none")){
                result.put("status",false);
                result.put("message","idCard不能为空");
                return result;
            }

            if(!params.containsKey("email")){
                result.put("status",false);
                result.put("message","没有给出email");
                return result;
            }
            if(params.get("email").toString().equals("none")){
                result.put("status",false);
                result.put("message","email不能为空");
                return result;
            }

            if(!params.containsKey("sex")){
                result.put("status",false);
                result.put("message","没有给出sex");
                return result;
            }
            if(params.get("sex").toString().equals("none")){
                result.put("status",false);
                result.put("message","sex不能为空");
                return result;
            }

            if(!params.containsKey("organizationId")){
                result.put("status",false);
                result.put("message","没有给出organizationId");
                return result;
            }
            if(params.get("organizationId").toString().equals("none")){
                result.put("status",false);
                result.put("message","organizationId不能为空");
                return result;
            }


            // 获取参数
            String autName = params.get("autName").toString();
            String password = params.get("password").toString();
            String phoneNumber = params.get("phoneNumber").toString();
            String idCard = params.get("idCard").toString();
            String email = params.get("email").toString();
            String organizationId = params.get("organizationId").toString();


            // 处理sex枚举类型数据
            String sex = params.get("sex").toString();
//            Integer s = Integer.parseInt(params.get("sex").toString());
//            Sex sex =Sex.getByValue(s);
//            System.out.println("性别为：" + sex.getKey());
//            System.out.println(sex.getValue());

            // 将密码用sha-256加密
            password = Sha256.SHA(password);
            System.out.println("SHA-256后密码为：" + password);

            // 生成autManId
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String autManId = id.replace("-" , "");
            System.out.println("autManId为：" + autManId);


            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);


            // 身份证加密
            System.out.println("身份证为：" + idCard);
            K2C8 SK0 = new K2C8(idCard,pk,paillier);
            System.out.println("K2C8转换后的大整数为：" + SK0.getB());
            SK0.StepOne();
            String sidCard = SK0.FIN.toString();
            System.out.println("K2C8加密后的大整数为：" + sidCard);
            // 将密文赋值给idCard,数据库id_card字段长度也要设置大一点，不然放不下
            idCard = sidCard;

            // 身份证解密测试
            BigInteger midCard = paillier.SDecryption(SK0.FIN);
            System.out.println("解密后的大整数值为：" + midCard);

            String temp = SK0.parseString(midCard,paillier);
            System.out.println("大整数转化为字符串：" + temp);


            // 存入数据库
            int flag = autmanMapper.insertAutman(autManId,autName,password,phoneNumber,idCard,email,sex,organizationId);

            if(flag == 1){
                result.put("status",true);
                result.put("message","注册成功!");
                // 注册成功返回autManId
                result.put("autManId",autManId);
            }
            else{
                result.put("status",false);
                result.put("message","注册失败!");
            }


        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }
        return result;

    }


    /**
     * 机构管理员信息完善
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/aut/update")
    public Object updateUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("autManId")){
                result.put("status",false);
                result.put("message","没有给出autManId");
                return result;
            }
            if(params.get("autManId").toString().equals("none")){
                result.put("status",false);
                result.put("message","autManId不能为空");
                return result;
            }

            if(!params.containsKey("newPassword")){
                result.put("status",false);
                result.put("message","没有给出newPassword");
                return result;
            }
            if(params.get("newPassword").toString().equals("none")){
                result.put("status",false);
                result.put("message","newPassword不能为空");
                return result;
            }

            if(!params.containsKey("phoneNumber")){
                result.put("status",false);
                result.put("message","没有给出phoneNumber");
                return result;
            }
            if(params.get("phoneNumber").toString().equals("none")){
                result.put("status",false);
                result.put("message","phoneNumber不能为空");
                return result;
            }

            if(!params.containsKey("idCard")){
                result.put("status",false);
                result.put("message","没有给出idCard");
                return result;
            }
            if(params.get("idCard").toString().equals("none")){
                result.put("status",false);
                result.put("message","idCard不能为空");
                return result;
            }

            if(!params.containsKey("email")){
                result.put("status",false);
                result.put("message","没有给出email");
                return result;
            }
            if(params.get("email").toString().equals("none")){
                result.put("status",false);
                result.put("message","email不能为空");
                return result;
            }

            if(!params.containsKey("sex")){
                result.put("status",false);
                result.put("message","没有给出sex");
                return result;
            }
            if(params.get("sex").toString().equals("none")){
                result.put("status",false);
                result.put("message","sex不能为空");
                return result;
            }

            if(!params.containsKey("organizationId")){
                result.put("status",false);
                result.put("message","没有给出organizationId");
                return result;
            }
            if(params.get("organizationId").toString().equals("none")){
                result.put("status",false);
                result.put("message","organizationId不能为空");
                return result;
            }


            // 获取参数
            String autManId = params.get("autManId").toString();
            String newPassword = params.get("newPassword").toString();
            String phoneNumber = params.get("phoneNumber").toString();
            String idCard = params.get("idCard").toString();
            String email = params.get("email").toString();
            String sex = params.get("sex").toString();
            String organizationId = params.get("organizationId").toString();

            // 将密码用sha-256加密
            newPassword = Sha256.SHA(newPassword);
            System.out.println("SHA-256后密码为：" + newPassword);

            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 身份证加密
            System.out.println("身份证为：" + idCard);
            K2C8 SK0 = new K2C8(idCard,pk,paillier);
            System.out.println("K2C8转换后的大整数为：" + SK0.getB());
            SK0.StepOne();
            String sidCard = SK0.FIN.toString();
            System.out.println("K2C8加密后的大整数为：" + sidCard);
            // 将密文赋值给idCard,数据库id_card字段长度也要设置大一点，不然放不下
            idCard = sidCard;

            // 身份证解密测试
            BigInteger midCard = paillier.SDecryption(SK0.FIN);
            System.out.println("解密后的大整数值为：" + midCard);

            String temp = SK0.parseString(midCard,paillier);
            System.out.println("大整数转化为字符串：" + temp);

            // 更新数据库
            int flag = autmanMapper.updateAutman(autManId,newPassword,phoneNumber,idCard,email,sex,organizationId);

            if(flag == 1){
                result.put("status",true);
                result.put("message","更新成功!");
            }
            else{
                result.put("status",false);
                result.put("message","更新失败!");
            }



        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }

        return result;

    }


    /**
     * 新增公证类型
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/aut/notarTypeAdd")
    public Object addNotarType(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("notarizationTypeName")){
                result.put("status",false);
                result.put("message","没有给出notarizationTypeName");
                return result;
            }
            if(params.get("notarizationTypeName").toString().equals("none")){
                result.put("status",false);
                result.put("message","notarizationTypeName不能为空");
                return result;
            }

            if(!params.containsKey("notarizationMoney")){
                result.put("status",false);
                result.put("message","没有给出notarizationMoney");
                return result;
            }
            if(params.get("notarizationMoney").toString().equals("none")){
                result.put("status",false);
                result.put("message","notarizationMoney不能为空");
                return result;
            }

            // 获取参数
            String notarizationTypeName = params.get("notarizationTypeName").toString();
            String notarizationMoney = params.get("notarizationMoney").toString();

            // 生成notarizationTypeId
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String notarizationTypeId = id.replace("-" , "");
            System.out.println("notarizationTypeId为：" + notarizationTypeId);

            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 加密公证金额
            String snotarizationMoney = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(notarizationMoney)),pk).toString();

            // 存入数据库
            int flag = notarizationTypeMapper.insertNotarizationType(notarizationTypeId,notarizationTypeName,snotarizationMoney);


            if(flag == 1){
                result.put("status",true);
                result.put("message","新增公证类型成功!");
                result.put("notarizationTypeId",notarizationTypeId);
            }
            else{
                result.put("status",false);
                result.put("message","新增公证类型失败!");
            }



        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }

        return result;



    }




    /**
     * 更新公证类型
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/aut/notarTypeUpdate")
    public Object updateNotarType(HttpServletRequest req){


        Map<String,Object> result = new HashMap<>();

        try{

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("notarizationTypeId")){
                result.put("status",false);
                result.put("message","没有给出notarizationTypeId");
                return result;
            }
            if(params.get("notarizationTypeId").toString().equals("none")){
                result.put("status",false);
                result.put("message","notarizationTypeId不能为空");
                return result;
            }

            if(!params.containsKey("notarizationTypeName")){
                result.put("status",false);
                result.put("message","没有给出notarizationTypeName");
                return result;
            }
            if(params.get("notarizationTypeName").toString().equals("none")){
                result.put("status",false);
                result.put("message","notarizationTypeName不能为空");
                return result;
            }

            if(!params.containsKey("notarizationMoney")){
                result.put("status",false);
                result.put("message","没有给出notarizationMoney");
                return result;
            }
            if(params.get("notarizationMoney").toString().equals("none")){
                result.put("status",false);
                result.put("message","notarizationMoney不能为空");
                return result;
            }

            // 获取参数
            String notarizationTypeId = params.get("notarizationTypeId").toString();
            String notarizationTypeName = params.get("notarizationTypeName").toString();
            String notarizationMoney = params.get("notarizationMoney").toString();

            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 加密公证金额
            String snotarizationMoney = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(notarizationMoney)),pk).toString();

            int flag = notarizationTypeMapper.updateNotarizationType(notarizationTypeId,notarizationTypeName,snotarizationMoney);



            if(flag == 1){
                result.put("status",true);
                result.put("message","更新公证类型成功!");
                result.put("notarizationTypeId",notarizationTypeId);
            }
            else{
                result.put("status",false);
                result.put("message","更新公证类型失败!");
            }



        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }

        return result;



    }



    /**
     * 查找公证类型名称和公证金额
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/aut/notarTypeSelect")
    public Object selectNotarType(HttpServletRequest req) {


        Map<String, Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if (!params.containsKey("notarizationTypeId")) {
                result.put("status", false);
                result.put("message", "没有给出notarizationTypeId");
                return result;
            }
            if (params.get("notarizationTypeId").toString().equals("none")) {
                result.put("status", false);
                result.put("message", "notarizationTypeId不能为空");
                return result;
            }

            // 获取参数
            String notarizationTypeId = params.get("notarizationTypeId").toString();

            // 数据库查找
            NotarizationTypeEntity n1 = notarizationTypeMapper.selectNotarizationType(notarizationTypeId);

            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 解密公证金额
            BigInteger mnotarizationMoney = paillier.SDecryption(new CipherPub(n1.getNotarizationMoney()));
            System.out.println("公证金额解密后：" + mnotarizationMoney);


            result.put("status", true);
            result.put("message", "查找成功!");
            result.put("notarizationTypeName", n1.getNotarizationTypeName());
            result.put("notarizationMoney", mnotarizationMoney);


        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status", false);
            result.put("message", str);
        }

        return result;
    }


    /**
     * 机构管理员上传申请材料
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/aut/uplaodMaterialFile")
    public Object addEvidenceUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

//            JSONObject params = ParseRequest.parse(req);

            // 获取参数
            String autManId = req.getParameter("autManId");
            String notarizationType = req.getParameter("notarizationType");

            MultipartHttpServletRequest multipartReq = (MultipartHttpServletRequest) req;
            List<MultipartFile> files = multipartReq.getFiles("file");

            //生成当前时间戳
            Date time = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current_time = sdf.format(time);
            SimpleDateFormat sdf_stamp = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = sdf_stamp.format(time);


//            String userId = params.get("userId").toString();
//            String evidenceType = params.get("evidenceType").toString();
//            String evidenceName = params.get("evidenceName").toString();

            // 1. 文件存放路径 ："/用户id/"
            String folderPath = "/"+autManId+"_"+notarizationType+"_"+timestamp;

            // 2. 转发数据给 云 的 后端 （还未测试，不一定可以发送成功）
            Map<String, Object> map = new HashMap<>();
            map.put("file",files);
            map.put("FolderPath",folderPath);
//            String s = HttpUtils.doPost("http://localhost:8090/uploadFiles", map);

            // 云发回来的数据是一个map，里面存放{"status":false/ture,"message":"xxxx"/success,"data":null/"xxxx"}
            // 可以根据status值来判断是否上传成功
            // 3. 根据返回信息判断是否上传成功，上传不成功，返回失败信息给前端
            Map<String, Object> resInfo = new HashMap<>();
            resInfo.put("status",true);
            resInfo.put("message","xxxxxx");
            Boolean statusInfo = (boolean)resInfo.get("status");
            if(statusInfo == false){
                result.put("status",false);
                result.put("message",resInfo.get("message"));
                return result;
            }

            //4. 上传成功
            //存储信息到数据库



            // 5. 返回成功信息给前端
            result.put("status",true);
            result.put("message","success");

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String str = sw.toString();

            result.put("status",false);
            result.put("message",str);
        }

        return result;

    }


}

