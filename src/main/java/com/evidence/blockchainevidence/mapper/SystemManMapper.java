package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.ManagerEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SystemManMapper {


    @Select("select * from manager where username= #{username} and password = #{password} ")
    ManagerEntity selectByNameAndPwd(@Param("username") String username, @Param("password") String password);

    @Insert("insert into manager(manId,username,password,phoneNumber,idCard,email,sex)" +
            " values(#{manId},#{username},#{password},#{phoneNumber},#{idCard},#{email},#{sex})")
    int insertSystemMan(@Param("manId") String manId, @Param("username") String username, @Param("password") String password,
                     @Param("phoneNumber") String phoneNumber, @Param("idCard") String idCard, @Param("email") String email,
                     @Param("sex") String sex);

    @Update("update manager set password = #{newPassword},phoneNumber = #{phoneNumber},idCard = #{idCard},email = #{email},sex = #{sex} where manId = #{manId} ")
    int updateSystemMan(@Param("manId") String manId,@Param("newPassword") String newPassword,@Param("phoneNumber") String phoneNumber,
                   @Param("idCard") String idCard,@Param("email") String email,@Param("sex") String sex);

}
