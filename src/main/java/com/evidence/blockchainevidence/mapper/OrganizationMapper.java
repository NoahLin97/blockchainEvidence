package com.evidence.blockchainevidence.mapper;


import com.evidence.blockchainevidence.entity.OrganizationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OrganizationMapper {

    @Select("select * from organization where organizationId = #{organizationId}")
    OrganizationEntity selectByOrganizationId(@Param("organizationId") String organizationId);

//    @Insert()
//    int insertOrganization();

}
