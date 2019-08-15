package com.vailter.standard.controller;

import com.vailter.standard.domain.UserVo;
import com.vailter.standard.exception.AppException;
import com.vailter.standard.ret.CodeMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("")
    public List<UserVo> getUserList() {
        List<UserVo> userVoList = new ArrayList<>(2);
        userVoList.add(UserVo.builder().id(1L).name("日拱一兵").age(18).build());
        userVoList.add(UserVo.builder().id(2L).name("tan").age(19).build());
        return userVoList;
    }

    @GetMapping("/{id}")
    public UserVo getUserByName(@PathVariable Long id) {
        return UserVo.builder().id(id).name("日拱一兵").age(18).build();
    }

    @GetMapping("/testResponseEntity")
    public ResponseEntity getUserByAge() {
        UserVo userVo = UserVo.builder().id(1L).name("日拱一兵").age(18).build();
        return new ResponseEntity(userVo, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/testNonRet")
    public void testNonRet() {
        System.out.println("testNonRet!");
    }

    @GetMapping("/testException")
    public ResponseEntity testException() {
        throw new AppException(CodeMsg.PERMISSION_CHECK_ERROR);
    }
}