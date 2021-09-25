package com.evidence.blockchainevidence.controller;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.entity.NotaryEntity;
import com.evidence.blockchainevidence.mapper.AutmanMapper;
import com.evidence.blockchainevidence.mapper.NotaryMapper;
import com.evidence.blockchainevidence.service.EvidenceService;
import com.evidence.blockchainevidence.service.NotaryService;
import com.evidence.blockchainevidence.service.UserService;
import com.evidence.blockchainevidence.subprotocols.K2C8;
import com.evidence.blockchainevidence.utils.HttpUtils;
import com.evidence.blockchainevidence.utils.ParseRequest;
import com.evidence.blockchainevidence.utils.Sha256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.evidence.blockchainevidence.controller.AutmanController.eviSelect;


@RestController
public class NotaryController {


    @Autowired(required = false)
    NotaryMapper notaryMapper;
    @Autowired(required = false)
    AutmanMapper autmanMapper;
    @Autowired
    UserService userService;
    @Autowired
    NotaryService notaryService;
    @Autowired
    EvidenceService evidenceService;

    //区块链服务端地址
    public static String blockchain_url = "http://192.168.31.245:8090/";

    /**
     * 查询申请公证的记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/notar/notarRecord")
    public Object notarRecord (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            if(!params.containsKey("notaryId")){
                result.put("status",false);
                result.put("message","给出没有用户Id！");
                return result;
            }
            if(params.get("notaryId").toString().equals("none")){
                result.put("status",false);
                result.put("message","用户Id不能为空！");
                return result;
            }
            if(params.containsKey("notarizationStatus")){
                params.remove("notarizationStatus ");
            }
            if(params.containsKey("transactionStatus")){
                params.remove("transactionStatus ");
            }
            params.put("transactionStatus","1");
            if(!params.containsKey("dealType")){
                result.put("status",false);
                result.put("message","给出没有dealType！");
                return result;
            }
            String dealType = params.get("dealType").toString();

            if(dealType.equals("0")){
                params.put("notarizationStatus","1");
                params.remove("notaryId");
            }else if(dealType.equals("1")){
                params.put("notarizationStatus","2");
            }else if(dealType.equals("2")){
                params.put("notarizationStatus","3");
            }else{
                result.put("status",false);
                result.put("message","dealType类型不支持，请传入“0”，“1”或“2”！");
                return result;
            }

            Map<String,Object> rs= eviSelect(params,autmanMapper);


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
     * 公证员注册
     * @param req
     * @return 公证员注册成功返回公钥
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/notar/regist")
    public Object registerNotar(HttpServletRequest req) {

        Map<String, Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("notaryName")){
                result.put("status",false);
                result.put("message","没有给出notaryName");
                return result;
            }
            if(params.get("notaryName").toString().equals("none")){
                result.put("status",false);
                result.put("message","notaryName不能为空");
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

            if(!params.containsKey("notarizationType")){
                result.put("status",false);
                result.put("message","没有给出notarizationType");
                return result;
            }
            if(params.get("notarizationType").toString().equals("none")){
                result.put("status",false);
                result.put("message","notarizationType不能为空");
                return result;
            }

            if(!params.containsKey("workYear")){
                result.put("status",false);
                result.put("message","没有给出workYear");
                return result;
            }
            if(params.get("workYear").toString().equals("none")){
                result.put("status",false);
                result.put("message","workYear不能为空");
                return result;
            }

            if(!params.containsKey("position")){
                result.put("status",false);
                result.put("message","没有给出position");
                return result;
            }
            if(params.get("position").toString().equals("none")){
                result.put("status",false);
                result.put("message","position不能为空");
                return result;
            }


            // 获取参数
            String notaryName = params.get("notaryName").toString();
            String password = params.get("password").toString();
            String phoneNumber = params.get("phoneNumber").toString();
            String idCard = params.get("idCard").toString();
            String email = params.get("email").toString();
            String organizationId = params.get("organizationId").toString();
            String jobNumber = params.get("jobNumber").toString();
            String workYear = params.get("workYear").toString();
            String position = params.get("position").toString();

            // 处理sex枚举类型数据
            String sex = params.get("sex").toString();
//            Integer s = Integer.parseInt(params.get("sex").toString());
//            Sex sex = Sex.getByValue(s);
//            System.out.println("性别为：" + sex.getKey());
//            System.out.println(sex.getValue());

            // 处理notarizationType枚举类型数据
            String notarizationType = params.get("notarizationType").toString();
//            Integer n = Integer.parseInt(params.get("notarizationType").toString());
//            NotarizationType notarizationType = NotarizationType.getByValue(n);
//            System.out.println("公证类型为：" + notarizationType.getKey());
//            System.out.println(notarizationType.getValue());

            // 将密码用sha-256加密
            password = Sha256.SHA(password);
            System.out.println("SHA-256后密码为：" + password);

            // 生成notaryId
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String notaryId = id.replace("-", "");
            System.out.println("userId为：" + notaryId);

            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 数据库public_key字段长度要设置大一点,不然存不下
            String publicKey = pk.toString();
            // System.out.println("公钥为：" + pk);
            System.out.println("公钥为：" + publicKey);

            // 存入数据库
            int flag = notaryService.insertNotary(notaryId, notaryName, jobNumber, password, phoneNumber, idCard, email, sex, organizationId, notarizationType, publicKey, workYear, position);

            if (flag == 1) {
                result.put("status", true);
                result.put("message", "注册成功!");
                // 注册成功返回公钥
                result.put("publicKey", publicKey);
            } else {
                result.put("status", false);
                result.put("message", "注册失败!");
            }


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
     * 公证员登录
     * @param req
     * @return 登陆成功返回notaryId
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/notar/login")
    public Object loginNotar(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("notaryName")){
                result.put("status",false);
                result.put("message","没有给出notaryName");
                return result;
            }
            if(params.get("notaryName").toString().equals("none")){
                result.put("status",false);
                result.put("message","notaryName不能为空");
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
            String notaryName = params.get("notaryName").toString();
            String password = params.get("password").toString();

            // 将密码用sha-256加密
            password = Sha256.SHA(password);
            System.out.println("SHA-256后密码为：" + password);

            // 判断用户名和密码对是否存在数据库中
            NotaryEntity u1 = null;
            u1 = notaryService.selectByNameAndPwd(notaryName,password);

            if(u1 != null){
                result.put("status",true);
                result.put("message","登录成功!");
                // 登录成功返回NotaryId
                result.put("notaryId",u1.getNotaryId());
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
     * 公证员完善信息
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/notar/update")
    public Object updateNotar(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("notaryId")){
                result.put("status",false);
                result.put("message","没有给出notaryId");
                return result;
            }
            if(params.get("notaryId").toString().equals("none")){
                result.put("status",false);
                result.put("message","notaryId不能为空");
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

            if(!params.containsKey("notarizationType")){
                result.put("status",false);
                result.put("message","没有给出notarizationType");
                return result;
            }
            if(params.get("notarizationType").toString().equals("none")){
                result.put("status",false);
                result.put("message","notarizationType不能为空");
                return result;
            }

            if(!params.containsKey("workYear")){
                result.put("status",false);
                result.put("message","没有给出workYear");
                return result;
            }
            if(params.get("workYear").toString().equals("none")){
                result.put("status",false);
                result.put("message","workYear不能为空");
                return result;
            }

            if(!params.containsKey("position")){
                result.put("status",false);
                result.put("message","没有给出position");
                return result;
            }
            if(params.get("position").toString().equals("none")){
                result.put("status",false);
                result.put("message","position不能为空");
                return result;
            }


            // 获取参数
            String notaryId = params.get("notaryId").toString();
            String newPassword = params.get("newPassword").toString();
            String phoneNumber = params.get("phoneNumber").toString();
            String idCard = params.get("idCard").toString();
            String email = params.get("email").toString();
            String sex = params.get("sex").toString();
            String organizationId = params.get("organizationId").toString();
            String notarizationType = params.get("notarizationType").toString();
            String workYear = params.get("workYear").toString();
            String position = params.get("position").toString();


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
            int flag = notaryService.updateNotary(notaryId,newPassword,phoneNumber,idCard,email,sex,organizationId,notarizationType,workYear,position);


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
     * 公证审核
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/notar/audit")
    public Object auditNotar(HttpServletRequest req){
        Map<String,Object> result = new HashMap<>();

        try {
            JSONObject params = ParseRequest.parse(req);

            if(!params.containsKey("evidenceId")){
                result.put("status",false);
                result.put("message","evidence_id不能为空");
                return result;
            }
            if(!params.containsKey("notaryId")){
                result.put("status",false);
                result.put("message","notaryId不能为空");
                return result;
            }
            if(!params.containsKey("acceptFlag")){
                result.put("status",false);
                result.put("message","acceptFlag不能为空");
                return result;
            }
            if(!params.containsKey("notarizationInformation")){
                result.put("status",false);
                result.put("message","notarizationInformation不能为空");
                return result;
            }

            // 获取参数
            String evidenceId = params.get("evidenceId").toString();
            String notaryId = params.get("notaryId").toString();
            String acceptFlag = params.get("acceptFlag").toString();
            String notarizationInformation = params.get("notarizationInformation").toString();

            // 将公证状态写入数据库
            String notarizationStatus=null;

            if(acceptFlag.equals("1"))
                notarizationStatus="3";

            else if(acceptFlag.equals("0"))
                notarizationStatus="4";

            evidenceService.updateNotarStatus(notarizationStatus,evidenceId);

            //公证审核信息
            evidenceService.updateNotarInfo(notarizationInformation,evidenceId);

            //公证完成时间
            Date time = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String notarizationEndTime = sdf.format(time);
            evidenceService.updateNotarEndTime(notarizationEndTime,evidenceId);

            //将结果上传区块链
            Map<String,Object> request=new HashMap<>();

            JSONObject value=new JSONObject();
            value.put("evidenceId",evidenceId);
            value.put("notaryId",notaryId);
            value.put("type","NotarizationAudit");
            value.put("notarizationStatus",notarizationStatus);
            value.put("notarizationInformation",notarizationInformation);

            request.put("key",evidenceId);
            request.put("value",value);

            String str= HttpUtils.doPost(blockchain_url+"writeNotarizationAudit",request);
            System.out.println(str);

            result.put("message","公证审核完成");
            result.put("status",true);

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
     * 预约公证，审核公证申请
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/notar/appoint")
    public Object appointNotar(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("evidenceId")){
                result.put("status",false);
                result.put("message","没有给出evidenceId");
                return result;
            }
            if(params.get("evidenceId").toString().equals("none")){
                result.put("status",false);
                result.put("message","evidenceId不能为空");
                return result;
            }

            if(!params.containsKey("notaryId")){
                result.put("status",false);
                result.put("message","没有给出notaryId");
                return result;
            }
            if(params.get("notaryId").toString().equals("none")){
                result.put("status",false);
                result.put("message","notaryId不能为空");
                return result;
            }

            // 获取参数
            String evidenceId = params.get("evidenceId").toString();
            String notaryId = params.get("notaryId").toString();

            // 分配公证员
            int flag = evidenceService.updateNotaryId(notaryId,evidenceId);

            // 修改公证状态为公证审核中2
            int flag1 = evidenceService.updateNotarStatus("2",evidenceId);

            if(flag == 1 && flag1 == 1){
                result.put("status",true);
                result.put("message","预约成功!");
            }
            else{
                result.put("status",false);
                result.put("message","预约失败!");
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




}




