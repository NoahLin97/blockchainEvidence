package com.evidence.blockchainevidence.provider;

import org.apache.ibatis.annotations.Param;

public class QueryProvider {

    public String selectEvidence(String evidenceId, String userId, String notaryId,
                                 String notarizationStatus, String notarizationType,
                                 String paymentStatus, String evidenceType, String organizationId,
                                 String evidenceNameWildcard, String notarizationStartTimeStart,
                                 String notarizationStartTimeEnd, String notarizationEndTimeStart,
                                 String notarizationEndTimeEnd) {
        String sql = "select evidenceId,evidenceName,userId,evidence.notaryId,notaryName,evidence.organizationId,organizationName," +
                "blockchainTime,evidenceBlockchainId,evidenceTime,evidenceType,fileHash,filePath,fileSize," +
                "notarizationBlockchainIdEnd,notarizationBlockchainIdStart," +
                "notarizationEndTime,notarizationInformation,notarizationMatters,notarizationMoney,notarizationStartTime,notarizationStatus,evidence.notarizationType,transactionId" +
                " from evidence, notary, organization " +
            "where evidence.notaryId=notary.notaryId and evidence.organizationId=organization.organizationId";
//        if (orgId != null) {
//            sql += " and ORG_ID=#{orgId}";
//        }
//        if (startDate != null) {
//            sql += " and DATA_DT>=#{startDate}";
//        }
//        if (endDate != null) {
//            sql += " and DATA_DT<=#{endDate}";
//        }
//        if (merId != null) {
//            sql += " and MER_ID=#{merId}";
//        }


        //直接筛选的

        if(!evidenceId.equals("none")){
            sql+=" and evidence.evidenceId=#{evidenceId}";
        }
        if(!userId.equals("none")){
            sql+=" and evidence.userId=#{userId}";
        }
        if(!notaryId.equals("none")){
            sql+=" and evidence.notaryId=#{notaryId}";
        }



        if(!notarizationStatus.equals("none")){
            sql+=" and notarizationStatus=#{notarizationStatus}";
        }
        if(!notarizationType.equals("none")){
            sql+=" and notarizationType=#{notarizationType}";
        }
        if(!paymentStatus.equals("none")){
            sql+=" and paymentStatus=#{paymentStatus}";
        }
        if(!evidenceType.equals("none")){
            sql+=" and evidenceType=#{evidenceType}";
        }
        if(!organizationId.equals("none")){
            sql+=" and organizationId=#{organizationId}";
        }
        //要通配的字符串
        if(!evidenceNameWildcard.equals("none")){
            sql+=" and evidenceName like '%"+evidenceNameWildcard+"%'";
        }

        //要比大小的date
        if(!notarizationStartTimeStart.equals("none")){
            sql+=" and notarizationStartTime > #{notarizationStartTimeStart}";
        }

        if(!notarizationStartTimeEnd.equals("none")){
            sql+=" and notarizationStartTime < #{notarizationStartTimeEnd}";
        }

        if(!notarizationEndTimeStart.equals("none")){
            sql+=" and notarizationEndTime > #{notarizationEndTimeStart}";
        }

        if(!notarizationEndTimeEnd.equals("none")){
            sql+=" and notarizationEndTime < #{notarizationEndTimeEnd}";
        }

        //要比大小的明文








        return sql;
    }



    public String selectTransaction(String userId, String transactionType, String usernameWildcard, String transactionTimeStart,
                                 String transactionTimeEnd) {
        String sql = "select transactionId,transaction.userId,userRemains,transactionMoney,transactionPeople,transactionType,storageSize," +
                "transactionTime,transactionBlockchainId,blockchainTime,user.username" +
                " from transaction, user " +
                "where transaction.userId = user.userId ";


        //直接筛选的

//        if(!transactionStatus.equals("none")){
//            sql+=" and transaction.transactionStatus=#{transactionStatus}";
//        }
        if(!userId.equals("none")){
            sql+=" and transaction.userId=#{userId}";
        }
        if(!transactionType.equals("none")){
            sql+=" and transaction.transactionType=#{transactionType}";
        }



        //要通配的字符串
        if(!usernameWildcard.equals("none")){
            sql+=" and username '%"+usernameWildcard+"%'";
        }

        //要比大小的date
        if(!transactionTimeStart.equals("none")){
            sql+=" and transactionTime > #{transactionTimeStart}";
        }

        if(!transactionTimeEnd.equals("none")){
            sql+=" and transactionTime < #{transactionTimeEnd}";
        }



        //要比大小的明文








        return sql;
    }


}
