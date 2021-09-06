package com.evidence.blockchainevidence.service;

import com.evidence.blockchainevidence.entity.NotaryEntity;
import com.evidence.blockchainevidence.mapper.NotaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotaryService {

    @Autowired
    private NotaryMapper notaryMapper;

    public NotaryEntity selectByNameAndPwd(String notaryName,String password){
        return notaryMapper.selectByNameAndPwd(notaryName,password);
    }

    public int insertNotary(String notaryId, String notaryName, String jobNumber, String password, String phoneNumber,
                            String idCard, String email, String sex, String organizationId, String notarizationType,
                            String publicKey, String workYear,String position){
        return notaryMapper.insertNotary(notaryId,notaryName,jobNumber,password,phoneNumber,idCard,email,sex,organizationId,notarizationType,publicKey,workYear,position);
    }

    public  int updateNotary(String notaryId,String newPassword,String phoneNumber,String idCard,String email,String sex,
                             String organizationId,String notarizationType,String workYear,String position){
        return notaryMapper.updateNotary(notaryId,newPassword,phoneNumber,idCard,email,sex,organizationId,notarizationType,workYear,position);
    }


}