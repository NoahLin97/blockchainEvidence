package com.evidence.blockchainevidence.mapper;


import com.evidence.blockchainevidence.entity.EvidenceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface EvidenceMapper {

    @Select("select * from evidence where evidenceId = #{evidenceId}")
    EvidenceEntity selectByEvidenceId(@Param("evidenceId") String evidenceId);

    @Update("update evidence set organizationId = #{organizationId}, notarizationType = #{notarizationType} where evidenceId = #{evidenceId}")
    int updateOrganIdAndNotarType(@Param("organizationId") String organizationId,@Param("notarizationType") String notarizationType,@Param("evidenceId") String evidenceId);

    @Update("update evidence set notarizationStatus = #{notarizationStatus} where evidenceId = #{evidenceId}")
    int updateNotarStatus(@Param("notarizationStatus") String notarizationStatus,@Param("evidenceId") String evidenceId);




}
