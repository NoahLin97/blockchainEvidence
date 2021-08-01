package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.NotaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NotaryMapper {

    @Select("select * from notary")
    List<NotaryEntity> findAll();

}
