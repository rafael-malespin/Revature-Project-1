package com.dao;

import com.driver.MainDriver;
import com.model.Reimbursement;
import com.model.ReimbursementStatus;
import com.model.ReimbursementType;
import com.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDaoImpl implements ReimbursementDao{
    public static String url="jdbc:postgresql://"+System.getenv("TRAINING_DB_ENDPOINT")+"/"+System.getenv("TRAINING_DB_NAME");
    public static String username=System.getenv("TRAINING_DB_USERNAME");
    public static String password=System.getenv("TRAINING_DB_PASSWORD");

    final static Logger loggy = Logger.getLogger(MainDriver.class);
    static {
        loggy.setLevel(Level.ALL);
        //loggy.setLevel(Level.ERROR);
    }
    @Override
    public void setUrl(String url) {
        ReimbursementDaoImpl.url=url;
        loggy.info("Database url was changed to: " +url);
    }

    @Override
    public void setUsername(String user) {
        ReimbursementDaoImpl.username=user;
    }

    @Override
    public void setPassword(String password) {
        ReimbursementDaoImpl.password=password;
    }

    /** Will attempt to add a new reimbursement to the sql database using a Reimbursement containing the amount, submitTime,
     * authorID, Type, and pending status of the Reimbursement
     *
     * @param newReimbursement A Reimbursement contain information use to make a new Reimbursement in the database
     * @return a boolean indicating if the insertion of the new Reimbursement was successful
     */
    @Override
    public boolean insertNewReimbursement(Reimbursement newReimbursement) {
        int rowsInserted=0;
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "INSERT INTO ers_reimbursement (reimb_amount," +
                    "reimb_submitted," +
                    "reimb_description," +
                    "reimb_author," +
                    "reimb_status_id," +
                    "reimb_type_id)\n" +
                    "VALUES (?,?,?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setFloat(1,newReimbursement.getAmount());
            ps.setTimestamp(2,(Timestamp) newReimbursement.getSubmittedTime());
            ps.setString(3,newReimbursement.getDescription());
            ps.setInt(4,newReimbursement.getAuthorID());
            ps.setInt(5,newReimbursement.getStatus().getValue());
            ps.setInt(6,newReimbursement.getType().getValue());
            rowsInserted=ps.executeUpdate();

        }catch (SQLException e){
            loggy.error("ERROR when INSERTING a new reimbursement",e);
            e.printStackTrace();
            return false;
        }
        return rowsInserted != 0;
    }

    /** Retrieves all the Reimbursements currently contained within the database
     *
     * @return An ArrayList of all the Reimbursements contained within the database
     */
    @Override
    public List<Reimbursement> selectAllReimbursements() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT reimb_id,reimb_amount,reimb_submitted,reimb_resolved,reimb_description,reimb_author,reimb_resolver,rs.reim_status,rt.reim_type,eu.user_first_name,eu.user_last_name,\n" +
                    "eu2.user_first_name AS resolver_first_name,\n" +
                    "eu2.user_last_name AS resolver_last_name\n" +
                    "FROM ers_reimbursement r\n" +
                    "INNER JOIN reimbursement_status rs \n" +
                    "ON r.reimb_status_id =rs.reim_status_id\n" +
                    "INNER JOIN reimbursement_type rt \n" +
                    "ON r.reimb_type_id =rt.reim_type_id\n" +
                    "INNER JOIN ers_users eu\n" +
                    "ON r.reimb_author =eu.ers_users_id\n" +
                    "LEFT JOIN ers_users eu2\n" +
                    "ON r.reimb_resolver =eu2.ers_users_id\n" +
                    "ORDER BY r.reimb_submitted DESC,r.reimb_resolved DESC;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                reimbursements.add(reimbursementBuilderHelper(rs));
            }
            ps.executeQuery();

        }catch (SQLException e){
            loggy.error("ERROR when SELECTING ALL reimbursements",e);
            e.printStackTrace();
        }
        return reimbursements;
    }

    /** We retrieve all the Reimbursements from the database and filter them by the passed in Status
     *
     * @param filter the ReimbursementStatus we want to filter by
     * @return an ArrayList of Reimbursements whose Status match the filter
     */
    @Override
    public List<Reimbursement> selectAllReimbursementsFilteredByStatus(ReimbursementStatus filter) {
        List<Reimbursement> reimbursements = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT reimb_id,reimb_amount,reimb_submitted,reimb_resolved,reimb_description,reimb_author,reimb_resolver,rs.reim_status,rt.reim_type,eu.user_first_name,eu.user_last_name,\n" +
                    "eu2.user_first_name AS resolver_first_name,\n" +
                    "eu2.user_last_name AS resolver_last_name\n" +
                    "FROM ers_reimbursement r\n" +
                    "INNER JOIN reimbursement_status rs \n" +
                    "ON r.reimb_status_id =rs.reim_status_id\n" +
                    "INNER JOIN reimbursement_type rt \n" +
                    "ON r.reimb_type_id =rt.reim_type_id\n" +
                    "INNER JOIN ers_users eu\n" +
                    "ON r.reimb_author =eu.ers_users_id\n" +
                    "LEFT JOIN ers_users eu2\n" +
                    "ON r.reimb_resolver =eu2.ers_users_id\n" +
                    "WHERE rs.reim_status =?\n" +
                    "ORDER BY r.reimb_submitted DESC, r.reimb_resolved DESC;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,filter.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                reimbursements.add(
                        reimbursementBuilderHelper(rs)
                );
            }
            ps.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
            loggy.error("ERROR when SELECTING a reimbursements FILTERED BY STATUS",e);

        }
        return reimbursements;
    }

    /** Retrieves the Reimbursements created by the currentUser that are currently in the database
     *
     * @param currentUser user whose Reimbursements we want to retrieve
     * @return an ArrayList of Reimbursements created by currentUser
     */
    @Override
    public List<Reimbursement> selectAllReimbursementsForUser(User currentUser) {
        List<Reimbursement> reimbursements = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT reimb_id,reimb_amount,reimb_submitted,reimb_resolved,reimb_description,reimb_author,reimb_resolver,rs.reim_status,rt.reim_type,eu.user_first_name,eu.user_last_name,\n" +
                    "eu2.user_first_name AS resolver_first_name,\n" +
                    "eu2.user_last_name AS resolver_last_name\n" +
                    "FROM ers_reimbursement r\n" +
                    "INNER JOIN reimbursement_status rs \n" +
                    "ON r.reimb_status_id =rs.reim_status_id\n" +
                    "INNER JOIN reimbursement_type rt \n" +
                    "ON r.reimb_type_id =rt.reim_type_id\n" +
                    "INNER JOIN ers_users eu\n" +
                    "ON r.reimb_author =eu.ers_users_id\n" +
                    "LEFT JOIN ers_users eu2\n" +
                    "ON r.reimb_resolver =eu2.ers_users_id\n" +
                    "WHERE \n" +
                    "r.reimb_author =?\n" +
                    "ORDER BY r.reimb_submitted DESC,r.reimb_resolved DESC;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,currentUser.getUserID());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                reimbursements.add(
                        reimbursementBuilderHelper(rs)
                );
            }
            ps.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
            loggy.error("ERROR when SELECTING a reimbursements FILTERED by USER",e);

        }
        return reimbursements;
    }

    /** Retrieves the Reimbursements from the database who were created by the passed in User and filtered by
     * the passed in ReimbursementStatus
     *
     * @param currentUser The user whose reimbursement we want to retrieve
     * @param filter the ReimbursementStatus that we want to filter the reimbursements by
     * @return An ArrayList containing the desired reimbursements
     */
    @Override
    public List<Reimbursement> selectAllReimbursementsForUserBasedOnStatus(User currentUser, ReimbursementStatus filter) {
        List<Reimbursement> reimbursements = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "SELECT reimb_id,reimb_amount,reimb_submitted,reimb_resolved,reimb_description,reimb_author,reimb_resolver,rs.reim_status,rt.reim_type,eu.user_first_name,eu.user_last_name,\n" +
                    "eu2.user_first_name AS resolver_first_name,\n" +
                    "eu2.user_last_name AS resolver_last_name\n" +
                    "FROM ers_reimbursement r\n" +
                    "INNER JOIN reimbursement_status rs \n" +
                    "ON r.reimb_status_id =rs.reim_status_id\n" +
                    "INNER JOIN reimbursement_type rt \n" +
                    "ON r.reimb_type_id =rt.reim_type_id\n" +
                    "INNER JOIN ers_users eu\n" +
                    "ON r.reimb_author =eu.ers_users_id\n" +
                    "LEFT JOIN ers_users eu2\n" +
                    "ON r.reimb_resolver =eu2.ers_users_id\n" +
                    "WHERE \n" +
                    "r.reimb_author =?\n" +
                    "AND rs.reim_status =?\n" +
                    "ORDER BY r.reimb_submitted DESC,r.reimb_resolved DESC;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,currentUser.getUserID());
            ps.setString(2,filter.toString());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                reimbursements.add(
                        reimbursementBuilderHelper(rs)
                );
            }
            ps.executeQuery();

        }catch (SQLException e){
            e.printStackTrace();
            loggy.error("ERROR when SELECTING a reimbursements FILTERED by USER and STATUS",e);

        }
        return reimbursements;
    }

    /**
     * Updates an entry of the reimbursement table in the sql database
     * @param reimbursement Reimbursement containing the information needed to update a reimbursement
     * @return a boolean if the update was successful
     */
    @Override
    public boolean updateReimbursementStatus(Reimbursement reimbursement) {
        int rowsAffected=0;
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            String sql = "UPDATE ers_reimbursement SET reimb_status_id = ?,reimb_resolver =?,REIMB_RESOLVED = ? WHERE reimb_id =?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,reimbursement.getStatus().getValue());
            ps.setInt(2,reimbursement.getResolverID());
            ps.setTimestamp(3,(Timestamp) reimbursement.getResolvedTime());
            ps.setInt(4,reimbursement.getReimbursementID());
            rowsAffected=ps.executeUpdate(); //hopefully updates the reimbursement
        }catch (SQLException e){
            e.printStackTrace();
            loggy.error("ERROR when UPDATING a reimbursement",e);
            return false;
        }
        return rowsAffected != 0;
    }

    @Override
    public void deleteReimbursement(Reimbursement formerReimbursement) {

    }

    /**
     * creates the reimbursement table and initial values for testing purposes
     */
    @Override
    public void h2InitDao() {
        try(Connection conn=
                    DriverManager.getConnection(url,username, password))
        {
            String sql= "CREATE TABLE ERS_REIMBURSEMENT(\n" +
                    "\tREIMB_ID SERIAL PRIMARY KEY,\n" +
                    "\tREIMB_AMOUNT NUMERIC NOT NULL,\n" +
                    "\tREIMB_SUBMITTED TIMESTAMP NOT NULL,\n" +
                    "\tREIMB_RESOLVED TIMESTAMP,\n" +
                    "\tREIMB_DESCRIPTION VARCHAR(250),\n" +
                    "\tREIMB_AUTHOR INTEGER NOT NULL,\n" +
                    "\tREIMB_RESOLVER INTEGER,\n" +
                    "\tREIMB_STATUS_ID INTEGER NOT NULL,\n" +
                    "\tREIMB_TYPE_ID INTEGER NOT NULL,\n" +
                    "\tFOREIGN KEY (REIMB_AUTHOR) REFERENCES ERS_USERS(ERS_USERS_ID),\n" +
                    "\tFOREIGN KEY (REIMB_RESOLVER) REFERENCES ERS_USERS(ERS_USERS_ID),\n" +
                    "\tFOREIGN KEY (REIMB_STATUS_ID) REFERENCES REIMBURSEMENT_STATUS(reim_status_id),\n" +
                    "\tFOREIGN KEY (REIMB_TYPE_ID) REFERENCES REIMBURSEMENT_TYPE(REIM_TYPE_ID)\n" +
                    ");\n" +
                    "INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)\n" +
                    "VALUES (300,CURRENT_TIMESTAMP,'Company dinner',1,1,3);\n" +
                    "INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id,reimb_resolver)\n" +
                    "VALUES (500,CURRENT_TIMESTAMP,'Bribe',1,2,4,3);\n" +
                    "INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id,reimb_resolver)\n" +
                    "VALUES (1000,CURRENT_TIMESTAMP,'Emergency',2,3,2,3);" +
                    "INSERT INTO ers_reimbursement (reimb_amount,reimb_submitted,reimb_description,reimb_author,reimb_status_id,reimb_type_id)\n" +
                    "VALUES (300,CURRENT_TIMESTAMP,'Airplane Ticket',2,1,2);";

            Statement state = conn.createStatement();
            state.execute(sql);
        }catch(SQLException e) {
            loggy.error("ERROR when initializing a H2 database in Reimbursement Dao",e);
            e.printStackTrace();
        }
    }

    /**
     * Destroys the reimbursement table from database
     */
    @Override
    public void h2DestroyDao() {
        try(Connection conn=
                    DriverManager.getConnection(url,username, password))
        {
            String sql= "DROP TABLE ers_reimbursement;";

            Statement state = conn.createStatement();
            state.execute(sql);
        }catch(SQLException e) {
            e.printStackTrace();
            loggy.error("ERROR when destroying a H2 database in Reimbursement Dao",e);

        }
    }

    /**
     * Builds a Reimbursement using a ResultSet
     * @param rs the ResultSet that may contain the information needed to make a Reimbursement Object
     * @return a new Reimbursement made from the ResultSet
     * @throws SQLException if any of the columns is invalid throws an exception
     */
    public Reimbursement reimbursementBuilderHelper(ResultSet rs) throws SQLException{
        if(rs.getString(12)==null){
            return new Reimbursement(rs.getInt(1),
                    rs.getFloat(2),
                    rs.getTimestamp(3),
                    rs.getTimestamp(4),
                    rs.getString(5),
                    rs.getInt(6),
                    rs.getInt(7),
                    ReimbursementStatus.getStatus(rs.getString(8)),
                    ReimbursementType.getType(rs.getString(9)),
                    rs.getString(10)+" "+rs.getString(11),
                    null
            );
        }
        else{
            return new Reimbursement(rs.getInt(1),
                    rs.getFloat(2),
                    rs.getTimestamp(3),
                    rs.getTimestamp(4),
                    rs.getString(5),
                    rs.getInt(6),
                    rs.getInt(7),
                    ReimbursementStatus.getStatus(rs.getString(8)),
                    ReimbursementType.getType(rs.getString(9)),
                    rs.getString(10)+" "+rs.getString(11),
                    rs.getString(12)+" "+rs.getString(13)
            );
        }
    }
}
