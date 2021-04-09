package com.model;

public enum ReimbursementStatus {
    PENDING(1),APPROVED(2),DENIED(3);
    private final int value;
    public int getValue() {
        return value;
    }
    ReimbursementStatus(int value){
        this.value=value;
    }

    public static ReimbursementStatus getStatus(String val){
        if(val.equalsIgnoreCase(PENDING.toString())){
            return PENDING;
        }
        else if(val.equalsIgnoreCase(APPROVED.toString())){
            return APPROVED;
        }
        else if(val.equalsIgnoreCase(DENIED.toString())){
            return DENIED;
        }
        return null;
    }
}
