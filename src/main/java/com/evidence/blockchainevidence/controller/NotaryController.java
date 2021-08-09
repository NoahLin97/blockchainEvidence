package com.evidence.blockchainevidence.controller;

import com.alibaba.fastjson.JSONObject;
import com.evidence.blockchainevidence.mapper.AutmanMapper;
import com.evidence.blockchainevidence.mapper.NotaryMapper;
import com.evidence.blockchainevidence.utils.ParseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static com.evidence.blockchainevidence.controller.AutmanController.eviSelect;


@RestController
public class NotaryController {


    @Autowired(required = false)
    NotaryMapper notaryMapper;
    @Autowired(required = false)
    AutmanMapper autmanMapper;
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
            if(params.containsKey("paymentStatus")){
                params.remove("paymentStatus ");
            }
            params.put("paymentStatus","1");
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


}




