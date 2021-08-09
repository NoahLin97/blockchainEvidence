package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.*;
import com.evidence.blockchainevidence.provider.QueryProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
                                      @Param("evidenceNameWildcard")String evidenceNameWildcard,@Param("usernameWildcard")String usernameWildcard,@Param("notarizationStartTimeStart")String notarizationStartTimeStart,
                                      @Param("notarizationStartTimeEnd")String notarizationStartTimeEnd,@Param("notarizationEndTimeStart")String notarizationEndTimeStart,
                                      @Param("notarizationEndTimeEnd")String notarizationEndTimeEnd);

    @SelectProvider(method = "selectEvidence2", type = QueryProvider.class)
    List<EvidenceEntity> findEvidence2(@Param("evidenceId")String evidenceId, @Param("userId")String userId, @Param("usernameWildcard")String usernameWildcard,
                                       @Param("notarizationStatus")String notarizationStatus, @Param("evidenceBlockchainId")String evidenceBlockchainId,
                                       @Param("evidenceType")String evidenceType,
                                       @Param("evidenceNameWildcard")String evidenceNameWildcard,@Param("evidenceTimeStart")String evidenceTimeStart,
                                       @Param("evidenceTimeEnd")String evidenceTimeEnd, @Param("blockchainTimeStart")String blockchainTimeStart,
                                       @Param("blockchainTimeEnd") String blockchainTimeEnd);

    @SelectProvider(method = "selectTransaction", type = QueryProvider.class)
    List<TransactionEntity> findTransaction(@Param("userId")String userId,
                                            @Param("transactionType")String transactionType, @Param("usernameWildcard")String usernameWildcard, @Param("transactionTimeStart")String transactionTimeStart,
                                            @Param("transactionTimeEnd")String transactionTimeEnd);

    @SelectProvider(method = "selectUser", type = QueryProvider.class)
    List<UserEntity> findUser(@Param("userId")String userId, @Param("usernameWildcard")String usernameWildcard, @Param("phoneNumberWildcard")String phoneNumberWildcard,
                              @Param("emailWildcard")String emailWildcard, @Param("sex")String sex);
    @SelectProvider(method = "selectOrganization", type = QueryProvider.class)
    List<OrganizationEntity> findOrganization(@Param("organizationId")String organizationId , @Param("organizationIdNameWildcard")String organizationIdNameWildcard, @Param("addressWildcard")String addressWildcard,
                                              @Param("phoneNumberWildcard")String phoneNumberWildcard, @Param("legalPeopleWildcard")String legalPeopleWildcard, @Param("emailWildcard")String emailWildcard);
    @SelectProvider(method = "selectNotary", type = QueryProvider.class)
    List<NotaryEntity> findNotary(@Param("notaryId")String notaryId , @Param("notaryNameWildcard")String notaryNameWildcard, @Param("phoneNumberWildcard")String phoneNumberWildcard, @Param("jobNumberWildcard")String jobNumberWildcard,
                                  @Param("emailWildcard")String emailWildcard, @Param("sex")String sex, @Param("organizationId")String organizationId, @Param("notarizationType")String notarizationType );
    @SelectProvider(method = "selectAutman", type = QueryProvider.class)
    List<AutManagerEntity> findAutman(@Param("autManId")String autManId , @Param("autNameWildcard")String autNameWildcard, @Param("phoneNumberWildcard")String phoneNumberWildcard, @Param("jobNumberWildcard")String jobNumberWildcard,
                                      @Param("emailWildcard")String emailWildcard, @Param("sex")String sex, @Param("organizationId")String organizationId);


    @Select("SELECT DISTINCT timeFlag FROM notary_statistics")
    List<String> findNotaTimes();

    @Select("SELECT DISTINCT timeFlag FROM organization_statistics")
    List<String> findOrgTimes();
}