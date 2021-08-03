package com.evidence.blockchainevidence.controller;

import com.evidence.blockchainevidence.mapper.NotaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NotaryController {


    @Autowired(required = false)
    NotaryMapper notaryMapper;




}




