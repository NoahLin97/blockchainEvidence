package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.OrganizationStatisticsEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface OrganizationStatisticsMapper {
    @Select("select * from organization_statistics")
    List<OrganizationStatisticsEntity> findAll();

    //公证机构统计查询
    @Select("select * from organization_statistics where timestamp(timeFlag)=#{timeFlag}")
    List<OrganizationStatisticsEntity> selectBytimeFlag(@Param("timeFlag") String timeFlag);

    //公证机构统计生成
    @Insert("insert into organization_statistics(organizationStatisticsId ,organizationId, organizationName, notarizationCount, notarizationSuccessCount, notarizationTotalMoney, notstyCount, timeFlag)\n" +
            "select uuid() as organizationStatisticsId, tmp1.organizationId, organizationName, notarizationCount, notarizationSuccessCount, notarizationTotalMoney, notaryCount, now() as timeFlag from\n" +
            "(select organizationId, organizationName, count(notaryId) as notaryCount from (\n" +
            "SELECT notary.notaryId, notary.notaryName, organization.organizationId, organization.organizationName \n" +
            "FROM \n" +
            "notary \n" +
            "right join \n" +
            "organization \n" +
            "on notary.organizationId=organization.organizationId) as tmp \n" +
            "group by organizationId) as tmp1\n" +
            "left join\n" +
            "(select organizationId, count(notarizationStatus) as notarizationCount from(\n" +
            "SELECT organization.organizationId, evidence.notarizationStatus, evidence.notarizationMoney\n" +
            "FROM \n" +
            "evidence\n" +
            "right join organization\n" +
            "on evidence.organizationId=organization.organizationId) as tmp\n" +
            "where notarizationStatus<>'0' group by organizationId) as tmp2\n" +
            "on tmp1.organizationId=tmp2.organizationId\n" +
            "left join\n" +
            "(select organizationId, count(*) as notarizationSuccessCount, sum(notarizationMoney) as notarizationTotalMoney from (\n" +
            "SELECT organization.organizationId, evidence.notarizationStatus, evidence.notarizationMoney\n" +
            "FROM \n" +
            "evidence\n" +
            "right join organization\n" +
            "on evidence.organizationId=organization.organizationId) as tmp\n" +
            "where notarizationStatus='3' group by organizationId) as tmp3\n" +
            "on tmp2.organizationId=tmp3.organizationId;")
    void OrganizationStatisticsGen();
}
