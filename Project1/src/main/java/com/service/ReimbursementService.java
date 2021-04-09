package com.service;

import com.model.Reimbursement;
import com.model.ReimbursementStatus;
import com.model.ReimbursementType;
import com.model.User;

import java.sql.Timestamp;
import java.util.List;

public interface ReimbursementService {

    public List<Reimbursement> getAllReimbursements();
    public List<Reimbursement> getReimbursementsByStatus(String statusName);
    public List<Reimbursement> getAllReimbursementsForUser(User user);
    public List<Reimbursement> getReimbursementsForUserByStatus(User user,String statusName);
    public boolean updateACertainReimbursement(Reimbursement updatedReimbursement);
    public boolean submitANewReimbursementRequest(Reimbursement newReimbursement);

}
