package com.model;

import java.sql.Timestamp;
import java.util.Date;

public class Reimbursement {
    private int reimbursementID;
    private float amount;
    private Date submittedTime;
    private Date resolvedTime;
    private String description;
    private int authorID;
    private int resolverID;
    private ReimbursementStatus status;
    private ReimbursementType type;
    private String authorName;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getResolverName() {
        return resolverName;
    }

    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
    }

    private String resolverName;

    public Reimbursement() {
    }

    public Reimbursement(int reimbursementID,
                         float amount,
                         Timestamp submittedTime,
                         Timestamp resolvedTime,
                         String description,
                         int authorID,
                         int resolverID,
                         ReimbursementStatus status,
                         ReimbursementType type,
                         String authorName,
                         String resolverName) {
        this.reimbursementID = reimbursementID;
        this.amount = amount;
        this.submittedTime = submittedTime;
        this.resolvedTime = resolvedTime;
        this.description = description;
        this.authorID = authorID;
        this.resolverID = resolverID;
        this.status = status;
        this.type = type;
        this.authorName=authorName;
        this.resolverName=resolverName;
    }

    public int getReimbursementID() {
        return reimbursementID;
    }

    public void setReimbursementID(int reimbursementID) {
        this.reimbursementID = reimbursementID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getSubmittedTime() {
        return submittedTime;
    }

    public void setSubmittedTime(Timestamp submittedTime) {
        this.submittedTime = submittedTime;
    }

    public Date getResolvedTime() {
        return resolvedTime;
    }

    public void setResolvedTime(Timestamp resolvedTime) {
        this.resolvedTime = resolvedTime;
    }
//    public void setResolvedTime(Long timeInMilliseconds){
//        this.resolvedTime=new Timestamp(timeInMilliseconds);
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public int getResolverID() {
        return resolverID;
    }

    public void setResolverID(int resolverID) {
        this.resolverID = resolverID;
    }

    public ReimbursementStatus getStatus() {
        return status;
    }

    public void setStatus(ReimbursementStatus reimbursementStatus) {
        this.status = reimbursementStatus;
    }

    public ReimbursementType getType() {
        return type;
    }

    public void setType(ReimbursementType reimbursementType) {
        this.type = reimbursementType;
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "reimbursementID=" + reimbursementID +
                ", amount=" + amount +
                ", submittedTime=" + submittedTime +
                ", resolvedTime=" + resolvedTime +
                ", description='" + description + '\'' +
                ", authorID=" + authorID +
                ", resolverID=" + resolverID +
                ", status=" + status +
                ", type=" + type +
                ", authorName='" + authorName + '\'' +
                ", resolverName='" + resolverName + '\'' +
                '}';
    }
}
