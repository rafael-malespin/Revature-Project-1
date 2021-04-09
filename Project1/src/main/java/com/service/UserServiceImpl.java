package com.service;

import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.model.User;

import java.util.List;

public class UserServiceImpl implements UserService{
    UserDao userDao;
    public UserServiceImpl(){
        userDao=new UserDaoImpl();
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     *  Will retrieve the User whose username and password matches the ones that were passed in
     * @param username the potential username to be checked
     * @param password the potential password to be checked
     * @return A User whose username and password matched the ones passed in; will be null if not user matched the
     * username/password pair
     */
    @Override
    public User loginUser(String username, String password) {
        return userDao.selectAUser(username,password);
    }

    /**
     * Retrieves all of the Users contained in the database
     * @return ArrayList containing Users
     */
    @Override
    public List<User> getAllUsers() {
        return userDao.selectAllUsers();
    }
}
