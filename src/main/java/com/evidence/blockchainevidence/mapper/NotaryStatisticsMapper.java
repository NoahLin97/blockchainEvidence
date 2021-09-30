package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.EvidenceEntity;
import com.evidence.blockchainevidence.entity.NotaryStatisticsEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Component
public interface NotaryStatisticsMapper {
    @Select("select * from notary_statistics")
    List<NotaryStatisticsEntity> findAll();

    //公证员统计查询
    @Select("select * from notary_statistics where timestamp(timeFlag)=#{timeFlag}")
    List<NotaryStatisticsEntity> selectBytimeFlag(@Param("timeFlag") String timeFlag);

    //公证员统计生成
    @Insert("insert into notary_statistics(notaryStatisticsId ,notstyId, notaryName, organizationName, notarizationCount, notarizationFailCount, notarizationSuccessCount, notarizationTotalMoney, notarizationType, timeFlag)\n" +
            "select uuid() as notaryStatisticsId, tmp1.notaryId, notaryName, organizationName, notarizationCount, notarizationFailCount, notarizationSuccessCount, notarizationTotalMoney, notarizationType, #{timeFlag} as timeFlag from\n" +
            "(SELECT notaryId, notaryName, organization.organizationName, notarizationType from\n" +
            "notary\n" +
            "left join organization\n" +
            "on notary.organizationId=organization.organizationId) as tmp1\n" +
            "left join\n" +
            "(select notaryId, count(*) as notarizationCount from\n" +
            "(SELECT notary.notaryId,evidence.notarizationStatus FROM \n" +
            "notary\n" +
            "left join evidence\n" +
            "on notary.notaryId = evidence.notaryId) as tmp\n" +
            "where notarizationStatus<>'0' and notarizationStatus<>'1'\n" +
            "group by notaryId) as tmp2\n" +
            "on tmp1.notaryId = tmp2.notaryId\n" +
            "left join\n" +
            "(select notaryId, count(*) as notarizationSuccessCount, null as notarizationTotalMoney from\n" +
            "(SELECT notary.notaryId,evidence.notarizationStatus, evidence.notarizationMoney FROM \n" +
            "notary\n" +
            "left join evidence\n" +
            "on notary.notaryId = evidence.notaryId) as tmp\n" +
            "where notarizationStatus='3'\n" +
            "group by notaryId) as tmp3\n" +
            "on tmp2.notaryId = tmp3.notaryId\n" +
            "left join\n" +
            "(select notaryId, count(*) as notarizationFailCount from\n" +
            "(SELECT notary.notaryId,evidence.notarizationStatus FROM \n" +
            "notary\n" +
            "left join evidence\n" +
            "on notary.notaryId = evidence.notaryId) as tmp\n" +
            "where notarizationStatus='4'\n" +
            "group by notaryId) as tmp4\n" +
            "on tmp2.notaryId = tmp4.notaryId;")
    void notayStatisticsGen(@Param("timeFlag") String timeFlag);

    //公证员排名生成
    @Select("SELECT * FROM notary_statistics where timestamp(timeFlag)=#{timeFlag} order by ${sort} desc;")
    List<NotaryStatisticsEntity> rankBytimeFlagAndsort(@Param("timeFlag") String timeFlag, @Param("sort") String sort);

    //公证金额获取
    @Select("SELECT notary.notaryId, evidence.notarizationMoney FROM \n" +
            "notary\n" +
            "left join evidence\n" +
            "on notary.notaryId = evidence.notaryId\n" +
            "where notarizationStatus='3';")
    List<EvidenceEntity> getNotarizationMoney();

    //公证员金额写入
    @Update("update notary_statistics\n" +
            "set notarizationTotalMoney=#{notarizationTotalMoney}\n" +
            "where notaryStatisticsId is not null and notstyId=#{notstyId} and timestamp(timeFlag)=#{timeFlag};")
    void setNotarizationTotalMoney(@Param("notstyId")String notstyId, @Param("notarizationTotalMoney")String notarizationTotalMoney, @Param("timeFlag") String timeFlag);

    //写入密文测试
    @Update("update evidence\n" +
            "set notarizationMoney=#{notarizationMoney}\n" +
            "where evidenceId is not null and notaryId=#{notaryId};")
    void test(@Param("notaryId")String notaryId, @Param("notarizationMoney")String notarizationMoney);
}
