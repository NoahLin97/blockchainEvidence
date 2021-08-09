package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.NotaryEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NotaryMapper {

    @Insert("insert into notary(notaryId,notaryName,jobNumber,password,phoneNumber,idCard,email,sex,organizationId,notarizationType,publicKey) " +
            "values(#{notaryId},#{notaryName},#{jobNumber},#{password},#{phoneNumber},#{idCard},#{email},#{sex},#{organizationId},#{notarizationType},#{publicKey})")
    int insertNotary(@Param("notaryId") String notaryId, @Param("notaryName") String notaryName, @Param("jobNumber") String jobNumber,
                     @Param("password") String password, @Param("phoneNumber") String phoneNumber, @Param("idCard") String idCard,
                     @Param("email") String email, @Param("sex")String sex, @Param("organizationId") String organizationId,
                     @Param("notarizationType") String notarizationType, @Param("publicKey") String publicKey);

    @Select("select * from notary where notaryName = #{notaryName} and password = #{password}")
    NotaryEntity selectByNameAndPwd(@Param("notaryName") String notaryName,@Param("password") String password);

    @Update("update notary set password = #{newPassword},phoneNumber = #{phoneNumber},idCard = #{idCard},email = #{email},sex = #{sex}," +
            "organizationId = #{organizationId},notarizationType = #{notarizationType} where notaryId = #{notaryId} ")
    int updateNotary(@Param("notaryId") String notaryId,@Param("newPassword") String newPassword,@Param("phoneNumber") String phoneNumber,
                     @Param("idCard") String idCard,@Param("email") String email,@Param("sex") String sex,@Param("organizationId") String organizationId,@Param("notarizationType") String notarizationType);


    @Select("select * from notary")
    List<NotaryEntity> findAll();

}
