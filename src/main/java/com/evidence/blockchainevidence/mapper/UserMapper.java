package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    @Insert("insert into user(userId,username,password,phoneNumber,idCard,email,sex,remains,storageSpace,hasUsedStorage,publicKey)" +
            " values(#{userId},#{username},#{password},#{phoneNumber},#{idCard},#{email},#{sex},#{remains},#{storageSpace},#{hasUsedStorage},#{publicKey})")
    int insertUser(@Param("userId") String userId, @Param("username") String username, @Param("password") String password,
                   @Param("phoneNumber") String phoneNumber, @Param("idCard") String idCard, @Param("email") String email,
                   @Param("sex") String sex, @Param("remains") String remains, @Param("storageSpace") String storageSpace,
                   @Param("hasUsedStorage") String hasUsedStorage, @Param("publicKey") String publicKey);


    @Select("select * from user where username= #{username} and password = #{password} ")
    UserEntity selectByNameAndPwd(@Param("username") String username, @Param("password") String password);


    @Select("select * from user")
    List<UserEntity> findAll();


    @Update("update user set password = #{newPassword},phoneNumber = #{phoneNumber},idCard = #{idCard},email = #{email},sex = #{sex} where userId = #{userId} ")
    int updateUser(@Param("userId") String userId,@Param("newPassword") String newPassword,@Param("phoneNumber") String phoneNumber,
                   @Param("idCard") String idCard,@Param("email") String email,@Param("sex") String sex);


    @Select("select * from user where userId = #{userId}")
    UserEntity selectByUserId(@Param("userId") String userId);

    @Update("update user set remains = #{remains} where userId = #{userId}")
    int updateRemains(@Param("remains") String remains,@Param("userId") String userId);

    @Update("update user set storageSpace = #{storageSpace} where userId = #{userId}")
    int updateStorageSpace(@Param("storageSpace") String storageSpace,@Param("userId") String userId);

    @Update("update user set hasUsedStorage = #{hasUsedStorage} where userId = #{userId}")
    int updateHasUsedStorage(@Param("hasUsedStorage") String hasUsedStorage,@Param("userId") String userId);

    @Select("select * from user where username = #{userName}")
    UserEntity selectByUserName(String userName);
}
