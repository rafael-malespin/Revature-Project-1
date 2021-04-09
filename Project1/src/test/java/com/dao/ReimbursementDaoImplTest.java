package com.dao;

import com.model.Reimbursement;
import com.model.ReimbursementStatus;
import com.model.ReimbursementType;
import com.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReimbursementDaoImplTest {

    ReimbursementDao reimbursementDao;
    UserDao userDao;
    @BeforeEach
    void setUp() {
        String tempURL="jdbc:h2:C:\\Users\\rafae\\Desktop";
        reimbursementDao=new ReimbursementDaoImpl();
        reimbursementDao.setUrl(tempURL);
        //reimbursementDao.setUsername("sa");
        //reimbursementDao.setPassword("sa");

        userDao=new UserDaoImpl();
        userDao.setURL(tempURL);
        //userDao.setUsername("sa");
        //userDao.setPassword("sa");

        userDao.h2InitDao();
        reimbursementDao.h2InitDao();
    }

    @AfterEach
    void tearDown() {
        reimbursementDao.h2DestroyDao();
        userDao.h2DestroyDao();
        reimbursementDao=null;
        userDao=null;
    }

    @Test
    public void testSelectReimbursement(){
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(new Reimbursement(1,300,new Timestamp(System.currentTimeMillis()),null,"Company dinner",1,0, ReimbursementStatus.PENDING, ReimbursementType.FOOD,"Rafael Malespin",null));

        List<Reimbursement> retrievedRecords=reimbursementDao.selectAllReimbursements();
        assertAll(
                ()->assertEquals(4,retrievedRecords.size()),
                ()->assertEquals(reimbursements.get(0).getReimbursementID(),retrievedRecords.get(3).getReimbursementID()),
                ()->assertEquals(reimbursements.get(0).getAmount(),retrievedRecords.get(3).getAmount()),
                ()->assertEquals(reimbursements.get(0).getResolvedTime(),retrievedRecords.get(3).getResolvedTime()),
                ()->assertEquals(reimbursements.get(0).getDescription(),retrievedRecords.get(3).getDescription()),
                ()->assertEquals(reimbursements.get(0).getAuthorID(),retrievedRecords.get(3).getAuthorID()),
                ()->assertEquals(reimbursements.get(0).getStatus(),retrievedRecords.get(3).getStatus()),
                ()->assertEquals(reimbursements.get(0).getType(),retrievedRecords.get(3).getType())
        );
    }

    @Test
    public void testSelectReimbursementOnStatus(){
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(new Reimbursement(3,1000,new Timestamp(System.currentTimeMillis()),null,"Emergency",2,3, ReimbursementStatus.DENIED, ReimbursementType.TRAVEL,"Dani Malespin","Gwen Levin"));
        List<Reimbursement> retrievedRecords = reimbursementDao.selectAllReimbursementsFilteredByStatus(ReimbursementStatus.DENIED);
        assertAll(
                ()->assertEquals(1,retrievedRecords.size()),
                ()->assertEquals(reimbursements.get(0).getReimbursementID(),retrievedRecords.get(0).getReimbursementID()),
                ()->assertEquals(reimbursements.get(0).getAmount(),retrievedRecords.get(0).getAmount()),
                ()->assertEquals(reimbursements.get(0).getResolvedTime(),retrievedRecords.get(0).getResolvedTime()),
                ()->assertEquals(reimbursements.get(0).getDescription(),retrievedRecords.get(0).getDescription()),
                ()->assertEquals(reimbursements.get(0).getAuthorID(),retrievedRecords.get(0).getAuthorID()),
                ()->assertEquals(reimbursements.get(0).getStatus(),retrievedRecords.get(0).getStatus()),
                ()->assertEquals(reimbursements.get(0).getType(),retrievedRecords.get(0).getType()),
                ()->assertEquals(reimbursements.get(0).getAuthorName(),retrievedRecords.get(0).getAuthorName()),
                ()->assertEquals(reimbursements.get(0).getResolverName(),retrievedRecords.get(0).getResolverName())
        );
    }

    @Test
    public void testSelectReimbursementOnUser(){
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(new Reimbursement(3,1000,new Timestamp(System.currentTimeMillis()),null,"Emergency",2,3, ReimbursementStatus.DENIED, ReimbursementType.TRAVEL,"Dani Malespin","Gwen Levin"));
        User user =new User();
        user.setUserID(2);
        List<Reimbursement> retrievedRecords = reimbursementDao.selectAllReimbursementsForUser(user);
        assertAll(
                ()->assertEquals(2,retrievedRecords.size()),
                ()->assertEquals(reimbursements.get(0).getReimbursementID(),retrievedRecords.get(0).getReimbursementID()),
                ()->assertEquals(reimbursements.get(0).getAmount(),retrievedRecords.get(0).getAmount()),
                ()->assertEquals(reimbursements.get(0).getResolvedTime(),retrievedRecords.get(0).getResolvedTime()),
                ()->assertEquals(reimbursements.get(0).getDescription(),retrievedRecords.get(0).getDescription()),
                ()->assertEquals(reimbursements.get(0).getAuthorID(),retrievedRecords.get(0).getAuthorID()),
                ()->assertEquals(reimbursements.get(0).getStatus(),retrievedRecords.get(0).getStatus()),
                ()->assertEquals(reimbursements.get(0).getType(),retrievedRecords.get(0).getType()),
                ()->assertEquals(reimbursements.get(0).getAuthorName(),retrievedRecords.get(0).getAuthorName()),
                ()->assertEquals(reimbursements.get(0).getResolverName(),retrievedRecords.get(0).getResolverName())
        );
    }
    @Test
    public void testSelectReimbursementOnUserAndStatus(){
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(new Reimbursement(3,1000,new Timestamp(System.currentTimeMillis()),null,"Emergency",2,0, ReimbursementStatus.DENIED, ReimbursementType.TRAVEL,"Dani Malespin",null));
        User user =new User();
        user.setUserID(2);
        List<Reimbursement> retrievedRecords = reimbursementDao.selectAllReimbursementsForUserBasedOnStatus(user, ReimbursementStatus.DENIED);
        assertAll(
                ()->assertEquals(1,retrievedRecords.size()),
                ()->assertEquals(reimbursements.get(0).getReimbursementID(),retrievedRecords.get(0).getReimbursementID()),
                ()->assertEquals(reimbursements.get(0).getAmount(),retrievedRecords.get(0).getAmount()),
                ()->assertEquals(reimbursements.get(0).getResolvedTime(),retrievedRecords.get(0).getResolvedTime()),
                ()->assertEquals(reimbursements.get(0).getDescription(),retrievedRecords.get(0).getDescription()),
                ()->assertEquals(reimbursements.get(0).getAuthorID(),retrievedRecords.get(0).getAuthorID()),
                ()->assertEquals(reimbursements.get(0).getStatus(),retrievedRecords.get(0).getStatus()),
                ()->assertEquals(reimbursements.get(0).getType(),retrievedRecords.get(0).getType())
        );
    }

    @Test
    public void testUpdateReimbursement1(){
        Reimbursement newReimbursement=new Reimbursement();
        newReimbursement.setReimbursementID(1);
        newReimbursement.setAuthorID(1);
        newReimbursement.setStatus(ReimbursementStatus.APPROVED);
        newReimbursement.setResolvedTime(new Timestamp(System.currentTimeMillis()));
        newReimbursement.setResolverID(3);
        reimbursementDao.updateReimbursementStatus(newReimbursement);
        List<Reimbursement> retrievedRecords = reimbursementDao.selectAllReimbursementsFilteredByStatus(ReimbursementStatus.APPROVED);
        assertAll(
                ()->assertEquals(2,retrievedRecords.size()),
                ()->assertEquals(1,retrievedRecords.get(1).getReimbursementID()),
                ()->assertEquals(300,retrievedRecords.get(1).getAmount()),
                ()->assertNotNull(retrievedRecords.get(1).getResolvedTime()),
                ()->assertEquals("Company dinner",retrievedRecords.get(1).getDescription()),
                ()->assertEquals(1,retrievedRecords.get(1).getAuthorID()),
                ()->assertEquals(ReimbursementStatus.APPROVED,retrievedRecords.get(1).getStatus()),
                ()->assertEquals(ReimbursementType.FOOD,retrievedRecords.get(1).getType())
        );
    }

    @Test
    public void testUpdateReimbursement2(){
        Reimbursement newReimbursement=new Reimbursement();
        newReimbursement.setReimbursementID(1);
        newReimbursement.setAuthorID(1);
        newReimbursement.setStatus(ReimbursementStatus.DENIED);
        newReimbursement.setResolvedTime(new Timestamp(System.currentTimeMillis()));
        newReimbursement.setResolverID(3);
        reimbursementDao.updateReimbursementStatus(newReimbursement);
        List<Reimbursement> retrievedRecords = reimbursementDao.selectAllReimbursementsFilteredByStatus(ReimbursementStatus.DENIED);
        assertAll(
                ()->assertEquals(2,retrievedRecords.size()),
                ()->assertEquals(1,retrievedRecords.get(1).getReimbursementID()),
                ()->assertEquals(300,retrievedRecords.get(1).getAmount()),
                ()->assertNotNull(retrievedRecords.get(1).getResolvedTime()),
                ()->assertEquals("Company dinner",retrievedRecords.get(1).getDescription()),
                ()->assertEquals(1,retrievedRecords.get(1).getAuthorID()),
                ()->assertEquals(ReimbursementStatus.DENIED,retrievedRecords.get(1).getStatus()),
                ()->assertEquals(ReimbursementType.FOOD,retrievedRecords.get(1).getType())
        );
    }
}