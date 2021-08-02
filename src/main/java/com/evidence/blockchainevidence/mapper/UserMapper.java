package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.Sex;
import com.evidence.blockchainevidence.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    @Insert("insert into user(user_id,username,password,phone_number,id_card,email,sex,remains,storage_space,has_used_storage,public_key)" +
            " values(#{userId},#{username},#{password},#{phoneNumber},#{idCard},#{email},#{sex},#{remains},#{storageSpace},#{hasUsedStorage},#{publicKey})")
    int insertUser(@Param("userId") String userId, @Param("username") String username, @Param("password") String password,
                   @Param("phoneNumber") String phoneNumber, @Param("idCard") String idCard, @Param("email") String email,
                   @Param("sex") Sex sex, @Param("remains") int remains, @Param("storageSpace") int storageSpace,
                   @Param("hasUsedStorage") int hasUsedStorage, @Param("publicKey") String publicKey);

    @Select("select * from user where username= #{username} and password = #{password} ")
    UserEntity selectByNameAndPwd(@Param("username") String username,@Param("password") String password);

    @Select("select * from user")
    List<UserEntity> findAll();





}
