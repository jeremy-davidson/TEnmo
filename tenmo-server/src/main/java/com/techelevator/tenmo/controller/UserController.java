package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@PreAuthorize("isAuthenticated()")
@RestController
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping(value = "/user")
    public User[] getAllUsers() {
        User[] users = userDao.findAll().toArray(new User[0]);
        for(User u:users){
            u.setPassword(""); //Don't send the password hash back to client.
        }
        return userDao.findAll().toArray(new User[0]);
    }
}
