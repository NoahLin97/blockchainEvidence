package com.evidence.blockchainevidence.service;


import com.evidence.blockchainevidence.entity.EvidenceEntity;
import com.evidence.blockchainevidence.mapper.EvidenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvidenceService {

    @Autowired
    EvidenceMapper evidenceMapper;

    public EvidenceEntity selectByEvidenceId(String evidenceId){
        return evidenceMapper.selectByEvidenceId(evidenceId);
    }

    public int updateOrganIdAndNotarType(String organizationId,String notarizationType,String evidenceId){
        return evidenceMapper.updateOrganIdAndNotarType(organizationId,notarizationType,evidenceId);
    }

    public int updateNotarStatus(String notarizationStatus,String evidenceId){
        return evidenceMapper.updateNotarStatus(notarizationStatus,evidenceId);
    }

}
