package com.service;

import com.dao.ReimbursementDao;
import com.dao.ReimbursementDaoImpl;
import com.model.Reimbursement;
import com.model.ReimbursementStatus;
import com.model.User;

import java.util.List;

public class ReimbursementServiceImpl implements ReimbursementService {
    ReimbursementDao reimbursementDao;

    public ReimbursementServiceImpl() {
        this.reimbursementDao = new ReimbursementDaoImpl();
    }
    public ReimbursementServiceImpl(ReimbursementDao reimbursementDao) {
        this.reimbursementDao = reimbursementDao;
    }

    /**
     * Retrieves all the reimbursements from the database by calling reimbursementDao.selectAllReimbursements()
     * @return List of Reimbursements
     */
    @Override
    public List<Reimbursement> getAllReimbursements() {
        return reimbursementDao.selectAllReimbursements();
    }

    /**
     * Retrieves all the reimbursements from the database and filters by status by calling
     * reimbursementDao.selectAllReimbursementsFilteredByStatus() and passing the statusName to it.
     * @param statusName the status that will be used to filter the reimbursements
     * @return List of Reimbursements
     */
    @Override
    public List<Reimbursement> getReimbursementsByStatus(String statusName) {
        ReimbursementStatus selectedStatus=ReimbursementStatus.getStatus(statusName);
        return reimbursementDao.selectAllReimbursementsFilteredByStatus(selectedStatus);
    }

    /**
     * Retrieves all the reimbursements that has ever been created by the user
     * @param user The user whose reimbursements will be retrieved
     * @return List of Reimbursements created by user
     */
    @Override
    public List<Reimbursement> getAllReimbursementsForUser(User user) {

        return reimbursementDao.selectAllReimbursementsForUser(user);
    }

    /**
     * Retrieves a list of reimbursements from the Dao layer created by a user filtered by the passed in status
     * @param user the user whose reimbursement we want
     * @param statusName the desired category of Reimbursements
     * @return an ArrayList of Reimbursements filtered by user and current status
     */
    @Override
    public List<Reimbursement> getReimbursementsForUserByStatus(User user, String statusName) {
        ReimbursementStatus selectedStatus = ReimbursementStatus.getStatus(statusName);
        return reimbursementDao.selectAllReimbursementsForUserBasedOnStatus(user,selectedStatus);
    }

    /**
     * Updates a Reimbursement in the database by passing a Reimbursement with its ID, resolvedTime, resolverID, and new status
     * which is used to identity which reimbursement to update. if the Status is Pending it will return a false to indicate
     * that the update did not go through
     * @param updatedReimbursement the updated local Reimbursement that will be passed to the dao
     * @return a boolean dictating whether the dao was able to update the reimbursement
     */
    @Override
    public boolean updateACertainReimbursement(Reimbursement updatedReimbursement) {
        if(updatedReimbursement.getStatus()==ReimbursementStatus.PENDING){
            return false;
        }
        return reimbursementDao.updateReimbursementStatus(updatedReimbursement);
    }

    /**
     * Insert a Reimbursement in the database by passing a Reimbursement with its amount, submitTime, authorID,
     * type and status.
     *
     * @param newReimbursement the new local Reimbursement that will be passed to the dao and added to the database
     * @return a boolean dictating whether the dao was able to insert the reimbursement
     */
    @Override
    public boolean submitANewReimbursementRequest(Reimbursement newReimbursement) {
        newReimbursement.setStatus(ReimbursementStatus.PENDING);
        return reimbursementDao.insertNewReimbursement(newReimbursement);
    }
}
