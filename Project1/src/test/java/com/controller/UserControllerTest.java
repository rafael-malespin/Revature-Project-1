package com.controller;

import com.model.User;
import com.model.UserRole;
import com.service.UserService;
import com.service.UserServiceImpl;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    UserService userService;
    Javalin app;

    @BeforeEach
    void setUp() {
        app = Mockito.mock(Javalin.class);
        userService = Mockito.mock(UserServiceImpl.class);
        userController = new UserController(app,userService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void loginSuccessTest(){
        Context context = Mockito.mock(Context.class);
        User user = new User();
        user.setUsername("rafael123");
        user.setPassword("rafael123");
        Mockito.when(context.bodyAsClass(User.class)).thenReturn(user);
        User user1=new User(
                1,
                "rafael123",
                "rafael123",
                "Rafael",
                "Malespin",
                "rafael@yahoo.com",
                UserRole.EMPLOYEE
        );
        Mockito.when(userService.loginUser("rafael123","rafael123")).thenReturn(user1);
        userController.callingLoginUser(context);
        Mockito.verify(context,Mockito.times(1)).bodyAsClass(User.class);
        Mockito.verify(context,Mockito.times(1)).sessionAttribute("loggedInUser",user1);
        Mockito.verify(context,Mockito.times(1)).result("Logged In");
//        Mockito.verify(context,Mockito.times(1)).status(200);
    }

    @Test
    public void loginFailureTest(){
        Context context = Mockito.mock(Context.class);
        User user = new User();
        user.setUsername("rafael123");
        user.setPassword("rafael123");
        Mockito.when(context.bodyAsClass(User.class)).thenReturn(user);
        User user1=new User(
                1,
                "rafael123",
                "rafael123",
                "Rafael",
                "Malespin",
                "rafael@yahoo.com",
                UserRole.EMPLOYEE
        );
        Mockito.when(userService.loginUser("rafael123","rafael123")).thenReturn(null);
        userController.callingLoginUser(context);
        Mockito.verify(context,Mockito.times(1)).bodyAsClass(User.class);
        Mockito.verify(context,Mockito.times(1)).sessionAttribute("loggedInUser",null);
        Mockito.verify(context,Mockito.times(1)).result("Login failed");
//        Mockito.verify(context,Mockito.times(1)).status(401);
    }

    @Test
    public void getAllUsersTest(){
        Context context = Mockito.mock(Context.class);
        User user1=new User(
                1,
                "rafael123",
                "rafael123",
                "Rafael",
                "Malespin",
                "rafael@yahoo.com",
                UserRole.EMPLOYEE
        );
        List<User> users = new ArrayList<>();
        users.add(user1);
        Mockito.when(context.sessionAttribute("loggedInUser")).thenReturn(user1);
        Mockito.when(userService.getAllUsers()).thenReturn(users);
        userController.callingGetAllUsers(context);
        Mockito.verify(context,Mockito.times(1)).sessionAttribute("loggedInUser");
        Mockito.verify(context,Mockito.times(1)).json(users);
    }
    @Test
    public void getAllUsersFailureTest(){
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.sessionAttribute("loggedInUser")).thenReturn(null);
        userController.callingGetAllUsers(context);
        Mockito.verify(context,Mockito.times(1)).sessionAttribute("loggedInUser");
        Mockito.verify(context,Mockito.times(1)).result("Not Authorized");
    }

    @Test
    public void getLoggedInUserTest(){
        Context context = Mockito.mock(Context.class);
        User user1=new User(
                1,
                "rafael123",
                "rafael123",
                "Rafael",
                "Malespin",
                "rafael@yahoo.com",
                UserRole.EMPLOYEE
        );

        Mockito.when(context.sessionAttribute("loggedInUser")).thenReturn(user1);

        userController.callingGetLoginUser(context);
        Mockito.verify(context,Mockito.times(1)).sessionAttribute("loggedInUser");
        Mockito.verify(context,Mockito.times(1)).json(user1);
    }
    @Test
    public void getLoggedInFailureUserTest(){
        Context context = Mockito.mock(Context.class);
        User user1=new User(
                1,
                "rafael123",
                "rafael123",
                "Rafael",
                "Malespin",
                "rafael@yahoo.com",
                UserRole.EMPLOYEE
        );
        Mockito.when(context.sessionAttribute("loggedInUser")).thenReturn(null);
        userController.callingGetLoginUser(context);
        Mockito.verify(context,Mockito.times(1)).sessionAttribute("loggedInUser");
        Mockito.verify(context,Mockito.times(1)).result("No one is logged in");
//        Mockito.verify(context,Mockito.times(1)).status(404);
    }
    @Test
    public void logoutUserTest(){
        Context context = Mockito.mock(Context.class);
        User user1=new User(
                1,
                "rafael123",
                "rafael123",
                "Rafael",
                "Malespin",
                "rafael@yahoo.com",
                UserRole.EMPLOYEE
        );
        Mockito.when(context.sessionAttribute("loggedInUser")).thenReturn(user1);
        userController.callingLogout(context);
        Mockito.verify(context,Mockito.times(1)).sessionAttribute("loggedInUser");
        Mockito.verify(context,Mockito.times(1)).sessionAttribute("loggedInUser",null);
        Mockito.verify(context,Mockito.times(1)).result("You have been logged out.");
    }

}