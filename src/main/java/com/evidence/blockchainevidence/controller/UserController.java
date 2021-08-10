package com.evidence.blockchainevidence.controller;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.entity.EvidenceEntity;
import com.evidence.blockchainevidence.entity.NotaryEntity;
import com.evidence.blockchainevidence.entity.TransactionEntity;
import com.evidence.blockchainevidence.entity.UserEntity;
import com.evidence.blockchainevidence.helib.SEA;
import com.evidence.blockchainevidence.helib.SLT;
import com.evidence.blockchainevidence.mapper.AutmanMapper;
import com.evidence.blockchainevidence.mapper.NotaryMapper;
import com.evidence.blockchainevidence.mapper.UserMapper;
import com.evidence.blockchainevidence.service.EvidenceService;
import com.evidence.blockchainevidence.service.UserService;
import com.evidence.blockchainevidence.subprotocols.K2C16;
import com.evidence.blockchainevidence.subprotocols.K2C8;
import com.evidence.blockchainevidence.subprotocols.KMP;
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
import java.util.*;

import static com.evidence.blockchainevidence.controller.AutmanController.*;


@RestController
public class UserController {

    @Autowired(required = false)
    UserMapper userMapper;
    @Autowired(required = false)
    NotaryMapper notaryMapper;
    @Autowired
    UserService userService;
    @Autowired(required = false)
    AutmanMapper autmanMapper;
    @Autowired
    EvidenceService evidenceService;

