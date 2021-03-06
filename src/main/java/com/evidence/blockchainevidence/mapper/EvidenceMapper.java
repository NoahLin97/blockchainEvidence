package com.evidence.blockchainevidence.mapper;


import com.evidence.blockchainevidence.entity.EvidenceEntity;
import org.apache.ibatis.annotations.*;
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

    @Update("update evidence set notarizationMatters = #{notarizationMatters} where evidenceId = #{evidenceId}")
    int updateNotarMatters(@Param("notarizationMatters") String notarizationMatters,@Param("evidenceId") String evidenceId);

    @Update("update evidence set notarizationStartTime = #{notarizationStartTime} where evidenceId = #{evidenceId}")
    int updateNotarStartTime(@Param("notarizationStartTime") String notarizationStartTime,@Param("evidenceId") String evidenceId);

    @Update("update evidence set transactionID = #{transactionId} where evidenceId = #{evidenceId}")
    int updateTranId(@Param("transactionId") String transactionId,@Param("evidenceId") String evidenceId);

    @Update("update evidence set transactionStatus = #{transactionStatus} where evidenceId = #{evidenceId}")
    int updateTranStatus(@Param("transactionStatus") String transactionStatus,@Param("evidenceId") String evidenceId);

    @Update("update evidence set notarizationMoney = #{notarizationMoney} where evidenceId = #{evidenceId}")
    int updateNotarMoney(@Param("notarizationMoney") String notarizationMoney,@Param("evidenceId") String evidenceId);

    @Update("update evidence set notarizationInformation = #{notarizationInformation} where evidenceId = #{evidenceId}")
    int updateNotarInfo(@Param("notarizationInformation") String notarizationInformation,@Param("evidenceId") String evidenceId);

    @Update("update evidence set notarizationEndTime = #{notarizationEndTime} where evidenceId = #{evidenceId}")
    int updateNotarEndTime(@Param("notarizationEndTime") String notarizationEndTime,@Param("evidenceId") String evidenceId);

    @Update("update evidence set EvidenceBlockchainId = #{EvidenceBlockchainId} where evidenceId = #{evidenceId}")
    int updateEvidenceBlockchainId(@Param("EvidenceBlockchainId") String EvidenceBlockchainId,@Param("evidenceId") String evidenceId);

    @Update("update evidence set blockchainTime = #{blockchainTime} where evidenceId = #{evidenceId}")
    int updateBlockchainTime(@Param("blockchainTime") String blockchainTime,@Param("evidenceId") String evidenceId);

    @Update("update evidence set notarizationBlockchainIdStart = #{notarizationBlockchainIdStart} where evidenceId = #{evidenceId}")
    int updateNotarBlockchainIdStart(@Param("notarizationBlockchainIdStart") String notarizationBlockchainIdStart,@Param("evidenceId") String evidenceId);

    @Update("update evidence set notarizationBlockchainIdEnd = #{notarizationBlockchainIdEnd} where evidenceId = #{evidenceId}")
    int updateNotarBlockchainIdEnd(@Param("notarizationBlockchainIdEnd") String notarizationBlockchainIdEnd,@Param("evidenceId") String evidenceId);

    @Update("update evidence set notaryId = #{notaryId} where evidenceId = #{evidenceId}")
    int updateNotaryId(@Param("notaryId") String notaryId,@Param("evidenceId") String evidenceId);


    @Insert("insert into evidence(evidenceId, userId, evidenceType, evidenceName, filePath, fileSize, evidenceTime, notarizationStatus)\n" +
            "select #{evidenceId} as evidenceId, #{userId} as userId, #{evidenceType} as evidenceType, #{evidenceName} as evidenceName, " +
            "#{filePath} as filePath, #{fileSize} as fileSize, #{evidenceTime} as evidenceTime, '0' as notarizationStatus;")
    void insertEvi(@Param("evidenceId") String evidenceId, @Param("userId") String userId, @Param("evidenceType") String evidenceType, @Param("evidenceName") String evidenceName,
                   @Param("filePath") String filePath, @Param("fileSize") String fileSize, @Param("evidenceTime") String evidenceTime);

    @Select("select filePath from evidence where evidenceId = #{evidenceId};")
    String getfilePathByEvidenceId(@Param("evidenceId") String evidenceId);

    @Update("update evidence set evidenceBlockchainId = #{evidenceBlockchainId}, blockchainTime = #{blockchainTime} " +
            "where evidenceId = #{evidenceId}")
    void updateEviBCIdAndBCTime(@Param("evidenceBlockchainId") String evidenceBlockchainId,
                                  @Param("blockchainTime") String blockchainTime,
                                  @Param("evidenceId") String evidenceId);

}
