package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.NotaryStatisticsEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    @Insert("insert into notary_statistics(notaryStatisticsId ,notstyId, notaryName, organizationName, notarizationCount, notarizationSuccessCount, notarizationTotalMoney, notarizationType, timeFlag)\n" +
            "select uuid() as notaryStatisticsId, tmp1.notaryId, notaryName, organizationName, notarizationCount, notarizationSuccessCount, notarizationTotalMoney, notarizationType, now() as timeFlag from\n" +
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
            "(select notaryId, count(*) as notarizationSuccessCount, sum(notarizationMoney) as notarizationTotalMoney from\n" +
            "(SELECT notary.notaryId,evidence.notarizationStatus, evidence.notarizationMoney FROM \n" +
            "notary\n" +
            "left join evidence\n" +
            "on notary.notaryId = evidence.notaryId) as tmp\n" +
            "where notarizationStatus='3'\n" +
            "group by notaryId) as tmp3\n" +
            "on tmp2.notaryId = tmp3.notaryId;")
    void notayStatisticsGen();

    //公证员排名生成
    @Select("SELECT * FROM notary_statistics where timestamp(timeFlag)=#{timeFlag} order by ${sort} desc;")
    List<NotaryStatisticsEntity> rankBytimeFlagAndsort(@Param("timeFlag") String timeFlag, @Param("sort") String sort);
}