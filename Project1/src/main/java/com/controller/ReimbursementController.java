package com.controller;

import com.driver.MainDriver;
import com.model.Reimbursement;
import com.model.User;
import com.model.UserRole;
import com.service.ReimbursementService;
import com.service.ReimbursementServiceImpl;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A class used for containing the methods involving Reimbursements
 */
public class ReimbursementController {

    Javalin app;
    private static ReimbursementService reimbursementService;
    final static Logger loggy = Logger.getLogger(MainDriver.class);
    static {
        loggy.setLevel(Level.ALL);
        //loggy.setLevel(Level.ERROR);
    }

    public ReimbursementController(Javalin app){
        this.app=app;
        reimbursementService=new ReimbursementServiceImpl();
    }

    /**
     * Once confirming the loggedInUser is a Finance Manager, will retrieve the reimbursements within the database
     * else it will send a response saying that the user is Not authorized.
     * @param context used for sending a json containing all of the reimbursement currently in the database
     */
    public static void getAllReimbursements(Context context){
        User user=context.sessionAttribute("loggedInUser");

        if(user!=null && user.getRoleID()==UserRole.FINANCE_MANAGER) {
            loggy.info("All reimbursements were sent to user: "+
                    user.getUserID()+
                    ": "+user.getFirstName()+" "+user.getLastName());
            context.json(reimbursementService.getAllReimbursements());
        }
        else{
            loggy.warn("An unauthorized user attempted to access the reimbursements.");
            context.result("Not authorized");
        }
    }

    /**
     *  Will get all the reimbursements from the database that has the same status that was sent as the pathParam status.
     *  If the user is not a finance manager will send a response stating Not authorized
     * @param context sends a json containing the reimbursements retrieved from database
     */
    public static void getAllReimbursementsByStatus(Context context){
        User user=context.sessionAttribute("loggedInUser");
        if(user!=null && user.getRoleID()==UserRole.FINANCE_MANAGER) {
            String statusToFilter = context.pathParam("status");
            context.json(
                    reimbursementService.getReimbursementsByStatus(statusToFilter)
            );
            loggy.info("All "+statusToFilter+" reimbursements were sent to user: "+
                    user.getUserID()+
                    ": "+user.getFirstName()+" "+user.getLastName());
        }
        else{
            context.result("Not authorized");
        }
    }

    /**
     *  Will get all the reimbursements from the database that was made by the same EMPLOYEE whose ID was sent
     *  as the pathParam user.
     *  If the user id is not valid, it will send a message "File not found"
     * @param context sends a json containing the reimbursements retrieved from database
     */
    public static void getAllReimbursementsByUser(Context context){
        User user=context.sessionAttribute("loggedInUser");
        if(user==null){
            context.result("Not Authorized");
            return;
        }
        String userToFilter=context.pathParam("user");
        int userID;
        try {
            userID = Integer.parseInt(userToFilter);
        }catch(Exception e){
            loggy.error("Bad user id received when requesting all of a user's reimbursement requests.",e);
            e.printStackTrace();
            context.result("File not found");
            return;
        }
        User userToBeSent = new User();
        userToBeSent.setUserID(userID);
        context.json(reimbursementService.getAllReimbursementsForUser(userToBeSent));
        loggy.info("All reimbursements for user: "+userID+" were sent to client server.");
    }

    /**
     *  Will get all the reimbursements from the database that was made by the EMPLOYEE whose ID was sent
     *  as the pathParam user and by the status that was sent as the pathParam.
     *  If the user id is not valid, it will send a message "File not found"
     * @param context sends a json containing the reimbursements retrieved from database
     */
    public static void getAllReimbursementsByUserAndStatus(Context context){
        User user=context.sessionAttribute("loggedInUser");
        if(user==null){
            context.result("Not Authorized");
            return;
        }
        String userToFilter=context.pathParam("user");
        int userID;
        try {
            userID = Integer.parseInt(userToFilter);
        }catch(Exception e){
            loggy.error("Bad user id received when requesting of user's reimbursement requests, filtered by status.",e);
            e.printStackTrace();
            context.result("File Not Found");
            return;
        }
        String statusToFilter=context.pathParam("status");
        User user1 = new User();
        user1.setUserID(userID);
        context.json(reimbursementService.getReimbursementsForUserByStatus(user1,statusToFilter));
        loggy.info("All "+statusToFilter+" reimbursements for user: "+userID+" were sent to client server.");
    }

    /**
     * Will update a reimbursement to be either Approved or Denied by sending it to the service layer
     * @param context contains information on a Reimbursement in the body
     */
    public static void updateReimbursement(Context context){
        User user=context.sessionAttribute("loggedInUser");
        if(user!=null && user.getRoleID()==UserRole.FINANCE_MANAGER) {
            Reimbursement newReimbursement = context.bodyAsClass(Reimbursement.class);
            boolean success = reimbursementService.updateACertainReimbursement(newReimbursement);
            if (success) {
                loggy.info("User: "+user.getUserID()+" SUCCEEDED in UPDATING a reimbursement request.");
                context.result("Reimbursement successfully updated.");
            } else {
                context.result("Reimbursement was not updated");
                loggy.warn("User: "+user.getUserID()+" FAILED in UPDATING a reimbursement request.");
            }
        }
        else{
            context.result("Not Authorized");
        }
    }

    /**
     * Inserts a new reimbursement to the database
     * @param context contains a new Reimbursement in body
     */
    public static void insertReimbursement(Context context){
        User user=context.sessionAttribute("loggedInUser");
        if(user==null){
            context.result("Not Authorized");
            return;
        }
        Reimbursement newReimbursement=context.bodyAsClass(Reimbursement.class);
        boolean success = reimbursementService.submitANewReimbursementRequest(
                newReimbursement);

        if(success){
            context.result("Reimbursement successfully submitted.");
            loggy.info("User: "+user.getUserID()+" SUCCEEDED in SUBMITTING a new reimbursement request.");
        }
        else{
            context.result("Reimbursement was not submitted successfully.");
            loggy.warn("User: "+user.getUserID()+" FAILED in SUBMITTING a new reimbursement request.");

        }
    }
}
