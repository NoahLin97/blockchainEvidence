package com.evidence.blockchainevidence.service;


import com.evidence.blockchainevidence.entity.TransactionEntity;
import com.evidence.blockchainevidence.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    public TransactionEntity selectByTransactionId(String transactionId){
        return transactionMapper.selectByTransactionId(transactionId);
    }

    public int insertNotarTran(String transactionId,String userId,String userRemains,String transactionMoney,String transactionType){
        return transactionMapper.insertNotarTran(transactionId,userId,userRemains,transactionMoney,transactionType);
    }

    public int updateTranStatus(String transactionStatus,String transactionId){
        return transactionMapper.updateTranStatus(transactionStatus,transactionId);
    }

    public int updateUserRemains(String userRemains,String transactionId){
        return transactionMapper.updateUserRemains(userRemains,transactionId);
    }

    public int updateTranTime(String transactionTime,String transactionId){
        return transactionMapper.updateTranTime(transactionTime,transactionId);
    }

    public int updateTranPeople(String transactionPeople,String transactionId){
        return transactionMapper.updateTranPeople(transactionPeople,transactionId);
    }

    public int updateTranBlockchainId(String transactionBlockchainId,String transactionId){
        return transactionMapper.updateTranBlockchainId(transactionBlockchainId,transactionId);
    }

    public int updateBlockchainTime(String blockchainTime,String transactionId) {
        return transactionMapper.updateBlockchainTime(blockchainTime, transactionId);
    }

    public int updateStorageSize(String storageSize, String transactionId) {
        return transactionMapper.updateStorageSize(storageSize,transactionId);
    }
}
