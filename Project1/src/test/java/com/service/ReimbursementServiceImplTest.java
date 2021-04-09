package com.service;

import com.dao.ReimbursementDao;
import com.dao.ReimbursementDaoImpl;
import com.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReimbursementServiceImplTest {

    ReimbursementService reimbursementService;
    ReimbursementDao reimbursementDao;
    List<Reimbursement> reimbursements;
    List<Reimbursement> pending =new ArrayList<>();
    List<Reimbursement> approved =new ArrayList<>();
    List<Reimbursement> denied =new ArrayList<>();
    @BeforeEach
    void setUp() {
        reimbursementDao= Mockito.mock(ReimbursementDaoImpl.class);
        reimbursementService = new ReimbursementServiceImpl(reimbursementDao);
        reimbursements =new ArrayList<>();
        pending =new ArrayList<>();
        approved =new ArrayList<>();
        denied =new ArrayList<>();
        reimbursements.add(
                new Reimbursement(
                        1, 300, new Timestamp(1612202712), null,
                        "Company Dinner", 1, 0, ReimbursementStatus.PENDING, ReimbursementType.FOOD, "Rafael Malespin",
                        null
                )
        );
        reimbursements.add(
                new Reimbursement(
                        4, (float)(90.99), new Timestamp(1614636312), null,
                        "Holiday Inn 1 Night Stay", 2, 0, ReimbursementStatus.PENDING, ReimbursementType.LODGING, "Danielle Fenton",
                        "Gwen Tennyson"
                )
        );
        reimbursements.add(
                new Reimbursement(
                        2, (float)(1000.75), new Timestamp(1614636312), new Timestamp(1617250149),
                        "Airfare", 2, 3, ReimbursementStatus.APPROVED, ReimbursementType.TRAVEL, "Danielle Fenton",
                        "Gwen Tennyson"
                )
        );
        reimbursements.add(
                new Reimbursement(
                        3, 20_000, new Timestamp(1614981972), new Timestamp(1617250268),
                        "Collector's Edition Cat Burglar Nami action figure", 1, 3, ReimbursementStatus.DENIED, ReimbursementType.OTHER, "Rafael Malespin",
                        "Gwen Tennyson"
                )
        );
        pending.add(reimbursements.get(0));
        pending.add(reimbursements.get(1));
        approved.add(reimbursements.get(2));
        denied.add(reimbursements.get(3));

        reimbursementDao.h2InitDao();
    }

    @AfterEach
    void tearDown() {
        reimbursementDao.h2DestroyDao();
        reimbursementDao = null;
        reimbursementService = null;
    }

    @Test
    void getAllReimbursements() {

        Mockito.when(reimbursementDao.selectAllReimbursements()).thenReturn(reimbursements);
        List<Reimbursement> retrievedReimbursements = reimbursementService.getAllReimbursements();
        assertAll(
                ()->assertEquals(4,retrievedReimbursements.size()),
                ()->assertEquals(1,retrievedReimbursements.get(0).getReimbursementID()),
                ()->assertEquals(300,retrievedReimbursements.get(0).getAmount()),
                ()->assertEquals(new Timestamp(1612202712),retrievedReimbursements.get(0).getSubmittedTime()),
                ()-> assertNull(retrievedReimbursements.get(0).getResolvedTime()),
                ()->assertEquals("Company Dinner",retrievedReimbursements.get(0).getDescription()),
                ()->assertEquals(1,retrievedReimbursements.get(0).getAuthorID()),
                ()->assertEquals(0,retrievedReimbursements.get(0).getResolverID()),
                ()->assertEquals(ReimbursementStatus.PENDING,retrievedReimbursements.get(0).getStatus()),
                ()->assertEquals(ReimbursementType.FOOD,retrievedReimbursements.get(0).getType()),

                ()->assertEquals(2,retrievedReimbursements.get(2).getReimbursementID()),
                ()->assertEquals(1000.75,retrievedReimbursements.get(2).getAmount()),
                ()->assertEquals(new Timestamp(1614636312),retrievedReimbursements.get(2).getSubmittedTime()),
                ()->assertEquals(new Timestamp(1617250149),retrievedReimbursements.get(2).getResolvedTime()),
                ()->assertEquals("Airfare",retrievedReimbursements.get(2).getDescription()),
                ()->assertEquals(2,retrievedReimbursements.get(2).getAuthorID()),
                ()->assertEquals(3,retrievedReimbursements.get(2).getResolverID()),
                ()->assertEquals(ReimbursementStatus.APPROVED,retrievedReimbursements.get(2).getStatus()),
                ()->assertEquals(ReimbursementType.TRAVEL,retrievedReimbursements.get(2).getType()),

                ()->assertEquals(3,retrievedReimbursements.get(3).getReimbursementID()),
                ()->assertEquals(20_000,retrievedReimbursements.get(3).getAmount()),
                ()->assertEquals(new Timestamp(1614981972),retrievedReimbursements.get(3).getSubmittedTime()),
                ()-> assertEquals(new Timestamp(1617250268),retrievedReimbursements.get(3).getResolvedTime()),
                ()->assertEquals("Collector's Edition Cat Burglar Nami action figure",retrievedReimbursements.get(3).getDescription()),
                ()->assertEquals(1,retrievedReimbursements.get(3).getAuthorID()),
                ()->assertEquals(3,retrievedReimbursements.get(3).getResolverID()),
                ()->assertEquals(ReimbursementStatus.DENIED,retrievedReimbursements.get(3).getStatus()),
                ()->assertEquals(ReimbursementType.OTHER,retrievedReimbursements.get(3).getType())
        );


    }

    @Test
    void getReimbursementsByStatus() {
        Mockito.when(reimbursementDao.selectAllReimbursementsFilteredByStatus(ReimbursementStatus.PENDING)).thenReturn(pending);
        Mockito.when(reimbursementDao.selectAllReimbursementsFilteredByStatus(ReimbursementStatus.APPROVED)).thenReturn(approved);
        Mockito.when(reimbursementDao.selectAllReimbursementsFilteredByStatus(ReimbursementStatus.DENIED)).thenReturn(denied);

        List<Reimbursement> retrieved=reimbursementService.getReimbursementsByStatus("PENDING");
        assertAll(
                ()->assertEquals(2,retrieved.size()),
                ()->assertEquals(1,retrieved.get(0).getReimbursementID()),
                ()->assertEquals(300,retrieved.get(0).getAmount()),
                ()->assertEquals(new Timestamp(1612202712),retrieved.get(0).getSubmittedTime()),
                ()->assertNull(retrieved.get(0).getResolvedTime()),
                ()->assertEquals("Company Dinner",retrieved.get(0).getDescription()),
                ()->assertEquals(1,retrieved.get(0).getAuthorID()),
                ()->assertEquals(0,retrieved.get(0).getResolverID()),
                ()->assertEquals(ReimbursementStatus.PENDING,retrieved.get(0).getStatus()),
                ()->assertEquals(ReimbursementType.FOOD,retrieved.get(0).getType())
        );
        List<Reimbursement> retrieved2=reimbursementService.getReimbursementsByStatus("APPROVED");
        assertAll(
                ()->assertEquals(2,retrieved2.get(0).getReimbursementID()),
                ()->assertEquals(1000.75,retrieved2.get(0).getAmount()),
                ()->assertEquals(new Timestamp(1614636312),retrieved2.get(0).getSubmittedTime()),
                ()->assertEquals(new Timestamp(1617250149),retrieved2.get(0).getResolvedTime()),
                ()->assertEquals("Airfare",retrieved2.get(0).getDescription()),
                ()->assertEquals(2,retrieved2.get(0).getAuthorID()),
                ()->assertEquals(3,retrieved2.get(0).getResolverID()),
                ()->assertEquals(ReimbursementStatus.APPROVED,retrieved2.get(0).getStatus()),
                ()->assertEquals(ReimbursementType.TRAVEL,retrieved2.get(0).getType())
        );
        List<Reimbursement> retrieved3=reimbursementService.getReimbursementsByStatus("DENIED");
        assertAll(
                ()->assertEquals(3,retrieved3.get(0).getReimbursementID()),
                ()->assertEquals(20_000,retrieved3.get(0).getAmount()),
                ()->assertEquals(new Timestamp(1614981972),retrieved3.get(0).getSubmittedTime()),
                ()-> assertEquals(new Timestamp(1617250268),retrieved3.get(0).getResolvedTime()),
                ()->assertEquals("Collector's Edition Cat Burglar Nami action figure",retrieved3.get(0).getDescription()),
                ()->assertEquals(1,retrieved3.get(0).getAuthorID()),
                ()->assertEquals(3,retrieved3.get(0).getResolverID()),
                ()->assertEquals(ReimbursementStatus.DENIED,retrieved3.get(0).getStatus()),
                ()->assertEquals(ReimbursementType.OTHER,retrieved3.get(0).getType())
        );

    }

    @Test
    void getAllReimbursementsForUser() {
        List<Reimbursement> userReimbursements=new ArrayList<>();
        userReimbursements.add(reimbursements.get(0));
        userReimbursements.add(reimbursements.get(3));
        User user = new User();
        user.setUserID(1);
        Mockito.when(reimbursementDao.selectAllReimbursementsForUser(user)).thenReturn(userReimbursements);
        List<Reimbursement> retrieved=reimbursementService.getAllReimbursementsForUser(user);
        assertEquals(2,retrieved.size());
        assertAll(
                ()->assertEquals(2,retrieved.size()),
                ()->assertEquals(1,retrieved.get(0).getAuthorID()),
                ()->assertEquals(1,retrieved.get(1).getAuthorID()),
                ()->assertEquals("Rafael Malespin",retrieved.get(0).getAuthorName()),
                ()->assertEquals("Rafael Malespin",retrieved.get(1).getAuthorName()),
                ()->assertEquals(ReimbursementStatus.PENDING,retrieved.get(0).getStatus()),
                ()->assertEquals(ReimbursementStatus.DENIED,retrieved.get(1).getStatus())
        );
    }

    @Test
    void getReimbursementsForUserByStatus() {
        List<Reimbursement> userReimbursements=new ArrayList<>();
        userReimbursements.add(reimbursements.get(0));
        //userReimbursements.add(reimbursements.get(3));
        User user = new User();
        user.setUserID(1);
        Mockito.when(reimbursementDao.selectAllReimbursementsForUserBasedOnStatus(user,ReimbursementStatus.PENDING)).thenReturn(userReimbursements);
        List<Reimbursement> retrieved=reimbursementService.getReimbursementsForUserByStatus(user,"PENDING");
        assertAll(
                ()->assertEquals(1,retrieved.get(0).getReimbursementID()),
                ()->assertEquals(300,retrieved.get(0).getAmount()),
                ()->assertEquals(new Timestamp(1612202712),retrieved.get(0).getSubmittedTime()),
                ()->assertNull(retrieved.get(0).getResolvedTime()),
                ()->assertEquals("Company Dinner",retrieved.get(0).getDescription()),
                ()->assertEquals(1,retrieved.get(0).getAuthorID()),
                ()->assertEquals(0,retrieved.get(0).getResolverID()),
                ()->assertEquals(ReimbursementStatus.PENDING,retrieved.get(0).getStatus()),
                ()->assertEquals(ReimbursementType.FOOD,retrieved.get(0).getType())
        );
    }

    @Test
    void updateACertainReimbursement() {
        Mockito.when(reimbursementDao.updateReimbursementStatus(reimbursements.get(0))).thenReturn(false);
        Mockito.when(reimbursementDao.updateReimbursementStatus(reimbursements.get(2))).thenReturn(true);
        Mockito.when(reimbursementDao.updateReimbursementStatus(reimbursements.get(3))).thenReturn(true);
        assertAll(
                ()-> assertFalse(reimbursementService.updateACertainReimbursement(reimbursements.get(0))),
                ()-> assertTrue(reimbursementService.updateACertainReimbursement(reimbursements.get(2))),
                ()-> assertTrue(reimbursementService.updateACertainReimbursement(reimbursements.get(3)))
        );
    }
    @Test
    void submitANewReimbursementRequest() {
        Mockito.when(reimbursementDao.insertNewReimbursement(reimbursements.get(0))).thenReturn(true);
        Mockito.when(reimbursementDao.insertNewReimbursement(reimbursements.get(2))).thenReturn(true);
        assertAll(
                ()-> assertTrue(reimbursementService.submitANewReimbursementRequest(reimbursements.get(0))),
                ()-> assertTrue(reimbursementService.submitANewReimbursementRequest(reimbursements.get(2)))
        );
    }
}