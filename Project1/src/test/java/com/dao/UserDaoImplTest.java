package com.dao;

import com.model.User;
import com.model.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {

    ReimbursementDao reimbursementDao;
    UserDao userDao;
    @BeforeEach
    void setUp() {
        String tempURL="jdbc:h2:C:\\Users\\rafae\\Desktop";
        reimbursementDao=new ReimbursementDaoImpl();
        reimbursementDao.setUrl(tempURL);
        //reimbursementDao.setUsername("sa");
        //reimbursementDao.setPassword("sa");

        userDao=new UserDaoImpl();
        userDao.setURL(tempURL);
        //userDao.setUsername("sa");
        //userDao.setPassword("sa");

        userDao.h2InitDao();
        reimbursementDao.h2InitDao();
    }

    @AfterEach
    void tearDown() {
        reimbursementDao.h2DestroyDao();
        userDao.h2DestroyDao();
        reimbursementDao=null;
        userDao=null;
    }

    @Test
    public void loginTest(){
        String userName="rafael123";
        String password="rafael123";
        User retrievedUser = userDao.selectAUser(userName,password);
        assertAll(
                ()-> assertNotNull(retrievedUser),
                ()-> assertEquals(userName,retrievedUser.getUsername()),
                ()-> assertEquals(password,retrievedUser.getPassword()),
                ()-> assertEquals(UserRole.EMPLOYEE,retrievedUser.getRoleID()),
                ()-> assertEquals(1,retrievedUser.getUserID()),
                ()-> assertEquals("Rafael",retrievedUser.getFirstName()),
                ()-> assertEquals("Malespin",retrievedUser.getLastName()),
                ()-> assertEquals("rafael@yahoo.com",retrievedUser.getEmail())
        );
    }
    @Test
    public void loginFailureTest(){
        assertAll(
                ()-> assertNull(userDao.selectAUser("Bobby","Bobby")),
                ()-> assertNull(userDao.selectAUser("dani321","notPass")),
                ()-> assertNull(userDao.selectAUser("dani543","dani321")),
                ()-> assertNull(userDao.selectAUser("dani321","")),
                ()-> assertNull(userDao.selectAUser("","")),
                ()-> assertNull(userDao.selectAUser("","dani321")),
                ()-> assertNotNull(userDao.selectAUser("dani321","dani321"))

        );
    }

    @Test
    public void retrieveAllUsersTest(){
        List<User> userList= userDao.selectAllUsers();
        System.out.println(userList);
        assertAll(
                ()-> assertNotNull(userList),
                ()-> assertEquals(3,userList.size()),
                ()-> assertEquals(1,userList.get(0).getUserID()),
                ()-> assertEquals("rafael123",userList.get(0).getUsername()),
                ()-> assertEquals("rafael123",userList.get(0).getPassword()),
                ()-> assertEquals("Rafael",userList.get(0).getFirstName()),
                ()-> assertEquals("Malespin",userList.get(0).getLastName()),
                ()-> assertEquals("rafael@yahoo.com",userList.get(0).getEmail()),
                ()-> assertEquals(UserRole.EMPLOYEE,userList.get(0).getRoleID())
        );
    }
}