package com.dao;

import com.model.Reimbursement;
import com.model.ReimbursementStatus;
import com.model.User;

import java.util.List;

public interface ReimbursementDao {
    public void setUrl(String url);
    public void setUsername(String user);
    public void setPassword(String password);

    //CRUD METHODS
    //CREATE
    public boolean insertNewReimbursement(Reimbursement newReimbursement);

    //READ
    public List<Reimbursement> selectAllReimbursements();
    public List<Reimbursement> selectAllReimbursementsFilteredByStatus(ReimbursementStatus filter);
    public List<Reimbursement> selectAllReimbursementsForUser(User currentUser);
    public List<Reimbursement> selectAllReimbursementsForUserBasedOnStatus(User currentUser, ReimbursementStatus filter);

    //Update
    public boolean updateReimbursementStatus(Reimbursement reimbursement);

    //Delete
    public void deleteReimbursement(Reimbursement formerReimbursement);

    public void h2InitDao();
    public void h2DestroyDao();
}
