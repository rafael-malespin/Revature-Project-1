package com.frontcontroller;

import com.model.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public class FrontController {
    Javalin app;
    Dispatcher dispatcher;

    /**Sets up the FrontController object.
     *
     * @param app The javalin object used to build the routes
     */
    public FrontController(Javalin app){
        this.app = app;
        app.before("*",FrontController::checkAllRequests);
        app.after("*",FrontController::corsFix);
        dispatcher=new Dispatcher(app);
    }

    /**
     * Checks if the user is logged in before allowing them access to any page other than the login page
     * @param context contains the path that will be used to check for a loggedInUser
     */
    public static void checkAllRequests(Context context) {
        //System.out.println("In front controller");


        //do not check kick them out if they are trying to login
        if (!context.path().equals("/api/users/login") && context.path().contains("api/")) {
            User user = context.sessionAttribute("loggedInUser");
            if (user == null) {
                throw new UnauthorizedResponse("Not Authorized");
            }
        }
    }

    /**
     * Sets the Cross Origin authorization
     * @param context used to set the header of the response
     */
    //AFTER
    public static void corsFix(Context context){
        context.header("Access-Control-Allow-Origin", "*");
    }
}
