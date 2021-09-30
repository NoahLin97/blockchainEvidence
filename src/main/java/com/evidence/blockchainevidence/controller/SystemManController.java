package com.evidence.blockchainevidence.controller;


import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.entity.ManagerEntity;
import com.evidence.blockchainevidence.mapper.SystemManMapper;
import com.evidence.blockchainevidence.subprotocols.K2C8;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
public class SystemManController {

    @Autowired
    SystemManMapper systemManMapper;


    /**
     * 系统管理员登录
     * @param req
     * @return 登录成功返回manId
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/sys/login")
    public Object loginSys(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("username")){
                result.put("status",false);
                result.put("message","没有给出username");
                return result;
            }
            if(params.get("username").toString().equals("none")){
                result.put("status",false);
                result.put("message","username不能为空");
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
            String username = params.get("username").toString();
            String password = params.get("password").toString();

            // 将密码用sha-256加密
            password = Sha256.SHA(password);
            System.out.println("SHA-256后密码为：" + password);

            // 判断用户名和密码对是否存在数据库中
            ManagerEntity u1 = null;
            u1 = systemManMapper.selectByNameAndPwd(username,password);

            if(u1 != null){
                result.put("status",true);
                result.put("message","登录成功!");
                // 登录成功返回manId
                result.put("manId",u1.getManId());
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
     * 系统理员注册
     * @param req
     * @return 注册成功返回manId
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/sys/regist")
    public Object registerSys(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{
            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("username")){
                result.put("status",false);
                result.put("message","没有给出username");
                return result;
            }
            if(params.get("username").toString().equals("none")){
                result.put("status",false);
                result.put("message","username不能为空");
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



            // 获取参数
            String username = params.get("username").toString();
            String password = params.get("password").toString();
            String phoneNumber = params.get("phoneNumber").toString();
            String idCard = params.get("idCard").toString();
            String email = params.get("email").toString();


            // 处理sex枚举类型数据
            String sex = params.get("sex").toString();
//            Integer s = Integer.parseInt(params.get("sex").toString());
//            Sex sex =Sex.getByValue(s);
//            System.out.println("性别为：" + sex.getKey());
//            System.out.println(sex.getValue());

            // 将密码用sha-256加密
            password = Sha256.SHA(password);
            System.out.println("SHA-256后密码为：" + password);

            // 生成manId
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String manId = id.replace("-" , "");
            System.out.println("manId为：" + manId);


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
            int flag = systemManMapper.insertSystemMan(manId,username,password,phoneNumber,idCard,email,sex);

            if(flag == 1){
                result.put("status",true);
                result.put("message","注册成功!");
                // 注册成功返回manId
                result.put("manId",manId);
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
     * 系统理员信息完善
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/sys/update")
    public Object updateSys(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("manId")){
                result.put("status",false);
                result.put("message","没有给出manId");
                return result;
            }
            if(params.get("manId").toString().equals("none")){
                result.put("status",false);
                result.put("message","manId不能为空");
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

            // 获取参数
            String manId = params.get("manId").toString();
            String newPassword = params.get("newPassword").toString();
            String phoneNumber = params.get("phoneNumber").toString();
            String idCard = params.get("idCard").toString();
            String email = params.get("email").toString();
            String sex = params.get("sex").toString();

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
            int flag = systemManMapper.updateSystemMan(manId,newPassword,phoneNumber,idCard,email,sex);

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
     * 系统理员信息查询
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/sys/query")
    public Object querySys(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("manId")){
                result.put("status",false);
                result.put("message","没有给出manId");
                return result;
            }
            if(params.get("manId").toString().equals("none")){
                result.put("status",false);
                result.put("message","manId不能为空");
                return result;
            }


            // 获取参数
            String manId = params.get("manId").toString();
            ManagerEntity manager = systemManMapper.selectByManId(manId);

            // 生成公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            CipherPub idCard_cipher = new CipherPub(manager.getIdCard());

            // 身份证解密测试
            BigInteger idCard = paillier.SDecryption(idCard_cipher);
            System.out.println("解密后的身份证号为：" + idCard);

            manager.setIdCard(idCard.toString());

            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",manager);


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
