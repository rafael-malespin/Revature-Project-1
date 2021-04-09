package com.frontcontroller;

import com.controller.ReimbursementController;
import com.controller.UserController;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;

/**
 * Creates routes that the client could access to conduct client-server interaction
 */
public class Dispatcher {
    ReimbursementController reimbursementController;
    UserController userController;

    public Dispatcher(Javalin app){
        reimbursementController= new ReimbursementController(app);
        userController = new UserController(app);

        //User Controllers
        app.routes(()->{
            path("/api/users",()->{
                get(UserController::getAllUsers);
                path("/login",()->{
                    post(UserController::loginUser);
                    get(UserController::getLoggedInUser);
                });
                path("/logout",()->{
                    get(UserController::logoutUser);
                });
            });
        });

        //Reimbursement Controllers
        app.routes(()->{
            path("/api/reimbursements",()->{
                get(ReimbursementController::getAllReimbursements);
                put(ReimbursementController::updateReimbursement);
                post(ReimbursementController::insertReimbursement);
                path("/:status",()->
                        get(ReimbursementController::getAllReimbursementsByStatus)
                );
            });
            path("/api/user-reimbursements/:user",()-> {
                get(ReimbursementController::getAllReimbursementsByUser);
                path("/:status", () ->
                        get(ReimbursementController::getAllReimbursementsByUserAndStatus)
                );
            });
        });
    }
}
