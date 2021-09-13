package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.MaterialEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    @Select("select * from material where organizationId = #{organizationId} and notarizationType = #{notarizationType};")
    List<MaterialEntity> getMaterialByoriIdAndnotarType(@Param("organizationId") String organizationId,
                                                        @Param("notarizationType") String notarizationType);
}
