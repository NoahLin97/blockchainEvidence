package com.evidence.blockchainevidence.provider;

public class QueryProvider {

    //按公证所需参数查询
    public String selectEvidence(String evidenceId, String userId, String notaryId,
                                 String notarizationStatus, String notarizationType,
                                 String paymentStatus, String evidenceType, String organizationId,
                                 String evidenceNameWildcard, String usernameWildcard, String notarizationStartTimeStart,
                                 String notarizationStartTimeEnd, String notarizationEndTimeStart,
                                 String notarizationEndTimeEnd) {
        String sql = "select evidenceId,evidenceName,evidence.userId, username, evidence.notaryId,notaryName,evidence.organizationId,organizationName," +
                "blockchainTime,evidenceBlockchainId,evidenceTime,evidenceType,fileHash,filePath,fileSize," +
                "notarizationBlockchainIdEnd,notarizationBlockchainIdStart," +
                "notarizationEndTime,notarizationInformation,notarizationMatters,notarizationMoney,notarizationStartTime,notarizationStatus,evidence.notarizationType,transactionId, transactionStatus" +
                " from evidence, user, notary, organization " +
            "where evidence.userId=user.userId and evidence.notaryId=notary.notaryId and evidence.organizationId=organization.organizationId";
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
//        if(!evidenceNameWildcard.equals("none")){
//            sql+=" and evidenceName like '%"+evidenceNameWildcard+"%'";
//        }
        if(!usernameWildcard.equals("none")){
            sql+=" and username like '%"+usernameWildcard+"%'";
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
    //按存证所需参数查询
    public String selectEvidence2(String evidenceId, String userId, String usernameWildcard,
                                 String notarizationStatus, String evidenceBlockchainId,
                                 String evidenceType,
                                 String evidenceNameWildcard, String evidenceTimeStart,
                                 String evidenceTimeEnd, String blockchainTimeStart,
                                 String blockchainTimeEnd) {
        String sql = "select evidenceId,evidenceName,evidence.userId,username,evidence.notaryId,notaryName,evidence.organizationId,organizationName," +
                "blockchainTime,evidenceBlockchainId,evidenceTime,evidenceType,fileHash,filePath,fileSize," +
                "notarizationBlockchainIdEnd,notarizationBlockchainIdStart," +
                "notarizationEndTime,notarizationInformation,notarizationMatters,notarizationMoney,notarizationStartTime,notarizationStatus,evidence.notarizationType,transactionId, transactionStatus" +
                " from evidence, user, notary, organization " +
                "where evidence.userId=user.userId and evidence.notaryId=notary.notaryId and evidence.organizationId=organization.organizationId";
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

        if(!evidenceBlockchainId.equals("none")){
            sql+=" and evidenceBlockchainId=#{evidenceBlockchainId}";
        }


        if(!notarizationStatus.equals("none")){
            sql+=" and notarizationStatus=#{notarizationStatus}";
        }

        if(!evidenceType.equals("none")){
            sql+=" and evidenceType=#{evidenceType}";
        }

        //要通配的字符串
//        if(!evidenceNameWildcard.equals("none")){
//            sql+=" and evidenceName like '%"+evidenceNameWildcard+"%'";
//        }
        if(!usernameWildcard.equals("none")){
            sql+=" and username like '%"+usernameWildcard+"%'";
        }
        //要比大小的date
        if(!evidenceTimeStart.equals("none")){
            sql+=" and evidenceTime > #{evidenceTimeStart}";
        }

        if(!evidenceTimeEnd.equals("none")){
            sql+=" and evidenceTime < #{evidenceTimeEnd}";
        }

        if(!blockchainTimeStart.equals("none")){
            sql+=" and blockchainTime > #{blockchainTimeStart}";
        }

        if(!blockchainTimeEnd.equals("none")){
            sql+=" and blockchainTime < #{blockchainTimeEnd}";
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

    public String selectUser(String userId, String usernameWildcard, String phoneNumberWildcard,
                                    String emailWildcard, String sex) {
        String sql = "select *" +
                " from user " +
                "where 1 = 1 ";


        //直接筛选的

//        if(!transactionStatus.equals("none")){
//            sql+=" and transaction.transactionStatus=#{transactionStatus}";
//        }
        if(!userId.equals("none")){
            sql+=" and userId=#{userId}";
        }

        if(!sex.equals("none")){
            sql+=" and sex=#{sex}";
        }

        //要通配的字符串
        if(!usernameWildcard.equals("none")){
            sql+=" and username like '%"+usernameWildcard+"%'";
        }

        if(!phoneNumberWildcard.equals("none")){
            sql+=" and phoneNumber like '%"+phoneNumberWildcard+"%'";
        }

        if(!emailWildcard.equals("none")){
            sql+=" and email like '%"+emailWildcard+"%'";
        }

        //要比大小的date

        //要比大小的明文



        return sql;
    }

    public String selectOrganization(String organizationId , String organizationIdNameWildcard, String addressWildcard,
                             String phoneNumberWildcard, String legalPeopleWildcard, String emailWildcard) {
        String sql = "select *" +
                " from organization " +
                "where 1 = 1 ";


        //直接筛选的

//        if(!transactionStatus.equals("none")){
//            sql+=" and transaction.transactionStatus=#{transactionStatus}";
//        }
        if(!organizationId.equals("none")){
            sql+=" and organizationId=#{organizationId}";
        }

        //要通配的字符串
        if(!organizationIdNameWildcard.equals("none")){
            sql+=" and organizationIdName '%"+organizationIdNameWildcard+"%'";
        }

        if(!addressWildcard.equals("none")){
            sql+=" and address '%"+addressWildcard+"%'";
        }

        if(!phoneNumberWildcard.equals("none")){
            sql+=" and phoneNumber '%"+phoneNumberWildcard+"%'";
        }
        if(!legalPeopleWildcard.equals("none")){
            sql+=" and legalPeople '%"+legalPeopleWildcard+"%'";
        }

        if(!emailWildcard.equals("none")){
            sql+=" and email '%"+emailWildcard+"%'";
        }

        //要比大小的date

        //要比大小的明文



        return sql;
    }

    public String selectNotary(String notaryId , String notaryNameWildcard, String phoneNumberWildcard, String jobNumberWildcard,
                                     String emailWildcard, String sex, String organizationId, String notarizationType ) {
        String sql = "select notaryId, notaryName, jobNumber, notary.phoneNumber, idCard, notary.email, sex, notary.organizationId, notarizationType, organizationName" +
                " from notary,organization " +
                "where notary.organizationId = organization.organizationId ";


        //直接筛选的

        if(!notaryId.equals("none")){
            sql+=" and notaryId=#{notaryId}";
        }
        if(!organizationId.equals("none")){
            sql+=" and organizationId=#{organizationId}";
        }

        if(!sex.equals("none")){
            sql+=" and sex=#{sex}";
        }
        if(!notarizationType.equals("none")){
            sql+=" and notarizationType=#{notarizationType}";
        }

        //要通配的字符串
        if(!notaryNameWildcard.equals("none")){
            sql+=" and notaryName '%"+notaryNameWildcard+"%'";
        }

        if(!jobNumberWildcard.equals("none")){
            sql+=" and jobNumber '%"+jobNumberWildcard+"%'";
        }

        if(!phoneNumberWildcard.equals("none")){
            sql+=" and phoneNumber '%"+phoneNumberWildcard+"%'";
        }
        if(!jobNumberWildcard.equals("none")){
            sql+=" and jobNumber '%"+jobNumberWildcard+"%'";
        }

        if(!emailWildcard.equals("none")){
            sql+=" and email '%"+emailWildcard+"%'";
        }

        //要比大小的date

        //要比大小的明文



        return sql;
    }

    public String selectAutman(String autManId , String autNameWildcard, String phoneNumberWildcard, String jobNumberWildcard,
                               String emailWildcard, String sex, String organizationId ) {
        String sql = "select notaryId, notaryName, jobNumber, phoneNumber, idCard, email, sex, aut_manager.organizationId, organizationName" +
                " from aut_manager,organization " +
                "where aut_manager.organizationId = organization.organizationId ";


        //直接筛选的

        if(!autManId.equals("none")){
            sql+=" and autManId=#{autManId}";
        }
        if(!organizationId.equals("none")){
            sql+=" and organizationId=#{organizationId}";
        }

        if(!sex.equals("none")){
            sql+=" and sex=#{sex}";
        }

        //要通配的字符串
        if(!autNameWildcard.equals("none")){
            sql+=" and autName '%"+autNameWildcard+"%'";
        }

        if(!jobNumberWildcard.equals("none")){
            sql+=" and jobNumber '%"+jobNumberWildcard+"%'";
        }

        if(!phoneNumberWildcard.equals("none")){
            sql+=" and phoneNumber '%"+phoneNumberWildcard+"%'";
        }
        if(!jobNumberWildcard.equals("none")){
            sql+=" and jobNumber '%"+jobNumberWildcard+"%'";
        }

        if(!emailWildcard.equals("none")){
            sql+=" and email '%"+emailWildcard+"%'";
        }

        //要比大小的date

        //要比大小的明文



        return sql;
    }

}
