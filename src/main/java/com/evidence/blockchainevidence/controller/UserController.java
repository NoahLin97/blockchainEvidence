package com.evidence.blockchainevidence.controller;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.entity.*;
import com.evidence.blockchainevidence.helib.SEA;
import com.evidence.blockchainevidence.helib.SLT;
import com.evidence.blockchainevidence.mapper.*;
import com.evidence.blockchainevidence.service.EvidenceService;
import com.evidence.blockchainevidence.service.OrganizationService;
import com.evidence.blockchainevidence.service.TransactionService;
import com.evidence.blockchainevidence.service.UserService;
import com.evidence.blockchainevidence.subprotocols.K2C16;
import com.evidence.blockchainevidence.subprotocols.K2C8;
import com.evidence.blockchainevidence.subprotocols.KMP;
import com.evidence.blockchainevidence.utils.HttpUtils;
import com.evidence.blockchainevidence.utils.ParseRequest;
import com.evidence.blockchainevidence.utils.Sha256;
import com.evidence.blockchainevidence.utils.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
    @Autowired
    OrganizationService organizationService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    NotarizationTypeMapper notarizationTypeMapper;

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
     * 获取区块链交易id和上链时间
     */
    @PostMapping(path = "/getInfo")
    public Object getInfo(HttpServletRequest request) throws IOException, InterruptedException {
        BufferedReader br = request.getReader();
        String str, wholeStr = "";
        while((str = br.readLine()) != null){
            wholeStr += str;
        }
        JSONObject json=JSONObject.parseObject(wholeStr);
        String txID = json.getString("txID");
        String type = json.getString("type");
        String t = json.getString("timestamp");

        System.out.println("timestamp:" + t);

        Date time = new Date(t);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String Time = sdf.format(time);

        System.out.println("Time:" + Time);

        System.out.println("txID:" + txID);
        System.out.println("type:" + type);

        JSONObject value = json.getJSONObject("value");


        if (type.equals("evidence"))
        {
            String evidenceId = json.getString("key");
            //存证区块链交易id
            evidenceService.updateEvidenceBlockchainId(txID,evidenceId);
            //上链时间
            evidenceService.updateBlockchainTime(Time,evidenceId);
        }
        else if(type.equals("notarizationApply"))
        {
            String evidenceId = json.getString("key");
            //公证申请区块链交易id
            evidenceService.updateNotarBlockchainIdStart(txID,evidenceId);
        }
        else if(type.equals("notarizationAudit"))
        {
            String evidenceId = json.getString("key");
            //公证完成区块链交易id
            evidenceService.updateNotarBlockchainIdEnd(txID,evidenceId);
        }
        else if(type.equals("transaction"))
        {
            String transactionId = json.getString("key");
            System.out.println("transactionId:" + transactionId);
            //上链时间
            transactionService.updateBlockchainTime(Time,transactionId);
            //支付交易区块链交易id
            transactionService.updateTranBlockchainId(txID,transactionId);
        }
        return true;
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
     * 生成公证机构
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/organizationGen")
    public Object genOrganization(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try{

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("organizationName")){
                result.put("status",false);
                result.put("message","没有给出organizationName");
                return result;
            }
            if(params.get("organizationName").toString().equals("none")){
                result.put("status",false);
                result.put("message","organizationName不能为空");
                return result;
            }

            if(!params.containsKey("address")){
                result.put("status",false);
                result.put("message","没有给出address");
                return result;
            }
            if(params.get("address").toString().equals("none")){
                result.put("status",false);
                result.put("message","address不能为空");
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

            if(!params.containsKey("legalPeople")){
                result.put("status",false);
                result.put("message","没有给出legalPeople");
                return result;
            }
            if(params.get("legalPeople").toString().equals("none")){
                result.put("status",false);
                result.put("message","legalPeople不能为空");
                return result;
            }

            // 获取参数
            String organizationName = params.get("organizationName").toString();
            String address = params.get("address").toString();
            String phoneNumber = params.get("phoneNumber").toString();
            String email = params.get("email").toString();
            String legalPeople = params.get("legalPeople").toString();

            // 生成organizationId
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String organizationId = id.replace("-" , "");
            System.out.println("userId为：" + organizationId);

            // 存入数据库
            int flag = organizationService.insertOrganization(organizationId,organizationName,address,phoneNumber,email,legalPeople);

            if(flag == 1){
                result.put("status",true);
                result.put("message","注册成功!");

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

            // 生成userId
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String userId = id.replace("-" , "");
            System.out.println("userId为：" + userId);

            // 假定初始存储空间为100,已用为0
            int storageSpace = 100;
            int hasUsedStorage = 0;

            // 初始余额为500
            int remains = 500;

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
            System.out.println("存储空间加密前：" + storageSpace);
            String sstorageSpace = paillier.Encryption(BigInteger.valueOf(storageSpace),pk).toString();
            System.out.println("存储空间加密后：" + sstorageSpace);

            // 存储空间解密测试
            BigInteger mstorageSpace = paillier.SDecryption(new CipherPub(sstorageSpace));
            System.out.println("存储空间解密后：" + mstorageSpace);

            // 余额加密
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

            // 判断前端传来的参数是否正确
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","没有给出userId");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","userId不能为空");
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

            // 判断前端传来的参数是否正确
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","没有给出userId");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","userId不能为空");
                return result;
            }

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

            if(!params.containsKey("notarizationMatters")){
                result.put("status",false);
                result.put("message","没有给出notarizationMatters");
                return result;
            }
            if(params.get("notarizationMatters").toString().equals("none")){
                result.put("status",false);
                result.put("message","notarizationMatters不能为空");
                return result;
            }

            // 获取参数
            String userId = params.get("userId").toString();
            String evidenceId = params.get("evidenceId").toString();
            String organizationId = params.get("organizationId").toString();
            String notarizationType = params.get("notarizationType").toString();
            String notarizationMatters = params.get("notarizationMatters").toString();

            // 从数据库查找公证金额
            NotarizationTypeEntity n1 = notarizationTypeMapper.selectNotarizationType(notarizationType);

            // 把接受到的organizationId和notarizationType写入数据库
            int flag = evidenceService.updateOrganIdAndNotarType(organizationId,n1.getNotarizationType(),evidenceId);

            // 把notarizationMatters写入数据库
            int flag1 = evidenceService.updateNotarMatters(notarizationMatters,evidenceId);

            // 修改公证状态为等待公证1,并写入数据库
            int flag2 = evidenceService.updateNotarStatus("1",evidenceId);

            // 生成公证申请时间，并写入数据库
            Date time = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String notarizationStartTime = sdf.format(time);
            int flag3 = evidenceService.updateNotarStartTime(notarizationStartTime,evidenceId);

            // 获取用户余额，写入数据库
            UserEntity u1 = null;
            u1 = userService.selectByUserId(userId);


            // 对要存入数据库的数据加密
            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 获取交易金额：查globalparams，根据公证类型获取对应的公证金额
//            Integer temp = Integer.parseInt(notarizationType);
//            String transactionMoney = notarizationMoneys[temp].toString();
            BigInteger btransactionMoney = paillier.SDecryption(new CipherPub(n1.getNotarizationMoney()));
            String transactionMoney = btransactionMoney.toString();

//            // 余额加密
//            System.out.println("余额加密前：" + u1.getRemains());
//            String suserRemains = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(u1.getRemains())),pk).toString();
//            System.out.println("余额加密后：" + suserRemains);
//
//            // 余额解密测试
//            BigInteger muserRemains = paillier.SDecryption(new CipherPub(suserRemains));
//            System.out.println("余额解密后：" + muserRemains);

            // 交易金额加密
            String notarizationMoney = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(transactionMoney)),pk).toString();

            // 生成transactionId，写入数据库（两张表）
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String transactionId = id.replace("-" , "");
            System.out.println("transactionId为：" + transactionId);
            // 写入transaction表
            int flag4 = transactionService.insertNotarTran(transactionId,userId,u1.getRemains(),notarizationMoney,"4");
            // 写入evidence表
            int flag5 = evidenceService.updateTranId(transactionId,evidenceId);

            // 交易金额写入evidence表
            int flag6 = evidenceService.updateNotarMoney(notarizationMoney,evidenceId);


            // 修改支付状态为未支付0（两张表），在transaction表增加交易状态
            int flag7 = evidenceService.updateTranStatus("0",evidenceId);
            int flag8 = transactionService.updateTranStatus("0",transactionId);

            // 通过evidenceId找到数据库中的那一行
            EvidenceEntity evi = null;
            evi = evidenceService.selectByEvidenceId(evidenceId);
