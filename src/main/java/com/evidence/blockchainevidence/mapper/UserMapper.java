package com.evidence.blockchainevidence.mapper;

import com.evidence.blockchainevidence.entity.User;
import com.evidence.blockchainevidence.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    @Select("select * from user")
    List<UserEntity> findAll();

}
