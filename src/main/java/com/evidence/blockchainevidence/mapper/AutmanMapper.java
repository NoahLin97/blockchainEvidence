package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.*;
import com.evidence.blockchainevidence.provider.QueryProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    @Select("select * from aut_manager where autName= #{autName} and password = #{password} ")
    AutManagerEntity selectByNameAndPwd(@Param("autName") String username, @Param("password") String password);


    @Insert("insert into aut_manager(autManId,autName,password,phoneNumber,idCard,email,sex,organizationId)" +
            " values(#{autManId},#{autName},#{password},#{phoneNumber},#{idCard},#{email},#{sex},#{organizationId})")
    int insertAutman(@Param("autManId") String autManId, @Param("autName") String autName, @Param("password") String password,
                   @Param("phoneNumber") String phoneNumber, @Param("idCard") String idCard, @Param("email") String email,
                   @Param("sex") String sex, @Param("organizationId") String organizationId);


    @Update("update aut_manager set password = #{newPassword},phoneNumber = #{phoneNumber},idCard = #{idCard},email = #{email},sex = #{sex}, organizationId = #{organizationId} where autManId = #{autManId} ")
    int updateAutman(@Param("autManId") String autManId,@Param("newPassword") String newPassword,@Param("phoneNumber") String phoneNumber,
                   @Param("idCard") String idCard,@Param("email") String email,@Param("sex") String sex,@Param("organizationId") String organizationId);


    //总成功数
    @Select("SELECT count(*) as a FROM `evidence` where notarizationStatus=\"3\"")
    int totalSuccess();

    //总不成功数
    @Select("SELECT count(*) as a FROM `evidence` where notarizationStatus=\"4\"")
    int totalNotSuccess();

    //分组成功数成功数
    @Select("SELECT notarizationType, count(case when notarizationStatus='3' then 1 end) as successNum, count(case when notarizationStatus='4' then 1 end) as failedNum , count(case when notarizationStatus='3' or notarizationStatus='4' then 1 end) as totalNum FROM `evidence` group by notarizationType")
    List<Map<String,Object>> totalTypeSuccess();

    @Select("select * from aut_manager where autManId= #{autManId}")
    AutManagerEntity selectByAutManId(@Param("autManId") String autManId);
}