//            System.out.println(evi.getFileHash());


            // 返回
            result.put("status",true);
            result.put("message","申请公证成功");
//            result.put("organizationId",organizationId);
//            result.put("notarizationType",notarizationType);
//            result.put("notarizationMatters",notarizationMatters);
            result.put("notarizationStatus",evi.getNotarizationStatus());
            result.put("transactionId",evi.getTransactionId());
            result.put("transactionStatus",evi.getTransactionStatus());
            result.put("notarizationStartTime",evi.getNotarizationStartTime());
            result.put("notarizationMoney",transactionMoney);


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

            // 判断前端传来的参数是否正确
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","没有给出userId");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","userId不能为空");
                return result;
            }

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

            if(!params.containsKey("transactionPeople")){
                result.put("status",false);
                result.put("message","没有给出transactionPeople");
                return result;
            }
            if(params.get("transactionPeople").toString().equals("none")){
                result.put("status",false);
                result.put("message","transactionPeople不能为空");
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
            tran = transactionService.selectByTransactionId(transactionId);

            // 通过userId找到数据库中的那一行
            UserEntity u1 = null;
            u1 = userService.selectByUserId(userId);

//            // 获取用户余额，并写入交易表中
//            String userRemains = u1.getRemains();
//            int flag = transactionService.updateUserRemains(userRemains,transactionId);

            // 解密transaction表中的userRemains和notarizationMoney，判断是否可以缴费成功
            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 解密userRemains和notarizationMoney
            BigInteger muserRemains = paillier.SDecryption(new CipherPub(u1.getRemains()));
            System.out.println("余额解密后：" + muserRemains);

            BigInteger mnotarizationMoney = paillier.SDecryption(new CipherPub(tran.getTransactionMoney()));
            System.out.println("交易金额解密后：" + mnotarizationMoney);

            if(muserRemains.intValue() - mnotarizationMoney.intValue() < 0){
                result.put("status",false);
                result.put("message","余额不足，无法进行公证缴费");

                return result;
            }
            else {

                // 更新userId的余额，减去公证金额，并写入数据库
                int i = muserRemains.intValue() - mnotarizationMoney.intValue();
                System.out.println("user更新后余额为：" + i);
                String si =  paillier.Encryption(BigInteger.valueOf(i),pk).toString();
                int flag = userService.updateRemains(si,userId);

                // 更新transactionPeople的余额，加上公证金额，并写入数据库
                // 获取transactionPeople那一行

                // transactionPeople传进来的是organizationId，通过organizationId找到legalPeople，向legalPeople汇款
                OrganizationEntity o1 = organizationService.selectByOrganizationId(transactionPeople);
                UserEntity u2 = null;
                u2 = userService.selectByUserId(o1.getLegalPeople());


                // 解密余额
                System.out.println(u2.getRemains());
                BigInteger m = paillier.SDecryption(new CipherPub(u2.getRemains()));
                System.out.println("transactionPeople余额解密后：" + m);

                int j = m.intValue() + mnotarizationMoney.intValue();
                System.out.println("transactionPeople更新后余额为：" + j);
                String sj = paillier.Encryption(BigInteger.valueOf(j),pk).toString();
                int flag1 = userService.updateRemains(sj,transactionPeople);

                // 修改支付交易类型为申请司法公证4，并写入数据库
                // 不需要了

                // 生成支付交易时间，并写入数据库
                Date time = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String transactionTime = sdf.format(time);
                int flag2 = transactionService.updateTranTime(transactionTime,transactionId);

                // 更新两张表的支付交易状态
                int flag3 = evidenceService.updateTranStatus("1",evidenceId);
                int flag4 = transactionService.updateTranStatus("1",transactionId);

                // 还要把transactionPeople写进transaction表中
                int flag5 = transactionService.updateTranPeople(transactionPeople,transactionId);
            }


            // 通过evidenceId找到数据库中的那一行
            EvidenceEntity evi1 = null;
            evi1 = evidenceService.selectByEvidenceId(evidenceId);
//            System.out.println(evi.getFileHash());

            // 通过transactionId找到数据库中的那一行
            TransactionEntity tran1 =null;
            tran1 = transactionService.selectByTransactionId(transactionId);


            // 与区块链交互
            Map<String,Object> blockchain1 = new HashMap<>();
            Map<String,Object> blockchain2 = new HashMap<>();
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


            blockchain1.put("key",evidenceId);
            blockchain1.put("value",jsonObject);

            String str1 = HttpUtils.doPost("http://192.168.31.245:8090/writeNotarizationApply",blockchain1);
            System.out.println("公证申请区块链Id为：" + str1);

            blockchain2.put("key",transactionId);
            System.out.println("transactionId:" + transactionId);
            blockchain2.put("value",jsonObject);
            String str2 = HttpUtils.doPost("http://192.168.31.245:8090/writeNotarPay",blockchain2);
            System.out.println("公证缴费区块链Id为：" + str2);

            // 返回
            result.put("status",true);
            result.put("message","公证缴费成功");
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


    /**
     * 积分商店充值，交易对象为空
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/charge")
    public Object chargeUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","没有给出userId");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","userId不能为空");
                return result;
            }

            if(!params.containsKey("transactionMoney")){
                result.put("status",false);
                result.put("message","没有给出transactionMoney");
                return result;
            }
            if(params.get("transactionMoney").toString().equals("none")){
                result.put("status",false);
                result.put("message","transactionMoney不能为空");
                return result;
            }

            // 获取参数
            String userId = params.get("userId").toString();
            String transactionMoney = params.get("transactionMoney").toString();

            // 生成transactionId，写入transaction表
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String transactionId = id.replace("-" , "");
            System.out.println("transactionId为：" + transactionId);

            // 通过userId找到数据库中的那一行
            UserEntity u1 = null;
            u1 = userService.selectByUserId(userId);

            // 解密用户余额
            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            // 解密userRemains
            BigInteger muserRemains = paillier.SDecryption(new CipherPub(u1.getRemains()));
            System.out.println("余额解密后：" + muserRemains);
            System.out.println("transactionMoney为：" + transactionMoney);

            // 获取用户余额并加上充值金额
//            Integer userRemains =Integer.parseInt(u1.getRemains());
            int i = muserRemains.intValue() + Integer.parseInt(transactionMoney);
            System.out.println("充值后的金额为：" + i);

            // 对充值后的金额和transactionMoney加密
            String si =  paillier.Encryption(BigInteger.valueOf(i),pk).toString();
            String stransactionMoney = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(transactionMoney)),pk).toString();

            // 将充值过后的金额写进user表中
            int flag = userService.updateRemains(si,userId);

            // 设置交易类型为充值0
            String transactionType = "0";

            // 将以上信息统一写入transaction表中
            int flag1 = transactionService.insertNotarTran(transactionId,userId,u1.getRemains(),stransactionMoney,transactionType);

            // 生成支付交易时间
            Date time = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String transactionTime = sdf.format(time);
            int flag2 = transactionService.updateTranTime(transactionTime,transactionId);

            // 判断交易状态，修改为已支付1
            String transactionStatus = "1";
            int flag3 = transactionService.updateTranStatus(transactionStatus,transactionId);


            // 与区块链交互
            Map<String,Object> blockchain = new HashMap<>();
            JSONObject jsonObject = new JSONObject();

            // 通过transactionId找到数据库中的那一行
            TransactionEntity tran1 =null;
            tran1 = transactionService.selectByTransactionId(transactionId);

            // transaction表
            jsonObject.put("transactionId",transactionId);
            jsonObject.put("userRemains",tran1.getUserRemains());
            jsonObject.put("transactionMoney",tran1.getTransactionMoney());
            jsonObject.put("transactionPeople",tran1.getTransactionPeople());
            jsonObject.put("transactionType",tran1.getTransactionType());
            jsonObject.put("transactionTime",tran1.getTransactionTime());
            jsonObject.put("transactionStatus",tran1.getTransactionStatus());


            blockchain.put("key",transactionId);
            blockchain.put("value",jsonObject);

            String str= HttpUtils.doPost("http://192.168.31.245:8090/writeCharge",blockchain);
            System.out.println("充值区块链Id为：" + str);


            // 返回
            result.put("status",true);
            result.put("message","充值成功");
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


    /**
     * 积分商店转赠
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/give")
    public Object giveUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","没有给出userId");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","userId不能为空");
                return result;
            }

            if(!params.containsKey("transactionPeople")){
                result.put("status",false);
                result.put("message","没有给出transactionPeople");
                return result;
            }
            if(params.get("transactionPeople").toString().equals("none")){
                result.put("status",false);
                result.put("message","transactionPeople不能为空");
                return result;
            }

            if(!params.containsKey("transactionMoney")){
                result.put("status",false);
                result.put("message","没有给出transactionMoney");
                return result;
            }
            if(params.get("transactionMoney").toString().equals("none")){
                result.put("status",false);
                result.put("message","transactionMoney不能为空");
                return result;
            }

            // 获取参数
            String userId = params.get("userId").toString();
            String transactionPeople = params.get("transactionPeople").toString();
            String transactionMoney = params.get("transactionMoney").toString();

            // 生成transactionId，写入transaction表
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String transactionId = id.replace("-" , "");
            System.out.println("transactionId为：" + transactionId);

            // 通过userId找到数据库中的那一行
            UserEntity u1 = null;
            u1 = userService.selectByUserId(userId);

            // 通过transactionPeople找到数据库中的那一行
            UserEntity u2 = null;
            u2 = userService.selectByUserId(transactionPeople);

            // 解密user和transactionPeople的余额
            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            BigInteger mu1Remains = paillier.SDecryption(new CipherPub(u1.getRemains()));
            BigInteger mu2Remains = paillier.SDecryption(new CipherPub(u2.getRemains()));
            System.out.println("u1余额解密后：" + mu1Remains);
            System.out.println("u2余额解密后：" + mu2Remains);
            System.out.println("transactionMoney为：" + transactionMoney);

            // 获取用户余额减去转赠金额，判断余额是否充足
//            Integer userRemains =Integer.parseInt(u1.getRemains());
            System.out.println(mu1Remains.intValue());
            System.out.println(Integer.parseInt(transactionMoney));
            if(mu1Remains.intValue() - Integer.parseInt(transactionMoney) < 0){
                result.put("status",false);
                result.put("message","余额不足，无法转赠");

                return result;
            }
            else{
                int i = mu1Remains.intValue() - Integer.parseInt(transactionMoney);
                System.out.println("转赠后u1余额为：" + i);

                // 获取转赠用户的余额，加上转赠的金额
//                Integer u2Remains = Integer.parseInt(u2.getRemains());
//                u2Remains += Integer.parseInt(transactionMoney);
                int j = mu2Remains.intValue() + Integer.parseInt(transactionMoney);
                System.out.println("转赠后u2的余额为：" + j);

                // 分别将两个用户的余额更新至数据库
                String si =  paillier.Encryption(BigInteger.valueOf(i),pk).toString();
                String sj = paillier.Encryption(BigInteger.valueOf(j),pk).toString();
                int flag = userService.updateRemains(si,userId);
                int flag1 = userService.updateRemains(sj,transactionPeople);

                // 加密transactionPeople和transactionMoney
//                String stransactionPeople = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(transactionPeople)),pk).toString();
                // transactionPeople加密
                System.out.println("transactionPeople为：" + transactionPeople);
                K2C8 SK0 = new K2C8(transactionPeople,pk,paillier);
                System.out.println("K2C8转换后的大整数为：" + SK0.getB());
                SK0.StepOne();
                String stransactionPeople = SK0.FIN.toString();
                System.out.println("K2C8加密后的大整数为：" + stransactionPeople);

                // transactionPeople解密测试
                BigInteger mtransactionPeople = paillier.SDecryption(SK0.FIN);
                System.out.println("解密后的大整数值为：" + mtransactionPeople);

                String temp = SK0.parseString(mtransactionPeople,paillier);
                System.out.println("大整数转化为字符串：" + temp);

                String stransactionMoney = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(transactionMoney)),pk).toString();

                // 设置交易类型为转赠1
                String transactionType = "1";

                // 将以上信息统一写入transaction表中
                int flag2 = transactionService.insertNotarTran(transactionId,userId,u1.getRemains(),stransactionMoney,transactionType);

                // 生成支付交易时间
                Date time = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String transactionTime = sdf.format(time);
                int flag3 = transactionService.updateTranTime(transactionTime,transactionId);

                // 判断交易状态，修改为已支付1
                String transactionStatus = "1";
                int flag4 = transactionService.updateTranStatus(transactionStatus,transactionId);

                // 把transactionPeople写入数据库
                int flag5 = transactionService.updateTranPeople(stransactionPeople,transactionId);

                // 与区块链交互
                Map<String,Object> blockchain = new HashMap<>();
                JSONObject jsonObject = new JSONObject();


                // 通过transactionId找到数据库中的那一行
                TransactionEntity tran1 =null;
                tran1 = transactionService.selectByTransactionId(transactionId);


                // transaction表
                jsonObject.put("transactionId",transactionId);
                jsonObject.put("userRemains",tran1.getUserRemains());
                jsonObject.put("transactionMoney",tran1.getTransactionMoney());
                jsonObject.put("transactionPeople",tran1.getTransactionPeople());
                jsonObject.put("transactionType",tran1.getTransactionType());
                jsonObject.put("transactionTime",tran1.getTransactionTime());
                jsonObject.put("transactionStatus",tran1.getTransactionStatus());


                blockchain.put("key",transactionId);
                blockchain.put("value",jsonObject);

                String str= HttpUtils.doPost("http://192.168.31.245:8090/writeGive",blockchain);
                System.out.println("转赠区块链Id为：" + str);


                // 返回
                result.put("status",true);
                result.put("message","转赠成功");
                result.put("transactionId",transactionId);
                result.put("userRemains",tran1.getUserRemains());
                result.put("transactionMoney",tran1.getTransactionMoney());
                result.put("transactionPeople",tran1.getTransactionPeople());
                result.put("transactionType",tran1.getTransactionType());
                result.put("transactionTime",tran1.getTransactionTime());
                result.put("transactionStatus",tran1.getTransactionStatus());

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
     * 积分商店体现，交易对象为空
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/withdraw")
    public Object withdrawUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","没有给出userId");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","userId不能为空");
                return result;
            }

            if(!params.containsKey("transactionMoney")){
                result.put("status",false);
                result.put("message","没有给出transactionMoney");
                return result;
            }
            if(params.get("transactionMoney").toString().equals("none")){
                result.put("status",false);
                result.put("message","transactionMoney不能为空");
                return result;
            }

            // 获取参数
            String userId = params.get("userId").toString();
            String transactionMoney = params.get("transactionMoney").toString();

            // 生成transactionId，写入transaction表
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String transactionId = id.replace("-" , "");
            System.out.println("transactionId为：" + transactionId);

            // 通过userId找到数据库中的那一行
            UserEntity u1 = null;
            u1 = userService.selectByUserId(userId);

            // 用户余额解密
            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            BigInteger mu1Remains = paillier.SDecryption(new CipherPub(u1.getRemains()));
            System.out.println("u1余额解密后：" + mu1Remains);
            System.out.println("transactionMoney为：" + transactionMoney);

            // 获取用户余额减去transactionMoney，判断用户余额是否够体现
//            Integer userRemains = Integer.parseInt(u1.getRemains());
            if(mu1Remains.intValue() - Integer.parseInt(transactionMoney) < 0){
                result.put("status",false);
                result.put("message","余额不足，无法体现");

                return result;
            }
            else{

                // 减去用户体现的金额
//                userRemains -= Integer.parseInt(transactionMoney);
                int i = mu1Remains.intValue() - Integer.parseInt(transactionMoney);
                System.out.println("提现后u1余额为：" + i);

                // 将用户余额更新至用户表中
                String si =  paillier.Encryption(BigInteger.valueOf(i),pk).toString();
                int flag = userService.updateRemains(si,userId);

                // 设置交易类型为提现2
                String transactionType = "2";

                // 加密transactionMoney
                String stransactionMoney = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(transactionMoney)),pk).toString();

                // 将以上信息统一写入transaction表中
                int flag1 = transactionService.insertNotarTran(transactionId,userId,u1.getRemains(),stransactionMoney,transactionType);

                // 生成支付交易时间
                Date time = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String transactionTime = sdf.format(time);
                int flag2 = transactionService.updateTranTime(transactionTime,transactionId);

                // 判断交易状态，修改为已支付1
                String transactionStatus = "1";
                int flag3 = transactionService.updateTranStatus(transactionStatus,transactionId);


                // 与区块链交互
                Map<String,Object> blockchain = new HashMap<>();
                JSONObject jsonObject = new JSONObject();


                // 通过transactionId找到数据库中的那一行
                TransactionEntity tran1 =null;
                tran1 = transactionService.selectByTransactionId(transactionId);

                // transaction表
                jsonObject.put("transactionId",transactionId);
                jsonObject.put("userRemains",tran1.getUserRemains());
                jsonObject.put("transactionMoney",tran1.getTransactionMoney());
                jsonObject.put("transactionPeople",tran1.getTransactionPeople());
                jsonObject.put("transactionType",tran1.getTransactionType());
                jsonObject.put("transactionTime",tran1.getTransactionTime());
                jsonObject.put("transactionStatus",tran1.getTransactionStatus());


                blockchain.put("key",transactionId);
                blockchain.put("value",jsonObject);

                String str= HttpUtils.doPost("http://192.168.31.245:8090/writeWithdraw",blockchain);
                System.out.println("提现区块链Id为：" + str);


                // 返回
                result.put("status",true);
                result.put("message","体现成功");
                result.put("transactionId",transactionId);
                result.put("userRemains",tran1.getUserRemains());
                result.put("transactionMoney",tran1.getTransactionMoney());
                result.put("transactionPeople",tran1.getTransactionPeople());
                result.put("transactionType",tran1.getTransactionType());
                result.put("transactionTime",tran1.getTransactionTime());
                result.put("transactionStatus",tran1.getTransactionStatus());

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
     * 积分商城购买存储空间
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/memPay")
    public Object memPayUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            JSONObject params = ParseRequest.parse(req);

            // 判断前端传来的参数是否正确
            if(!params.containsKey("userId")){
                result.put("status",false);
                result.put("message","没有给出userId");
                return result;
            }
            if(params.get("userId").toString().equals("none")){
                result.put("status",false);
                result.put("message","userId不能为空");
                return result;
            }

            if(!params.containsKey("transactionMoney")){
                result.put("status",false);
                result.put("message","没有给出transactionMoney");
                return result;
            }
            if(params.get("transactionMoney").toString().equals("none")){
                result.put("status",false);
                result.put("message","transactionMoney不能为空");
                return result;
            }

            if(!params.containsKey("storageSize")){
                result.put("status",false);
                result.put("message","没有给出storageSize");
                return result;
            }
            if(params.get("storageSize").toString().equals("none")){
                result.put("status",false);
                result.put("message","storageSize不能为空");
                return result;
            }

            // 获取参数
            String userId = params.get("userId").toString();
            String transactionMoney = params.get("transactionMoney").toString();
            String storageSize = params.get("storageSize").toString();

            // 生成transactionId，写入transaction表
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String transactionId = id.replace("-" , "");
            System.out.println("transactionId为：" + transactionId);

            // 通过userId找到数据库中的那一行
            UserEntity u1 = null;
            u1 = userService.selectByUserId(userId);

            // 用户余额解密
            // 生成注册用户的公私钥
            PaillierT paillier = new PaillierT(PaillierT.param);
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

            BigInteger mu1Remains = paillier.SDecryption(new CipherPub(u1.getRemains()));
            System.out.println("u1余额解密后：" + mu1Remains);
            System.out.println("transactionMoney为：" + transactionMoney);

            // 获取用户余额减去transactionMoney，判断用户余额是否够买存储空间
//            Integer userRemains = Integer.parseInt(u1.getRemains());
            if(mu1Remains.intValue() - Integer.parseInt(transactionMoney) < 0){

                result.put("status",false);
                result.put("message","余额不足，无法购买存储空间");

                return result;
            }
            else{

                // 减去用户购买存储空间的金额
//                userRemains -= Integer.parseInt(transactionMoney);
                int i = mu1Remains.intValue() - Integer.parseInt(transactionMoney);
                System.out.println("购买存储空间后u1的余额为：" + i);

                // 将用户余额更新至用户表中
                String si =  paillier.Encryption(BigInteger.valueOf(i),pk).toString();
                int flag = userService.updateRemains(si,userId);

                // 获取用户存储空间，加上购买的存储空间
//                Integer u1StorageSize =Integer.parseInt(u1.getStorageSpace());
//                u1StorageSize += Integer.parseInt(storageSize);
                BigInteger mu1StorageSpace = paillier.SDecryption(new CipherPub(u1.getStorageSpace()));
                System.out.println("u1存储空间解密后：" + mu1StorageSpace);

                int j = mu1StorageSpace.intValue() + Integer.parseInt(storageSize);
                System.out.println("购买后的存储空间为：" + j);

                // 更新存储空间至用户表中
                String sj = paillier.Encryption(BigInteger.valueOf(j),pk).toString();
                int flag1 = userService.updateStorageSpace(sj,userId);

                // 设置交易类型为购买存储空间3
                String transactionType = "3";

                // 加密transactionMoney
                String stransactionMoney = paillier.Encryption(BigInteger.valueOf(Integer.parseInt(transactionMoney)),pk).toString();

                // 将以上信息统一写入transaction表中
                int flag2 = transactionService.insertNotarTran(transactionId,userId,u1.getRemains(),stransactionMoney,transactionType);

                // 生成支付交易时间
                Date time = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String transactionTime = sdf.format(time);
                int flag3 = transactionService.updateTranTime(transactionTime,transactionId);

                // 判断交易状态，修改为已支付1
                String transactionStatus = "1";
                int flag4 = transactionService.updateTranStatus(transactionStatus,transactionId);


                // 与区块链交互
                Map<String,Object> blockchain = new HashMap<>();
                JSONObject jsonObject = new JSONObject();

                // 通过transactionId找到数据库中的那一行
                TransactionEntity tran1 =null;
                tran1 = transactionService.selectByTransactionId(transactionId);

                // transaction表
                jsonObject.put("transactionId",transactionId);
                jsonObject.put("userRemains",tran1.getUserRemains());
                jsonObject.put("transactionMoney",tran1.getTransactionMoney());
                jsonObject.put("transactionPeople",tran1.getTransactionPeople());
                jsonObject.put("transactionType",tran1.getTransactionType());
                jsonObject.put("transactionTime",tran1.getTransactionTime());
                jsonObject.put("transactionStatus",tran1.getTransactionStatus());


                blockchain.put("key",transactionId);
                blockchain.put("value",jsonObject);

                String str= HttpUtils.doPost("http://192.168.31.245:8090/writeMemPay",blockchain);
                System.out.println("购买存储空间区块链Id为：" + str);


                // 返回
                result.put("status",true);
                result.put("message","存储空间购买成功");
                result.put("transactionId",transactionId);
                result.put("userRemains",tran1.getUserRemains());
                result.put("transactionMoney",tran1.getTransactionMoney());
                result.put("transactionPeople",tran1.getTransactionPeople());
                result.put("transactionType",tran1.getTransactionType());
                result.put("transactionTime",tran1.getTransactionTime());
                result.put("transactionStatus",tran1.getTransactionStatus());


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


    @Autowired(required = false)
    EvidenceMapper evidenceMapper;
    /**
     * 用户上传文件
     * @param req
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/user/addEvidence")
    public Object addEvidenceUser(HttpServletRequest req){

        Map<String,Object> result = new HashMap<>();

        try {

            // 获取参数
            String userId = req.getParameter("userId");
            String evidenceType = req.getParameter("evidenceType");
            String evidenceName = req.getParameter("evidenceName");

            MultipartHttpServletRequest multipartReq = (MultipartHttpServletRequest) req;
            List<MultipartFile> files = multipartReq.getFiles("file");

            //生成当前时间戳
            Date time = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current_time = sdf.format(time);
            SimpleDateFormat sdf_stamp = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = sdf_stamp.format(time);


            // 1. 文件存放路径 ："/用户id/"
            String folderPath = "/"+userId+"/"+evidenceName+"_"+timestamp;

            // 2. 转发数据给云后端
            Boolean status = HttpUtils.doPostFormData("http://localhost:8090/uploadFiles?folderPath="+folderPath, files);

            // 3. 根据返回信息判断是否上传成功，上传不成功，返回失败信息给前端
            if(status == false){
                result.put("status",false);
                result.put("message","Uploading to the cloud failed");
                return result;
            }

            //4. 上传成功

            // 生成evidenceId，写入evidence表
            String id = UUID.randomUUID().toString();
            // 将UUID中的“-”去掉
            String evidenceId = id.replace("-" , "");

            //计算文件总大小
            Integer filesize = 0;
            for(int i = 0; i < files.size(); i++) {
                Float filesize_tmp = Float.parseFloat(String.valueOf(files.get(i).getSize())) / 1024;
                filesize += filesize_tmp.intValue();
            }

            //4.1 存储信息到数据库

            //加密字段

            //Paillier初始化
            PaillierT paillier = new PaillierT(PaillierT.param);
            //为新用户生成公私钥
            BigInteger sk = new BigInteger(1024 - 12, 64, new Random());
            BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);
            //加密存证名称和文件大小,得到string形式的密文，这个是用来存数据库的
            String filesize_cipher = paillier.Encryption(BigInteger.valueOf(filesize),pk).toString();
            K2C16 k2c16 = new K2C16(evidenceName, pk, paillier);
            k2c16.StepOne();
            CipherPub tmp = k2c16.FIN;
            String evidenceName_cipher = tmp.toString();

            evidenceMapper.insertEvi(evidenceId, userId, evidenceType, evidenceName_cipher, folderPath, filesize_cipher, current_time);


            //4.2 与区块链交互，返回存证区块链交易id和上链时间
            Map<String,Object> blockchain = new HashMap<>();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("evidenceId",evidenceId);
            jsonObject.put("userId",userId);
            jsonObject.put("evidenceType",evidenceType);
            jsonObject.put("evidenceName",evidenceName);
            jsonObject.put("filePath",folderPath);
            jsonObject.put("fileSize",filesize);
            jsonObject.put("evidenceTime",current_time);


            blockchain.put("key",evidenceId);
            blockchain.put("value",jsonObject);

            String str= HttpUtils.doPost("http://192.168.31.245:8090/writeEvidence",blockchain);
            System.out.println("上传证据区块链Id为：" + str);

//            String str = "123456789";  //测试用
            String evidenceBlockchainId = str;
            Date tmp_time = new Date(System.currentTimeMillis());
            String blockchain_time = sdf.format(tmp_time);

            evidenceMapper.updateEviBCIdAndBCTime(evidenceBlockchainId, blockchain_time, evidenceId);

            // 5. 返回成功信息给前端
            result.put("status",true);
            result.put("message","success");
            result.put("evidenceBlockchainId",evidenceBlockchainId);

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
     * 用户/公证员/机构管理员/系统管理员 下载存证文件
     * @param req
     * @param response
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/downloadUserFile")
    public void getEvidenceFile(HttpServletRequest req, HttpServletResponse response){

        Map<String,Object> result = new HashMap<>();

        try {
            //1. 获取参数
            String evidenceId = req.getParameter("evidenceId");

            // 2. 从数据库中查找文件路径
            String folderPath = evidenceMapper.getfilePathByEvidenceId(evidenceId);
//            String[] tmp = folderPath.split("/");
            String folderName = folderPath.split("/")[2] + ".zip";
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {
                // 3. 向云服务请求文件，设置url
                URL url = new URL("http://localhost:8090/downloadFolder?folderPath=" + folderPath);

                //test
//                File f= new File("D:\\tmp\\打印报表.zip");
//                InputStream inputStream = null ;    // 准备好一个输入的对象
//                inputStream = new FileInputStream(f)  ;    // 通过对象多态性，进行实例化

                // 4. 获得云服务返回的输入流 InputStream，放入至 BufferedInputStream
                InputStream inputStream = url.openStream();
                bis = new BufferedInputStream(inputStream);

                // 5. 设置返回给前端的信息
                response.reset(); // 来清除首部的空白行
                response.setContentType("application/octet-stream"); // 二进制数据类型
                // content-disposition 响应头 控制浏览器 以下载的形式打开文件，前端收到 response 后会下载
                response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(folderName, "UTF-8") + "\"");

                // 6. 将输入流转成字节数组
                byte[] bytes = StreamUtils.streamToByteArray(bis);

                // 7. 获取到 response 的 OutputStream 输出流，并入 BufferedOutputStream
                bos = new BufferedOutputStream(response.getOutputStream());

                // 8. 写入文件数据
                bos.write(bytes);

            } catch (IOException e) {
                // 异常处理
                e.printStackTrace();
            } finally {
                // 关闭流
                try {
                    if (bos != null) {
                        bos.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
        }

    }











}




