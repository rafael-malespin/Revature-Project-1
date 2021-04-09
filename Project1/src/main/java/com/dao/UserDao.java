package com.dao;

import com.model.Reimbursement;
import com.model.User;

import java.util.List;

public interface UserDao {
    //to allow changing the aspects of the connection for testing
    public void setURL(String url);
    public void setUsername(String user);
    public void setPassword(String password);

    //CRUD Methods
    //READ
    public User selectAUser(String username,String password);
    public List<User> selectAllUsers();

    public void h2InitDao();
    public void h2DestroyDao();
}
