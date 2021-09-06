package com.evidence.blockchainevidence.mapper;


import com.evidence.blockchainevidence.entity.NotarizationTypeEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NotarizationTypeMapper {

    @Insert("insert into notarization_type(notarizationTypeId,notarizationTypeName,notarizationMoney) " +
            "values(#{notarizationTypeId},#{notarizationTypeName},#{notarizationMoney})")
    int insertNotarizationType(@Param("notarizationTypeId") String notarizationTypeId, @Param("notarizationTypeName") String notarizationTypeName, @Param("notarizationMoney") String notarizationMoney);


    @Update("update notarization_type set notarizationTypeName = #{notarizationTypeName}, notarizationMoney = #{notarizationMoney}" +
            "where notarizationTypeId = #{notarizationTypeId}")
    int updateNotarizationType(@Param("notarizationTypeId") String notarizationTypeId, @Param("notarizationTypeName") String notarizationTypeName, @Param("notarizationMoney") String notarizationMoney);

    @Select("select * from notarization_type where notarizationTypeId = #{notarizationTypeId}")
    NotarizationTypeEntity selectNotarizationType(@Param("notarizationTypeId") String notarizationTypeId);


}
