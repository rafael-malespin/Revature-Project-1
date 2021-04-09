package com.dao;

import com.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    public static String url="jdbc:postgresql:"+System.getenv("TRAINING_DB_ENDPOINT")+"/"+System.getenv("TRAINING_DB_NAME");
    public static String username=System.getenv("TRAINING_DB_USERNAME");
    public static String password=System.getenv("TRAINING_DB_PASSWORD");
    @Override
    public void setURL(String url) {
        UserDaoImpl.url=url;
    }

    @Override
    public void setUsername(String user) {
        UserDaoImpl.username=username;
    }

    @Override
    public void setPassword(String password) {
        UserDaoImpl.password=password;
    }

    /**
     *  Adds a new user to the database
     * @param newUser the user to be added to the database
     */
    public void createUser(User newUser) {
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "INSERT INTO ers_users (ers_username," +
                    "ers_password," +
                    "user_first_name," +
                    "user_last_name," +
                    "user_email," +
                    "user_role_id)\n" +
                    "VALUES (?,?,?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,newUser.getUsername());
            ps.setString(2,newUser.getPassword());
            ps.setString(3, newUser.getFirstName());
            ps.setString(4,newUser.getLastName());
            ps.setString(5, newUser.getEmail());
            ps.setInt(6,newUser.getRoleID().getValue());
            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * Uses the variables passed in to retrieve a single user from the database and create a User object to send back
     * to the calling function
     * @param userName the username of a user
     * @param passWord the password of a user
     * @return The user retrieved from the database; is null if none was found.
     */
    @Override
    public User selectAUser(String userName,String passWord) {
        User user = null;
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT ers_users_id,ers_username,ers_password,user_first_name,user_last_name,user_email,ur.user_role\n" +
                    "FROM ers_users eu\n" +
                    "INNER JOIN user_roles ur\n" +
                    "ON eu.user_role_id =ur.user_role_id\n" +
                    "WHERE ers_username = ? \n" +
                    "AND ers_password = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,userName);
            ps.setString(2,passWord);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                user = (
                        new User(rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6),
                                UserRole.getRole(rs.getString(7))
                        )
                );
            }

            ps.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Connects to the database and retrieves all of the columns and rows needed to build a List of all the Users
     * contained in the database
     * @return an ArrayList of Users
     */
    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT ers_users_id," +
                    "ers_username," +
                    "ers_password," +
                    "user_first_name," +
                    "user_last_name," +
                    "user_email," +
                    "ur.user_role\n" +
                    "FROM ers_users eu\n" +
                    "INNER JOIN user_roles ur\n" +
                    "ON eu.user_role_id =ur.user_role_id;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                users.add(
                    new User(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            UserRole.getRole(rs.getString(7))
                    )
                );
            }
            ps.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Builds the sql database for testing purposes. Builds the User table along with the UserRole,ReimbursementType,
     * and ReimbursementStatus lookup tables.
     */
    @Override
    public void h2InitDao() {
        try(Connection conn=
                    DriverManager.getConnection(url,username, password))
        {
            String sql= "--creating look up tables\n" +
                    "CREATE TABLE reimbursement_status(\n" +
                    "\treim_status_id integer PRIMARY KEY,\n" +
                    "\treim_status varchar(20) NOT null\n" +
                    ");\n" +
                    "INSERT INTO reimbursement_status VALUES (1,'PENDING');\n" +
                    "INSERT INTO reimbursement_status VALUES (2,'APPROVED');\n" +
                    "INSERT INTO reimbursement_status VALUES (3,'DENIED');\n" +
                    "\n" +
                    "CREATE TABLE reimbursement_type(\n" +
                    "\treim_type_id integer PRIMARY KEY,\n" +
                    "\treim_type varchar(20) NOT null\n" +
                    ");\n" +
                    "INSERT INTO reimbursement_type VALUES (1,'LODGING');\n" +
                    "INSERT INTO reimbursement_type VALUES (2,'TRAVEL');\n" +
                    "INSERT INTO reimbursement_type VALUES (3,'FOOD');\n" +
                    "INSERT INTO reimbursement_type VALUES (4,'OTHER');\n" +
                    "\n" +
                    "CREATE TABLE user_roles(\n" +
                    "\tuser_role_id integer PRIMARY KEY,\n" +
                    "\tuser_role varchar(20) NOT null\n" +
                    ");\n" +
                    "INSERT INTO user_roles VALUES (1,'EMPLOYEE');\n" +
                    "INSERT INTO user_roles VALUES (2,'FINANCE_MANAGER');\n" +
                    "--making the other tables\n" +
                    "CREATE TABLE ERS_USERS(\n" +
                    "\tERS_USERS_ID SERIAL PRIMARY KEY,\n" +
                    "\tERS_USERNAME VARCHAR(50) UNIQUE NOT NULL,\n" +
                    "\tERS_PASSWORD VARCHAR(50) NOT NULL,\n" +
                    "\tUSER_FIRST_NAME VARCHAR(100) NOT NULL,\n" +
                    "\tUSER_LAST_NAME VARCHAR(100) NOT NULL,\n" +
                    "\tUSER_EMAIL VARCHAR(150) UNIQUE NOT NULL,\n" +
                    "\tUSER_ROLE_ID INTEGER NOT NULL,\n" +
                    "\tFOREIGN KEY (USER_ROLE_ID) REFERENCES USER_ROLES(USER_ROLE_ID)\n" +
                    ");\n" +
                    "\n" +
                    "--inserting a user\n" +
                    "INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id)\n" +
                    "VALUES ('rafael123','rafael123','Rafael','Malespin','rafael@yahoo.com',1);\n" +
                    "INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id)\n" +
                    "VALUES ('dani321','dani321','Dani','Malespin','dani@yahoo.com',1);" +
                    "INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id)\n" +
                    "VALUES ('gwen11','gwen11','Gwen','Levin','gwen@yahoo.com',2);";

            Statement state = conn.createStatement();
            state.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Destroys the database tables created for testing
     */
    @Override
    public void h2DestroyDao() {
        try(Connection conn=
                    DriverManager.getConnection(url,username, password))
        {
            String sql= "DROP TABLE ers_users;\n" +
                    "DROP TABLE reimbursement_status;\n" +
                    "DROP TABLE reimbursement_type;\n" +
                    "DROP TABLE user_roles;";

            Statement state = conn.createStatement();
            state.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
