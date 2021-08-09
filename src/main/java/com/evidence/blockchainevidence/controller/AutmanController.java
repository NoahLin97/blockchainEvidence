package com.evidence.blockchainevidence.controller;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.entity.*;
import com.evidence.blockchainevidence.mapper.AutmanMapper;
import com.evidence.blockchainevidence.mapper.NotaryMapper;
import com.evidence.blockchainevidence.mapper.NotaryStatisticsMapper;
import com.evidence.blockchainevidence.mapper.OrganizationStatisticsMapper;
import com.evidence.blockchainevidence.utils.ParseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static com.evidence.blockchainevidence.utils.GlobalParams.*;

@RestController
public class AutmanController {

    @Autowired(required = false)
    AutmanMapper autmanMapper;
    /**
     * 查询申请公证的记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/aut/notarRecord")
    public Object notarRecord (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);



            //直接筛选的
            String notarizationStatus=params.get("notarizationStatus").toString();
            String notarizationType=params.get("notarizationType").toString();
            String paymentStatus =params.get("paymentStatus").toString();
            String evidenceType =params.get("evidenceType").toString();
            String organizationId =params.get("organizationId").toString();

            //要通配的明文字符串
            String evidenceNameWildcard=params.get("evidenceNameWildcard").toString();


            //要比大小的date
            String notarizationStartTimeStart =params.get("notarizationStartTimeStart").toString();
            String notarizationStartTimeEnd =params.get("notarizationStartTimeEnd").toString();
            String notarizationEndTimeStart =params.get("notarizationEndTimeStart").toString();
            String notarizationEndTimeEnd =params.get("notarizationEndTimeEnd").toString();

            //要比大小的明文


            //要比大小的密文
            Integer notarizationMoneyUpper =Integer.parseInt(params.get("notarizationMoneyUpper").toString());
            Integer notarizationMoneyFloor =Integer.parseInt(params.get("notarizationMoneyFloor").toString());
            Integer fileSizeUpper =Integer.parseInt(params.get("fileSizeUpper").toString());
            Integer fileSizeFloor =Integer.parseInt(params.get("fileSizeFloor").toString());

            //要通配的密文



            //解密标识符
            Integer decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());




            //查询数据库
            List<EvidenceEntity> data = autmanMapper.findEvidence("none","none","none",
                    notarizationStatus, notarizationType,
                    paymentStatus, evidenceType, organizationId,
                    evidenceNameWildcard, notarizationStartTimeStart,
                    notarizationStartTimeEnd, notarizationEndTimeStart,
                    notarizationEndTimeEnd);



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

                if(notarizationMoneyUpper!=-1){

                }
                if(notarizationMoneyFloor!=-1){

                }
                if(fileSizeUpper!=-1){

                }
                if(fileSizeFloor!=-1){

                }

                //密文字符通配阶段




//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                EvidenceEntity s = iterator.next();

                Integer i=Integer.parseInt(s.getNotarizationStatus().toString());
                s.setNotarizationStatus(notarizationStatuses[i]);

                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1){

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
    @PostMapping("/aut/transQuery")
    public Object transQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);



            //直接筛选的
            String transactionStatus=params.get("transactionStatus").toString();
            String transactionType=params.get("transactionType").toString();

            //要通配的明文字符串
            String usernameWildcard=params.get("usernameWildcard").toString();

            //要比大小的date
            String transactionTimeStart =params.get("transactionTimeStart").toString();
            String transactionTimeEnd =params.get("transactionTimeEnd").toString();

            //要比大小的明文


            //要比大小的密文
            Integer transactionMoneyFloor =Integer.parseInt(params.get("transactionMoneyFloor").toString());
            Integer transactionMoneyUpper =Integer.parseInt(params.get("transactionMoneyUpper").toString());

            //要通配的密文



            //解密标识符
            Integer decryptFlag =Integer.parseInt(params.get("decryptFlag").toString());




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

                }
                if(transactionMoneyUpper!=-1){

                }


                //密文字符通配阶段




//                if (true) {//如果不匹配
//                    iterator.remove();//使用迭代器的删除方法删除
//                }

            }
//            System.out.println(data.size());


            //遍历替换enum类型的返回值
            iterator = data.iterator();
            while (iterator.hasNext()) {
                TransactionEntity s = iterator.next();

//                        Integer i=Integer.parseInt(s.getTransactionStatus().toString());
//                        s.setTransactionStatus(transactionStatuses[i]);

                //如果有解密标记，还要把密文替换为明文
                if(decryptFlag==1){

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
                if(decryptFlag==1){

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
            //数据库操作
            notaryStatisticsMapper.notayStatisticsGen();

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
                if(decryptFlag==1){

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
            organizationStatisticsMapper.OrganizationStatisticsGen();

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
                if(decryptFlag==1){

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


}