    /**
     * 数据库查询测试
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/notarQue")
    public Object notarWait (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            //获取参数
            Integer decryptflag=Integer.parseInt(params.get("decryptflag").toString());
            Integer notaryid=Integer.parseInt(params.get("notaryid").toString());
//            System.out.println(decryptflag);
//            System.out.println(notaryid);

            //查询数据库
            List<NotaryEntity> notaries = notaryMapper.findAll();


            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",notaries);

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
     * 数值加密测试,测试qw<kw
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/numPallierQue")
    public Object numPallierQue (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            //获取参数
            Integer qw=Integer.parseInt(params.get("qw").toString());
            Integer kw=Integer.parseInt(params.get("kw").toString());
            System.out.println(qw);
            System.out.println(kw);

            //Paillier初始化,我这里为了确保参数一致，把它放到类里面了，其实应该保存在加密文件里的
            PaillierT paillier = new PaillierT(PaillierT.param);


            //为新用户生成公私钥
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);



            //加密数字,得到string形式的密文，这个是用来存数据库的
            String sqw=paillier.Encryption(BigInteger.valueOf(qw),pk).toString();
            String skw=paillier.Encryption(BigInteger.valueOf(kw),pk).toString();

            //从数据库读取密文后，转成CipherPub进行同态计算
            CipherPub cqw= new CipherPub(sqw);
            CipherPub ckw= new CipherPub(skw);

            //判断qw1<kw<qw2是否成立
            SLT SK1 = new SLT(cqw,ckw,paillier);

            SK1.StepOne();
            SK1.StepTwo();
            SK1.StepThree();
            CipherPub cans=SK1.FIN;

            //解密数字
            BigInteger ans=paillier.SDecryption(cans);

            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",ans);

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
     * 字符加密测试,测试qw是否与kw匹配
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/strPallierQue")
    public Object strPallierQue (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            //获取参数
            String qw=params.get("qw").toString();
            String kw=params.get("kw").toString();
            System.out.println(qw);
            System.out.println(kw);

            //Paillier初始化,我这里为了确保参数一致，把它放到类里面了，其实应该保存在加密文件里的
            PaillierT paillier = new PaillierT(PaillierT.param);


            //为新用户生成公私钥
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);



            //加密字符串,得到string形式的密文，这个是用来存数据库的
            K2C8 SK0 = new K2C8(kw, pk, paillier);
            SK0.StepOne();
            String skw=SK0.FIN.toString();

            //从数据库读取密文后，转成CipherPub进行同态计算
            CipherPub ckw= new CipherPub(skw);

            //判断qw与kw是否匹配
            SEA SK1 = new SEA(qw, paillier);
            SK1.StepOne();
            KMP SK2 = new KMP(ckw, SK1.Y1, SK1.Y2, SK1.Y3, SK1.num1, SK1.num2,
                    SK1.numchar1, SK1.numchar2, SK1.flag, paillier);
            SK2.StepOne();
            CipherPub cans=SK2.FIN;


            //解密匹配结果
            BigInteger ans=paillier.SDecryption(cans);

            //解密kw的密文ckw
            String kw2=K2C16.parseString(paillier.SDecryption(ckw),paillier);
            System.out.println("解密结果为："+kw2);


            //填充返回值
            result.put("status",true);
            result.put("message","success");
            result.put("data",ans);

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
    @PostMapping("/user/notarRecord")
    public Object notarRecord (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);

            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","给出没有用户Id！");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","用户Id不能为空！");
                return result;
            }
            if(params.containsKey("usernameWildcard ")){
                params.remove("usernameWildcard ");
            }
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
    @PostMapping("/user/transQuery")
    public Object transQuery (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","给出没有用户Id！");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","用户Id不能为空！");
                return result;
            }
            if(params.containsKey("usernameWildcard ")){
                params.remove("usernameWildcard ");
            }
            if(params.containsKey("transactionPeopleCipher    ")){
                params.remove("transactionPeopleCipher ");
            }
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
     * 查询证据记录
     */
    @CrossOrigin(origins ="*")
    @PostMapping("/user/evidenceQuery")
    public Object evidenceQuery  (HttpServletRequest req){
        Map<String,Object> result=new HashMap<>();

        try{
            JSONObject params= ParseRequest.parse(req);
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","给出没有用户Id！");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","用户Id不能为空！");
                return result;
            }
            if(params.containsKey("usernameWildcard ")){
                params.remove("usernameWildcard ");
            }
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
     * 用户注册
     * @param req
     * @return 注册成功返回用户公钥
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/regist")
    public Object registerUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{
            JSONObject params = ParseRequest.parse(req);

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

            // 生成userId
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String userId = id.replace("-" , "");
            System.out.println("userId为：" + userId);

            // 假定初始存储空间为100,已用为0
            int storageSpace = 100;
            int hasUsedStorage = 0;

            // 初始余额为300
            int remains = 300;

            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 数据库public_key字段长度要设置大一点,不然存不下
            String publicKey = pk.toString();
            // System.out.println("公钥为：" + pk);
            System.out.println("公钥为：" + publicKey);

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


            // 存储空间加密
            System.out.println("加密前：" + storageSpace);
            String sstorageSpace = paillier.Encryption(BigInteger.valueOf(storageSpace),pk).toString();
            System.out.println("加密后：" + sstorageSpace);

            // 存储空间解密测试
            BigInteger mstorageSpace = paillier.SDecryption(new CipherPub(sstorageSpace));
            System.out.println("解密后：" + mstorageSpace);

            // 剩余空间加密
            String sremains = paillier.Encryption(BigInteger.valueOf(remains),pk).toString();

            // 已使用空间加密
            String shasUsedStorage = paillier.Encryption(BigInteger.valueOf(hasUsedStorage),pk).toString();

            // 存入数据库
            int flag = userService.insertUser(userId,username,password,phoneNumber,idCard,email,sex,sremains,sstorageSpace,shasUsedStorage,publicKey);

            if(flag == 1){
                result.put("status",true);
                result.put("message","注册成功!");
                // 注册成功返回公钥
                result.put("publicKey",publicKey);
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
     * 用户登录
     * @param req
     * @return 登录成功返回userId
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/login")
    public Object loginUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 获取参数
            String username = params.get("username").toString();
            String password = params.get("password").toString();

            // 将密码用sha-256加密
            password = Sha256.SHA(password);
            System.out.println("SHA-256后密码为：" + password);

            // 判断用户名和密码对是否存在数据库中
            UserEntity u1 = null;
            u1 = userService.selectByNameAndPwd(username,password);

            if(u1 != null){
                result.put("status",true);
                result.put("message","登录成功!");
                // 登录成功返回userid
                result.put("userId",u1.getUserId());
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
     * 用户信息完善
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/update")
    public Object updateUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{

            JSONObject params = ParseRequest.parse(req);

            // 获取参数
            String userId = params.get("userId").toString();
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
            int flag = userService.updateUser(userId,newPassword,phoneNumber,idCard,email,sex);

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
     * 公证申请，为某个存证申请公证，生成待交易请求
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/notarReq")
    public Object requestNotarization(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 获取参数
            String userId = params.get("userId").toString();
            String evidenceId = params.get("evidenceId").toString();
            String organizationId = params.get("organizationId").toString();
            String notarizationType = params.get("notarizationType").toString();
            String notarizationMatters = params.get("notarizationMatters").toString();

            // 把接受到的organizationId和notarizationType写入数据库
            int flag = evidenceService.updateOrganIdAndNotarType(organizationId,notarizationType,evidenceId);

            // 把notarizationMatters写入数据库





            // 修改公证状态为等待公证1
            int flag1 = evidenceService.updateNotarStatus("1",evidenceId);

            // 生成公证申请时间，并写入数据库
            Date notarizationStartTime = new Date();




            // 生成transactionID，写入数据库（两张表）
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String transactionID = id.replace("-" , "");
            System.out.println("transactionID为：" + transactionID);


            // 修改支付状态为未支付（两张表），在transaction表增加交易状态



            // 通过evidenceId找到数据库中的那一行
            EvidenceEntity evi = null;
            evi = evidenceService.selectByEvidenceId(evidenceId);
//            System.out.println(evi.getFileHash());


            // 返回
            result.put("status",true);
            result.put("message","申请成功");
//            result.put("organizationId",organizationId);
//            result.put("notarizationType",notarizationType);
//            result.put("notarizationMatters",notarizationMatters);
            result.put("notarizationStatus",evi.getNotarizationStatus());
            result.put("transactionID",evi.getTransactionId());
            result.put("transactionStatus",evi.getTransactionStatus());
            result.put("notarizationStartTime",notarizationStartTime);



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
     * 公证缴费，为某次公证申请缴费，通过公证申请生成的交易id处理
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/notarPay")
    public Object payNotarization(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 获取参数
            String userId = params.get("userId").toString();
            String evidenceId = params.get("evidenceId").toString();
            String transactionPeople = params.get("transactionPeople").toString();
            String notarizationMoney = params.get("notarizationMoney").toString();


            // 通过evidenceId找到数据库中的那一行
            EvidenceEntity evi = null;
            evi = evidenceService.selectByEvidenceId(evidenceId);
//            System.out.println(evi.getFileHash());

            // 通过证据表获得交易id
            String transactionId = evi.getTransactionId();


            // 通过transactionId找到数据库中的那一行
            TransactionEntity tran =null;
//            tran =

            // 通过userId找到数据库中的那一行
            UserEntity u1 = null;
//            u1 = userService

            // 获取用户余额，并写入交易表中
            String userRemains = u1.getRemains();


            // 修改支付交易类型为申请司法公证4，并写入数据库


            // 生成支付交易时间，并写入数据库


            // 通过evidenceId找到数据库中的那一行
            EvidenceEntity evi1 = null;
            evi1 = evidenceService.selectByEvidenceId(evidenceId);
//            System.out.println(evi.getFileHash());

            // 通过transactionId找到数据库中的那一行
            TransactionEntity tran1 =null;
//            tran =


            // 与区块链交互
            Map<String,Object> blockchain = new HashMap<>();
            JSONObject jsonObject = new JSONObject();

            // evidence表
            jsonObject.put("evidenceId",evidenceId);
            jsonObject.put("evidenceType",evi1.getEvidenceType());
            jsonObject.put("evidenceName",evi1.getEvidenceName());
            jsonObject.put("filePath",evi1.getFilePath());
            jsonObject.put("fileSize",evi1.getFileSize());
            jsonObject.put("fileHash",evi1.getFileHash());
            jsonObject.put("organizationId",evi1.getOrganizationId());
            jsonObject.put("notaryId",evi1.getNotaryId());
            jsonObject.put("notarizationStatus",evi1.getNotarizationStatus());
            jsonObject.put("notarizationStartTime",evi1.getNotarizationStartTime());
            jsonObject.put("notarizationMoney",evi1.getNotarizationMoney());
            jsonObject.put("notarizationType",evi1.getNotarizationType());
            jsonObject.put("notarizationMatters",evi1.getNotarizationMatters());

            // transaction表
            jsonObject.put("transactionId",transactionId);
            jsonObject.put("userRemains",tran1.getUserRemains());
            jsonObject.put("transactionMoney",tran1.getTransactionMoney());
            jsonObject.put("transactionPeople",tran1.getTransactionPeople());
            jsonObject.put("transactionType",tran1.getTransactionType());
            jsonObject.put("transactionTime",tran1.getTransactionTime());
            jsonObject.put("transactionStatus",tran1.getTransactionStatus());


            blockchain.put("key",evidenceId);
            blockchain.put("value",jsonObject);

            String str= HttpUtils.doPost("http://192.168.31.218:8090/writenotarizationapply",blockchain);
            System.out.println("区块链Id为：" + str);




            // 返回
            result.put("status",true);
            result.put("message","缴费成功");
            result.put("transactionId",transactionId);
            result.put("userRemains",tran1.getUserRemains());
            result.put("transactionMoney",tran1.getTransactionMoney());
            result.put("transactionPeople",tran1.getTransactionPeople());
            result.put("transactionType",tran1.getTransactionType());
            result.put("transactionTime",tran1.getTransactionTime());
            result.put("transactionStatus",tran1.getTransactionStatus());


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




