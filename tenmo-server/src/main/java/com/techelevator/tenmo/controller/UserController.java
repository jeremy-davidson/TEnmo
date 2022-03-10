package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping(value = "/user")
    public User[] getAllUsers(){
        return userDao.findAll().toArray(new User[0]);
    }
}
