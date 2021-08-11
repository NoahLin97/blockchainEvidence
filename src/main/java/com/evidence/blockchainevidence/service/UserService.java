package com.evidence.blockchainevidence.service;


import com.evidence.blockchainevidence.entity.UserEntity;
import com.evidence.blockchainevidence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserEntity selectByNameAndPwd(String username, String password){
        return userMapper.selectByNameAndPwd(username,password);
    }


    public int insertUser(String userId, String username, String password, String phoneNumber,
                          String idCard, String email,String sex, String remains, String storageSpace, String hasUsedStorage, String publicKey) {
        return userMapper.insertUser(userId,username,password,phoneNumber,
                idCard,email,sex,remains,storageSpace,hasUsedStorage,publicKey);
    }


    public int updateUser(String userId,String newPassword,String phoneNumber,String idCard,String email,String sex){
        return userMapper.updateUser(userId,newPassword,phoneNumber,idCard,email,sex);
    }


    public UserEntity selectByUserId(String userId){
        return userMapper.selectByUserId(userId);
    }

    public int updateRemains(String remains,String userId){
        return userMapper.updateRemains(remains,userId);
    }



}