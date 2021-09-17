package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.MaterialEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MaterialMapper {
    @Insert("insert into material(materialId, notarizationType, organizationId, filePath, uploadTime)\n" +
            "select uuid() as materialId, #{notarizationType} as notarizationType, aut_manager.organizationId, " +
            "#{filePath} as filePath, #{uploadTime} as uploadTime from aut_manager where autManId = #{autManId};")
    void insertMaterial(@Param("autManId") String autManId,
                        @Param("notarizationType") String notarizationType,
                        @Param("filePath") String filePath,
                        @Param("uploadTime") String uploadTime);

    @Select("select filePath from material where materialId = #{materialId};")
    String getfilePathByMaterialId(@Param("materialId") String materialId);

    @Select("select * from material where organizationId = #{organizationId};")
    List<MaterialEntity> getMaterialByOriId(@Param("organizationId") String organizationId);

    @Select("select * from material where organizationId = #{organizationId} and notarizationType = #{notarizationType};")
    MaterialEntity getMaterialByOriIdAndNotarType(@Param("organizationId") String organizationId,
                                            @Param("notarizationType") String notarizationType);

    @Update("update material set filePath = #{filePath}, uploadTime = #{uploadTime}\n" +
            "where materialId=#{materialId};")
    void updateMaterial(@Param("materialId") String materialId,
                        @Param("filePath") String filePath,
                        @Param("uploadTime") String uploadTime);
}
