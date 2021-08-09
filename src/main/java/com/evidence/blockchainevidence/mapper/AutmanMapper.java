package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.EvidenceEntity;
import com.evidence.blockchainevidence.entity.TransactionEntity;
import com.evidence.blockchainevidence.provider.QueryProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AutmanMapper {

//    @Select("select evidenceId,userId,evidence.notaryId,notaryName,evidence.organizationId,organizationName from evidence, notary, organization " +
//            "where evidence.notaryId=notary.notaryId and evidence.organizationId=organization.organizationId" +
//            "#{sqlparam}")
    @SelectProvider(method = "selectEvidence", type = QueryProvider.class)
    List<EvidenceEntity> findEvidence(@Param("evidenceId")String evidenceId,@Param("userId")String userId,@Param("notaryId")String notaryId,@Param("notarizationStatus")String notarizationStatus,@Param("notarizationType")String notarizationType,
                              @Param("paymentStatus")String paymentStatus,@Param("evidenceType")String evidenceType,@Param("organizationId")String organizationId,
                              @Param("evidenceNameWildcard")String evidenceNameWildcard,@Param("notarizationStartTimeStart")String notarizationStartTimeStart,
                              @Param("notarizationStartTimeEnd")String notarizationStartTimeEnd,@Param("notarizationEndTimeStart")String notarizationEndTimeStart,
                              @Param("notarizationEndTimeEnd")String notarizationEndTimeEnd);
    @SelectProvider(method = "selectTransaction", type = QueryProvider.class)
    List<TransactionEntity> findTransaction(@Param("userId")String userId,
                                            @Param("transactionType")String transactionType, @Param("usernameWildcard")String usernameWildcard, @Param("transactionTimeStart")String transactionTimeStart,
                                            @Param("transactionTimeEnd")String transactionTimeEnd);




}