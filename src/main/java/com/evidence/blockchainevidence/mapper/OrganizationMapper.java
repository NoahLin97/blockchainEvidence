package com.evidence.blockchainevidence.mapper;


import com.evidence.blockchainevidence.entity.OrganizationEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OrganizationMapper {


    @Select("select * from organization where organizationId = #{organizationId}")
    OrganizationEntity selectByOrganizationId(@Param("organizationId") String organizationId);


    @Insert("insert into organization(organizationId,organizationName,address,phoneNumber,email,legalPeople)" +
            " values(#{organizationId},#{organizationName},#{address},#{phoneNumber},#{email},#{legalPeople})")
    int insertOrganization(@Param("organizationId") String organizationId,@Param("organizationName") String organizationName,@Param("address") String address,
                            @Param("phoneNumber") String phoneNumber,@Param("email") String email,@Param("legalPeople") String legalPeople);



}
