package com.evidence.blockchainevidence.service;


import com.evidence.blockchainevidence.entity.User;
import com.evidence.blockchainevidence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User selectByNameAndPwd(String username,String password){
        return userMapper.selectByNameAndPwd(username,password);
    }

    public int insertUser(int userId, String username, String password, String phoneNumber,
                          String idCard, String email,User.Sex sex, int remains, int storageSpace, int hasUsedStorage, String publicKey) {
        return userMapper.insertUser(userId,username,password,phoneNumber,
                idCard,email,sex,remains,storageSpace,hasUsedStorage,publicKey);
    }

}
