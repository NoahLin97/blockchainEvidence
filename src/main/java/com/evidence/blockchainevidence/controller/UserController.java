package com.evidence.blockchainevidence.controller;

import com.evidence.blockchainevidence.entity.User;
import com.evidence.blockchainevidence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired(required = false)
    UserMapper userMapper;

    @RequestMapping("/user")
    public String userMapper(Model m){
        List<User> users = userMapper.findAll();
        m.addAttribute("user",users);
        return "user";
    }
}




