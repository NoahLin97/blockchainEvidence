package com.evidence.blockchainevidence.mapper;


import com.evidence.blockchainevidence.entity.NotarizationTypeEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NotarizationTypeMapper {

    @Insert("insert into notarization_type(notarizationTypeId,autManId,notarizationType,notarizationMoney) " +
            "values(#{notarizationTypeId},#{autManId},#{notarizationType},#{notarizationMoney})")
    int insertNotarizationType(@Param("notarizationTypeId") String notarizationTypeId,@Param("autManId") String autManId, @Param("notarizationType") String notarizationType, @Param("notarizationMoney") String notarizationMoney);


    @Update("update notarization_type set notarizationType = #{newNotarizationType},autManId = #{autManId}" +
            "where notarizationType = #{notarizationType} ")
    int updateNotarizationType(@Param("newNotarizationType") String newNotarizationType, @Param("notarizationType") String notarizationType,@Param("autManId") String autManId);

    @Update("update notarization_type set notarizationMoney = #{notarizationMoney}, autManId = #{autManId} " +
            "where notarizationType = #{notarizationType} ")
    int updateNotarizationMoney(@Param("notarizationMoney") String notarizationMoney,@Param("notarizationType") String notarizationType,@Param("autManId") String autManId);


    @Select("select * from notarization_type where notarizationTypeId = #{notarizationTypeId}")
    NotarizationTypeEntity selectNotarizationType(@Param("notarizationTypeId") String notarizationTypeId);

    @Select("select * from notarization_type")
    List<NotarizationTypeEntity> selectNotarizationTypeAll();
}
