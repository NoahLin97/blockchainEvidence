package com.evidence.blockchainevidence.controller;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.PaillierT.CipherPub;
import com.evidence.blockchainevidence.PaillierT.PaillierT;
import com.evidence.blockchainevidence.entity.NotaryEntity;
import com.evidence.blockchainevidence.entity.User;
import com.evidence.blockchainevidence.helib.SEA;
import com.evidence.blockchainevidence.helib.SLT;
import com.evidence.blockchainevidence.mapper.NotaryMapper;
import com.evidence.blockchainevidence.mapper.UserMapper;
import com.evidence.blockchainevidence.service.UserService;
import com.evidence.blockchainevidence.subprotocols.K2C8;
import com.evidence.blockchainevidence.subprotocols.KMP;
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
import java.util.List;
import java.util.Map;
import java.util.Random;


@RestController
public class UserController {

    @Autowired(required = false)
    UserMapper userMapper;
    @Autowired(required = false)
    NotaryMapper notaryMapper;
    @Autowired
    UserService userService;


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
            K2C8 SK0 = new K2C8(qw, pk, paillier);
            SK0 = new K2C8(kw, pk, paillier);
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
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @PostMapping("/login")
    public Object loginUser(String username,String password){

        User user1 = null;
        Map<String,String> result = new HashMap<>();

        // 将密码用sha-256加密
        password = Sha256.SHA(password);
//        System.out.println("SHA-256后密码为：" + password);

        // 判断用户名和密码对是否存在数据库中
        user1=userService.selectByNameAndPwd(username,password);
        if(user1 != null){
            result.put("message","登陆成功");
            result.put("username",user1.getUsername());
            return result;
        }

        result.put("message","登录失败");
        return result;
    }

    /**
     * 用户注册
     * @param userId 用户Id
     * @param username 用户名
     * @param password 密码
     * @param phoneNumber 电话号码
     * @param idCard 身份证号码
     * @param email 邮箱
     * @param sex 性别
     * @param remains 余额
     * @param storageSpace 存储空间
     * @param hasUsedStorage 已使用存储空间
     * @return
     */
    @PostMapping("/user/register")
    public Object registerUser(int userId, String username, String password, String phoneNumber, String idCard,
                               String email, User.Sex sex, int remains, int storageSpace, int hasUsedStorage){

        Map<String,String> result = new HashMap<>();

        // 将密码用sha-256加密
        password = Sha256.SHA(password);
//        System.out.println("SHA-256后密码为：" + password);

        // 生成注册用户的公私钥
        PaillierT paillier = new PaillierT();
        BigInteger sk = new BigInteger(1024, 64, new Random());
        BigInteger pk = paillier.g.modPow(sk, paillier.nsquare);

        // 数据库public_key字段长度要设置大一点,不然存不下
        String publicKey = pk.toString();
//        System.out.println("公钥为：" + pk);
//        System.out.println("公钥为：" + publicKey);

        int flag = userService.insertUser(userId,username,password,phoneNumber,idCard,
                email,sex,remains,storageSpace,hasUsedStorage,publicKey);

        if(flag == 1){
            result.put("message","注册成功");
            return result;
        }
        else {
            result.put("message","注册失败");
            return result;
        }
    }




}




