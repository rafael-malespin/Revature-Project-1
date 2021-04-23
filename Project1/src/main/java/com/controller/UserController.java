package com.controller;

import com.driver.MainDriver;
import com.model.User;
import com.service.UserService;
import com.service.UserServiceImpl;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * The UserController class is a class that will be used to connect to the Service layer objects and contains
 * the methods called by the Dispatcher when User based routes are selected by the user.
 */
public class UserController {
    Javalin app;
    static UserService userService;

    final static Logger loggy = Logger.getLogger(MainDriver.class);
    static {
        loggy.setLevel(Level.ALL);
        //loggy.setLevel(Level.ERROR);
    }

    /**
     *
     * @param app Javalin instance that will be used to connect to the application
     */
    public UserController(Javalin app){
        this.app =app;
        userService = new UserServiceImpl();
    }
    public UserController(Javalin app,UserService userService){
        this.app =app;
        this.userService = userService;
    }

    /**
     * Retrieves all the Users currently in the connected database
     * @param context the Context object that will be used to send responses back to the client server
     */
    public static void getAllUsers(Context context){
        User user=context.sessionAttribute("loggedInUser");
        if(user==null){
            context.result("Not Authorized");
            return;
        }
        context.json(userService.getAllUsers());
        loggy.info("User: "+user.getUserID()+" RETRIEVED a list of ALL USERS in database");
    }

    /** Uses a username and password received from the user to gain a particular User from the UserService layer.
     * Once received, the retrieved User is saved in the context.sessionAttribute with the key "loggedInUser" to
     * be retrieved for later use in the session.
     *
     * @param context Context object containing a json with a username and password and will be used to send
     *                responses to the client server
     */
    public static void loginUser(Context context){
        User user1 = context.bodyAsClass(User.class);
        User user = userService.loginUser(user1.getUsername(),user1.getPassword());
        context.sessionAttribute("loggedInUser",user);
        if(user!=null){
            context.result("Logged In");
            loggy.info("User: "+user.getUserID()+" SUCCEEDED in LOGGING IN.");
        }
        else{
            loggy.warn("There was a failed ATTEMPT to login.");
            context.result("Login failed");
            //context.status(401);
        }
    }

    /** Retrieves the currently logged in User for the client server
     *
     * @param context used to send a json containing attributes of the currently logged in User to the client server.
     *                Will send an error message if no one is logged in.
     */
    public static void getLoggedInUser(Context context){
        User user=context.sessionAttribute("loggedInUser");
        if(user==null){
            context.result("No one is logged in");
            //context.status(404);
            loggy.warn("A client attempted to access server without logging in.");
        }
        else {
            context.json(user);
        }
    }

    /** Logs the user out of the session by making the key-value pair in sessionAttribute with key "loggedInUser" equal
     * to null
     *
     * @param context used to send a response to the client server
     */
    public static void logoutUser(Context context){
        User user = context.sessionAttribute("loggedInUser");
        if(user!=null) {
            loggy.info("User: " + user.getUserID() + " was LOGGED OUT.");
        }
        context.sessionAttribute("loggedInUser",null);
        context.result("You have been logged out.");
    }

    //Functions for testing use
    public void callingLoginUser(Context context){
        loginUser(context);
    }
    public void callingGetAllUsers(Context context){
        getAllUsers(context);
    }
    public void callingLogout(Context context){
        logoutUser(context);
    }
    public void callingGetLoginUser(Context context){
        getLoggedInUser(context);
    }
}
