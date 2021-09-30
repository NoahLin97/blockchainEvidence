package com.evidence.blockchainevidence.mapper;


import com.evidence.blockchainevidence.entity.TransactionEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TransactionMapper {

    @Select("select * from transaction where transactionId = #{transactionId}")
    TransactionEntity selectByTransactionId(@Param("transactionId") String transactionId);

    @Insert("insert into transaction(transactionId,userId,userRemains,transactionMoney,transactionType)" +
            " values(#{transactionId},#{userId},#{userRemains},#{transactionMoney},#{transactionType})")
    int insertNotarTran(@Param("transactionId") String transactionId,@Param("userId") String userId,@Param("userRemains") String userRemains,
                        @Param("transactionMoney") String transactionMoney,@Param("transactionType") String transactionType);

    @Update("update transaction set transactionStatus = #{transactionStatus} where transactionId = #{transactionId}")
    int updateTranStatus(@Param("transactionStatus") String transactionStatus,@Param("transactionId") String transactionId);

    @Update("update transaction set userRemains = #{userRemains} where transactionId = #{transactionId}")
    int updateUserRemains(@Param("userRemains") String userRemains,@Param("transactionId") String transactionId);

    @Update("update transaction set transactionTime = #{transactionTime} where transactionId = #{transactionId}")
    int updateTranTime(@Param("transactionTime") String transactionTime,@Param("transactionId") String transactionId);

    @Update("update transaction set transactionPeople = #{transactionPeople} where transactionId = #{transactionId}")
    int updateTranPeople(@Param("transactionPeople") String transactionPeople,@Param("transactionId") String transactionId);

    @Update("update transaction set transactionBlockchainId = #{transactionBlockchainId} where transactionId = #{transactionId}")
    int updateTranBlockchainId(@Param("transactionBlockchainId") String transactionBlockchainId,@Param("transactionId") String transactionId);

    @Update("update transaction set blockchainTime = #{blockchainTime} where transactionId = #{transactionId}")
    int updateBlockchainTime(@Param("blockchainTime") String blockchainTime,@Param("transactionId") String transactionId);
}
