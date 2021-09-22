package com.evidence.blockchainevidence.service;


import com.evidence.blockchainevidence.entity.OrganizationEntity;
import com.evidence.blockchainevidence.mapper.OrganizationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    public int insertOrganization(String organizationId,String organizationName,String address,String phoneNumber,String email,String legalPeople){
        return organizationMapper.insertOrganization(organizationId,organizationName,address,phoneNumber,email,legalPeople);
    }

    public OrganizationEntity selectByOrganizationId(String organizationId){
        return organizationMapper.selectByOrganizationId(organizationId);
    }

}
