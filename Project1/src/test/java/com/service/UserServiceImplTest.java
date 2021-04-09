package com.service;

import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.model.User;
import com.model.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    UserService userService;
    UserDao userDao;

    @BeforeEach
    void setUp() {

        userDao = Mockito.mock(UserDaoImpl.class);
        userService = new UserServiceImpl(userDao);
    }

    @AfterEach
    void tearDown() {
        userService = null;
        userDao = null;
    }

    @Test
    void loginUserTest() {
        User user = new User(
                1,
                "rafael123",
                "rafael123",
                "Rafael",
                "Malespin",
                "rafael@yahoo.com",
                UserRole.EMPLOYEE
        );
        Mockito.when(userDao.selectAUser("rafael123","rafael123")).thenReturn(user);

        assertAll(
                ()->assertEquals(1,userService.loginUser("rafael123","rafael123").getUserID()),
                ()->assertEquals("rafael123",userService.loginUser("rafael123","rafael123").getUsername()),
                ()->assertEquals("rafael123",userService.loginUser("rafael123","rafael123").getPassword()),
                ()->assertEquals("Rafael",userService.loginUser("rafael123","rafael123").getFirstName()),
                ()->assertEquals("Malespin",userService.loginUser("rafael123","rafael123").getLastName()),
                ()->assertEquals("rafael@yahoo.com",userService.loginUser("rafael123","rafael123").getEmail()),
                ()->assertEquals(UserRole.EMPLOYEE,userService.loginUser("rafael123","rafael123").getRoleID())
        );
        Mockito.verify(userDao,Mockito.times(7)).selectAUser("rafael123","rafael123");
    }

    @Test
    void getAllUsersTest() {
        List<User> users =new ArrayList<>();
        users.add(new User(
                1,
                "rafael123",
                "rafael123",
                "Rafael",
                "Malespin",
                "rafael@yahoo.com",
                UserRole.EMPLOYEE
        ));
        users.add(new User(
                2,
                "dani",
                "fenton",
                "Danielle",
                "Fenton",
                "dani@gmail.com",
                UserRole.EMPLOYEE
        ));
        users.add(new User(
                3,
                "gwen",
                "levin",
                "Gwen",
                "Tennyson",
                "gwen10@hotmail.com",
                UserRole.FINANCE_MANAGER
        ));
        Mockito.when(userDao.selectAllUsers()).thenReturn(users);
        List<User> retrievedUsers=userService.getAllUsers();
        assertAll(
                ()->assertEquals(3,retrievedUsers.size()),
                ()->assertEquals(1,retrievedUsers.get(0).getUserID()),
                ()->assertEquals("rafael123",retrievedUsers.get(0).getUsername()),
                ()->assertEquals("rafael123",retrievedUsers.get(0).getPassword()),
                ()->assertEquals("Rafael",retrievedUsers.get(0).getFirstName()),
                ()->assertEquals("Malespin",retrievedUsers.get(0).getLastName()),
                ()->assertEquals("rafael@yahoo.com",retrievedUsers.get(0).getEmail()),
                ()->assertEquals(UserRole.EMPLOYEE,retrievedUsers.get(0).getRoleID()),
                ()->assertEquals(2,retrievedUsers.get(1).getUserID()),
                ()->assertEquals("dani",retrievedUsers.get(1).getUsername()),
                ()->assertEquals("fenton",retrievedUsers.get(1).getPassword()),
                ()->assertEquals("Danielle",retrievedUsers.get(1).getFirstName()),
                ()->assertEquals("Fenton",retrievedUsers.get(1).getLastName()),
                ()->assertEquals("dani@gmail.com",retrievedUsers.get(1).getEmail()),
                ()->assertEquals(UserRole.EMPLOYEE,retrievedUsers.get(1).getRoleID()),
                ()->assertEquals(3,retrievedUsers.get(2).getUserID()),
                ()->assertEquals("gwen",retrievedUsers.get(2).getUsername()),
                ()->assertEquals("levin",retrievedUsers.get(2).getPassword()),
                ()->assertEquals("Gwen",retrievedUsers.get(2).getFirstName()),
                ()->assertEquals("Tennyson",retrievedUsers.get(2).getLastName()),
                ()->assertEquals("gwen10@hotmail.com",retrievedUsers.get(2).getEmail()),
                ()->assertEquals(UserRole.FINANCE_MANAGER,retrievedUsers.get(2).getRoleID())
        );
        Mockito.verify(userDao,Mockito.times(1)).selectAllUsers();
    }
}